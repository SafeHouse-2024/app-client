package org.app.client.util.driver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class DriverManagerLinux {

    public static void removerDriversInvalidos(){
        try {
            // no lugar de ian, colocar o nome do usuário.
            Process p = Runtime.getRuntime().exec("sudo -S eject /media/ian/ESD-USB");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
            // Senha do sudo, não vai ser uma boa prática colocar ela direto no código, pode usar uma variável de ambiente.
            bw.write("");
            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
