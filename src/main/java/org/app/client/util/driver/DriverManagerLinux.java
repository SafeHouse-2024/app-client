package org.app.client.util.driver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

public class DriverManagerLinux {

    public static void removerDriversInvalidos(String user){
        File pendrivesLinux = new File("/media/%s".formatted(user));
        List<String> pendrives = List.of(pendrivesLinux.list());
        pendrives.forEach(pendrive -> {
            try {
                Process p = Runtime.getRuntime().exec("sudo -S eject /media/%s/%s".formatted(user, pendrive));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
                //Passar a senha do sudo
                bw.write("");
                bw.flush();
                bw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
