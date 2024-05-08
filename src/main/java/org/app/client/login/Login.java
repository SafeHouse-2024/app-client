package org.app.client.login;

import org.app.client.conexao.Conexao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Console;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Login {

    Conexao conexao = new Conexao();

    public String autenticar(Integer fkSistemaOperacional) {

        Scanner scanner = new Scanner(System.in);
        Console console = System.console();
        String senha = "";
        String sudo = " ";

        while (true) {
            System.out.println("Digite o email: ");
            String email = scanner.nextLine();
            System.out.println("Digite a senha: ");
            if (console != null) {
                senha = String.valueOf(console.readPassword());
            } else {
                senha = scanner.nextLine();
            }
            if (fkSistemaOperacional == 2) {
                System.out.println("Digite a senha do sudo: ");
                if (console != null) {
                    sudo = String.valueOf(console.readPassword());
                } else {
                    sudo = scanner.nextLine();
                }
            }
            System.out.println("Digite o código de acesso da máquina");
            String codigoAcesso = scanner.nextLine();

            if (autenticar(email, hashSenha(senha), codigoAcesso)) {
                System.out.println("Usuário autenticado");
                return codigoAcesso + "#" + sudo;
            } else {
                System.out.println("Credenciais inválidas, tente novamente.");
            }
        }
    }

    private String hashSenha(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(senha.getBytes());
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String bytesToHex(byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private boolean autenticar(String email, String senhaHash, String codigoAcesso) {
        JdbcTemplate getConexao = conexao.getJdbcTemplate();

        String usuario = null;
        try {
            usuario = getConexao.queryForObject(
                    "SELECT u.email FROM Usuario u JOIN Computador c ON c.fkUsuario = u.idUsuario WHERE u.email = ? AND u.senha = ? AND c.codigoAcesso = ? AND u.tipo = ?",
                    new BeanPropertyRowMapper<>(String.class),
                    email, senhaHash, codigoAcesso, "Máquina"
            );
        } catch (EmptyResultDataAccessException e) {
            System.out.println("Não foi possível se autenticar, preencha os campos corretamente");
        }

        return usuario != null;
    }
}

