package org.app.client.login;

import org.app.client.conexao.Conexao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Login {

    Conexao conexao = new Conexao();
    private static final int MAX_TENTATIVAS = 3;


    public String autenticar(Integer fkSistemaOperacional) {

        Scanner scanner = new Scanner(System.in);

        Console console = System.console();
        String senha = "";
        String sudo = " ";
        int tentativas = 0;

        while (tentativas < MAX_TENTATIVAS) {
            System.out.println("Digite o email: ");
            String email = scanner.nextLine();
            System.out.println("Digite a senha: ");
            if (console != null){
                senha = String.valueOf(console.readPassword());
            } else {
                senha = scanner.nextLine();
            }
            if (fkSistemaOperacional == 2){
                System.out.println("Digite a senha do sudo: ");
                if (console != null){
                    sudo = String.valueOf(console.readPassword());
                }else {
                    sudo = scanner.nextLine();
                }
            }
            System.out.println("Digite o código de acesso da máquina");
            String codigoAcesso = scanner.nextLine();

            if (autenticar(email, senha, codigoAcesso)) {
                System.out.println("Usuário autenticado");
                return codigoAcesso + "#" + sudo;
            } else {
                tentativas++;
                System.out.println("Senha incorreta. Tentativas restantes: " + (MAX_TENTATIVAS - tentativas));
            }
        }

        System.out.println("Limite de tentativas excedido. O login está bloqueado.");
        return null;
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




