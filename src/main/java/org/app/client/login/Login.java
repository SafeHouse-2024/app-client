package org.app.client.login;

import org.app.client.conexao.Conexao;
import org.app.client.conexao.ConexaoSql;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Console;
import java.util.Scanner;
import java.time.Instant;

public class Login {

    ConexaoSql conexaoSql = new ConexaoSql();
    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCKOUT_DURATION = 60000;
    private int attempts = 0;
    private long lockoutEnd = 0;

    public String autenticar(Integer fkSistemaOperacional) {
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();
        String senha = "";
        String sudo = " ";

        while (true) {
            long currentTime = Instant.now().toEpochMilli();
            if (attempts >= MAX_ATTEMPTS && currentTime < lockoutEnd) {
                System.out.println("Too many failed attempts. Please wait for 1 minute.");
                try {
                    Thread.sleep(lockoutEnd - currentTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

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

            if (autenticar(email, senha, codigoAcesso)) {
                System.out.println("Usuário autenticado");
                attempts = 0;
                return codigoAcesso + "#" + sudo;
            } else {
                attempts++;
                if (attempts >= MAX_ATTEMPTS) {
                    lockoutEnd = Instant.now().toEpochMilli() + LOCKOUT_DURATION;
                    System.out.println("Too many failed attempts. Please wait for 1 minute.");
                }
            }
        }
    }

    private boolean autenticar(String email, String senha, String codigoAcesso) {
        String usuario = null;
        try {
            JdbcTemplate getConexao = conexaoSql.getJdbcTemplate();
            usuario = getConexao.queryForObject(
                    "SELECT u.email FROM Usuario u JOIN Computador c ON c.fkUsuario = u.idUsuario WHERE u.email = ? AND u.senha = ? AND c.codigoAcesso = ? AND u.tipo = ?",
                    new BeanPropertyRowMapper<>(String.class),
                    email, senha, codigoAcesso, "Maquina"
            );
        } catch (Exception e) {
            System.out.println("Não foi possível se autenticar, preencha os campos corretamente");
        }

        return usuario != null;
    }
}


