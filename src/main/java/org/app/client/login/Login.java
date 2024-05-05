package org.app.client.login;

import org.app.client.conexao.Conexao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Scanner;

public class Login {

    Conexao conexao = new Conexao();
    private int tentativasFalhas = 0;
    private boolean bloqueado = false;
    private long tempoBloqueio = 0;


    public String autenticar(Integer fkSistemaOperacional) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (bloqueado) {
                long agora = System.currentTimeMillis();
                if (agora < tempoBloqueio) {
                    System.out.println("O site está bloqueado. Tente novamente mais tarde.");
                    return null; // Ou outra ação, dependendo do contexto
                } else {
                    // Reiniciar as tentativas após o tempo de bloqueio expirar
                    bloqueado = false;
                    tentativasFalhas = 0;
                    System.out.println("O bloqueio foi removido. Você pode tentar novamente.");
                }
            }

            System.out.println("Digite o email: ");
            String email = scanner.nextLine();
            System.out.println("Digite a senha: ");
            String senha = scanner.nextLine();
            System.out.println("Digite o código de acesso da máquina");
            String codigoAcesso = scanner.nextLine();

            if (autenticar(email, senha, codigoAcesso)) {
                System.out.println("Usuário autenticado");
                return codigoAcesso;
            } else {
                tentativasFalhas++;
                if (tentativasFalhas >= 3) {
                    // Bloquear o acesso por 1 minuto
                    bloqueado = true;
                    tempoBloqueio = System.currentTimeMillis() + 60000; // 1 minuto em milissegundos
                    System.out.println("Você excedeu o número de tentativas permitidas. O site está bloqueado por 1 minuto.");
                } else {
                    System.out.println("Tentativa de login falhou. Tente novamente.");
                }
            }
        }
    }


    private boolean autenticar(String email, String senha, String codigoAcesso) {
        JdbcTemplate getConexao = conexao.getJdbcTemplate();

        String usuario = null;
        try{
            usuario = getConexao.queryForObject("SELECT f.email FROM Funcionario f JOIN Computador c ON c.fkFuncionario = f.idFuncionario " +
                    "WHERE f.email = ? AND f.senha = ? AND c.codigoAcesso = ?", new BeanPropertyRowMapper<>(String.class), email, senha, codigoAcesso);
        }catch (EmptyResultDataAccessException e){
            System.out.println("Não foi possível se autenticar, preencha os campos corretamente");
        }

        return usuario != null;
    }

}




