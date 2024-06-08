package org.app.client.herancas;

import org.app.client.dao.controller.CaracteristicaComponenteController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ping extends CaracteristicaComponenteController {
    private static final String SPEEDTEST_CLI_COMMAND = "speedtest-cli --simple";

    private String ping;
    private String download;
    private String upload;

    public Ping() {
        pingar();
    }

    public void pingar() {
        try {
            Process process = Runtime.getRuntime().exec(SPEEDTEST_CLI_COMMAND);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            ping = reader.readLine();
            download = reader.readLine();
            upload = reader.readLine();

            process.waitFor();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao executar o comando SpeedTest CLI: " + e.getMessage());
        }
    }

    public String getPing() {
        if(ping == null){
            return "0.0";
        }
        return ping.split(" ")[1];
    }

    public String getDownload() {
        if(download == null){
            return "0.0";
        }
        return download.split(" ")[1];
    }

    public String getUpload() {
        if(upload == null){
            return "0.0";
        }
        return upload.split(" ")[1];
    }
}

