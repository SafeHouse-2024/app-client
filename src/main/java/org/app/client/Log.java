package org.app.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {

    private static final int MAX_LINES = 100;
    private static int lineCount = 0;
    private static int fileCount = 1;
    private static BufferedWriter bw;
    private static FileWriter fw;

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 1; i++) {
            generateLog("Arroz e feijão");
        }
    }

    public static void generateLog(String mensagem) throws IOException {
        if (bw == null) {
            openNewLogFile();
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);

        bw.write(timestamp + " - " + mensagem);
        bw.newLine();
        lineCount++;

        if (lineCount >= MAX_LINES) {
            bw.close();
            fw.close();
            lineCount = 0;
            openNewLogFile();
        }

        System.out.println("Log Gerado com sucesso!");
    }

    private static void openNewLogFile() throws IOException {
        String nomeUser = System.getProperty("user.name");
        String so = System.getProperty("os.name");

        Path mainDirectoryPath;

        if (so.toUpperCase().contains("win".toUpperCase())) {
            mainDirectoryPath = Paths.get("C:\\Users\\%s\\Documents".formatted(nomeUser));
        } else {
            mainDirectoryPath = Paths.get("/home/%s/Documents".formatted(nomeUser));
        }

        if (!Files.exists(mainDirectoryPath)) {
            Files.createDirectory(mainDirectoryPath);
        }

        Path logDirectoryPath = mainDirectoryPath.resolve("logs-SPECTRA");

        if (!Files.exists(logDirectoryPath)) {
            Files.createDirectory(logDirectoryPath);
        }

        String logFileName = "log_de_seguranca_" + fileCount + ".txt";
        File logFile = new File(logDirectoryPath.toString(), logFileName);

        if (!logFile.exists()) {
            logFile.createNewFile();
        }

        fw = new FileWriter(logFile, true);
        bw = new BufferedWriter(fw);
        fileCount++;
    }
}