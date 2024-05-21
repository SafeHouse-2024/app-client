package org.app.client.util;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ping {
    private static final String SPEEDTEST_CLI_COMMAND = "speedtest-cli --simple";

    private String ping;
    private String download;
    private String upload;

    public void pingar() {
        try {
            Process process = Runtime.getRuntime().exec(SPEEDTEST_CLI_COMMAND);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            ping = reader.readLine();
            download = reader.readLine();
            upload = reader.readLine();

            System.out.println("salve " + download);
            process.waitFor();
//            process.destroy();
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao executar o comando SpeedTest CLI: " + e.getMessage());
        }
    }

    public String getPing() {
        return ping;
    }

    public String getDownload() {
        return download;
    }

    public String getUpload() {
        return upload;
    }
}

