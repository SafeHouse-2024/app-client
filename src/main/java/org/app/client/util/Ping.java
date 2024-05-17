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
    public void pingar() {

        PowerShellResponse response = PowerShell.executeSingleCommand(comando);
        System.out.println("aqui" + response.getCommandOutput());
        if (response.isError()) {
            System.out.println("Erro ao executar o comando");
        }
        String[] linhas = response.getCommandOutput().split("\n");
        for (String linha : linhas) {
            dados.add(linha);
        }
    }

    public String getDownload(){
        pingar();
        return dados.get(1);
    }

    public String getUpload(){
        pingar();
        return dados.get(2);
    }

    public String getPing(){
        pingar();
        return dados.get(0);
    }
}

