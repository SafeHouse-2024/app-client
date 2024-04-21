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


    public String autenticar() {

        Scanner scanner = new Scanner(System.in);

        Console console = System.console();
        String senha = "";

        while (true) {
            System.out.println("Digite o email: ");
            String email = scanner.nextLine();
            System.out.println("Digite a senha: ");
            if (console != null){
                senha = String.valueOf(console.readPassword());
            } else {
                senha = scanner.nextLine();
            }
            System.out.println("Digite o código de acesso da máquina");
            String codigoAcesso = scanner.nextLine();

            if (autenticar(email, senha, codigoAcesso)) {
                System.out.println("Usuário autenticado");
                return codigoAcesso;
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




