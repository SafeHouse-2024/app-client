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
    private List<String> dados = new ArrayList<>();
    public String pingar(String so, Integer opcao) {
        try {
            if (so.toUpperCase().contains("WIN")) {
                PowerShellResponse response = PowerShell.executeSingleCommand(comando);
                String resposta = response.getCommandOutput();

                List<String> linhas = List.of(resposta.split("\n"));
                List<String[]> partes = new ArrayList<>();
                for (String linha : linhas) {
                    partes.add(linha.split(": "));
                }
                for (String[] parte : partes) {
                    if (parte.length >= 2) {
                        String titulo = parte[0];
                        dados.add(parte[1]);
                    }
                }
                if (opcao == 1) {
                    return dados.get(0);
                } else if (opcao == 2) {
                    return dados.get(1);
                } else if (opcao == 3) {
                    return dados.get(2);
                }
            } else {
                return "Sistema operacional não suportado.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Ocorreu um erro: " + e.getMessage();
        }
        return null;
    }
}

