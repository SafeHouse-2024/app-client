package org.app.client.login;

import org.app.client.conexao.Conexao;
import org.app.client.conexao.ConexaoSql;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Console;
import java.util.Scanner;

public class Login {

    ConexaoSql conexaoSql = new ConexaoSql();


    public String autenticar(Integer fkSistemaOperacional) {

        Scanner scanner = new Scanner(System.in);

        Console console = System.console();
        String senha = "";
        String sudo = " ";

        while (true) {
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
            }
        }
    }

    private boolean autenticar(String email, String senha, String codigoAcesso) {

        String usuario = null;
        // Trocar para conexão remota
        try{
            JdbcTemplate getConexao = conexaoSql.getJdbcTemplate();
            usuario = getConexao.queryForObject("SELECT u.email FROM Usuario u JOIN Computador c ON c.fkUsuario = u.idUsuario WHERE u.email = ? AND u.senha = ? AND c.codigoAcesso = ? AND u.tipo = ?", new BeanPropertyRowMapper<>(String.class), email, senha, codigoAcesso, "Maquina");
        }catch (Exception e){
            System.out.println("Não foi possível se autenticar, preencha os campos corretamente");
        }

        return usuario != null;
    }

}




