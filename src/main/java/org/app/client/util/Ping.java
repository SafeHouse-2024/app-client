package org.app.client.util;

import com.profesorfalken.jpowershell.PowerShell;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ping {
    private String comando = "speedtest-cli --simple";

    public void pingar(String so) {
        try {
            ProcessBuilder builder = new ProcessBuilder(new String[0]);
            if (so.toUpperCase().contains("win".toUpperCase())) {
                PowerShell.executeSingleCommand(this.comando);
            } else if (so.contains("nux")) {
                builder.command(new String[] { "bash", "-c", this.comando });
                Process processo = builder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(processo.getInputStream()));
                String linha;
                while ((linha = reader.readLine()) != null)
                    System.out.println(linha);
                int exitCode = processo.waitFor();
                System.out.println("Cde sado processo: " + exitCode);
            }
        } catch (IOException|InterruptedException e) {
            e.printStackTrace();
        }
    }
}

