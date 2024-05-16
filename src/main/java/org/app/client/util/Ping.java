package org.app.client.util;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Ping {
    private String comando = "speedtest-cli --simple";

    public String pingar(String so, Integer opcao) {
        try {
            ProcessBuilder builder = new ProcessBuilder(new String[0]);
            if (so.toUpperCase().contains("win".toUpperCase())) {
                PowerShellResponse response = PowerShell.executeSingleCommand(this.comando);
                StringBuilder output = new StringBuilder();
                String resposta = response.getCommandOutput();
                List<String> linhas = List.of(resposta.split("\n"));
                List<String> partes = new ArrayList<>();
                for (String linha : linhas) {
                    partes.add(linha.split(": "));
                    String titulo = partes[0];
                    String dado = partes[1];
                }

            } else if (so.contains("nux")) {
                builder.command(new String[] { "bash", "-c", this.comando });
                Process processo = builder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(processo.getInputStream()));
                String linha;
                while ((linha = reader.readLine()) != null)
                    System.out.println(linha);
                int exitCode = processo.waitFor();
                System.out.println("Codigo de saida do processo: " + exitCode);

                for (String comando : comandoPing) {
                    String[] partes = comando.split("/n");
                    if (partes.length == 2) {
                        String label = partes[0].trim();
                        String value = partes[1].trim();
                        switch (label.toLowerCase()) {
                            case "ping":
                                System.out.println(value);
                                break;
                            case "download":
                                System.out.println(value);
                                break;
                            case "upload":
                                System.out.println(value);
                                break;
                        }
                    }
                }
            }
        } catch (IOException|InterruptedException e) {
            e.printStackTrace();
        }
    }
}

