package org.app.client.login;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Login {

    private static Map<String, String> acesso = new HashMap<>();


    public static void autenticar() {

        Scanner scanner = new Scanner(System.in);
        acesso.put("admin", "admin");

        while (true) {
            System.out.println("Digite o usuário: ");
            String usuario = scanner.nextLine();
            System.out.println("Digite a senha: ");
            String senha = scanner.nextLine();

            if (autenticar(acesso, usuario, senha)) {
                System.out.println("Usuário autenticado");
                break;
            } else {
                System.out.println("Usuário não autenticado");
            }
        }

        scanner.close();
    }

    private static boolean autenticar(Map<String, String> acesso, String usuario, String senha){
        return acesso.containsKey(usuario) && acesso.get(usuario).equals(criptografarSenha(senha));
    }
    private static String criptografarSenha(String senha) {
        return senha;
    }

}




