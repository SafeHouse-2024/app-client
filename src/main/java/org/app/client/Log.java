package org.app.client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {

    private static final long MAX_SIZE = 4 * 1024;
    private static int fileCount = 1;
    private static BufferedWriter bw;
    private static FileWriter fw;
    private static Path logFilePath;
    private static LogType logType;

    public Log(LogType logType) {
        this.logType = logType;
    }

    public static void generateLog(String mensagem) throws IOException {
        if (bw == null || Files.size(logFilePath) >= MAX_SIZE) {
            openNewLogFile();
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);

        bw.write(timestamp + " - " + mensagem);
        bw.newLine();
        bw.flush();

        System.out.println("Log Gerado com sucesso!");
    }

    private static void openNewLogFile() throws IOException {
        String nomeUser = System.getProperty("user.name");
        String so = System.getProperty("os.name");

        Path mainDirectoryPath;

        if (so.toUpperCase().contains("win".toUpperCase())) {
            mainDirectoryPath = Paths.get(String.format("C:\\Users\\%s\\Documents", nomeUser));
        } else {
            mainDirectoryPath = Paths.get(String.format("/home/%s/Documents", nomeUser));
        }

        if (!Files.exists(mainDirectoryPath)) {
            Files.createDirectory(mainDirectoryPath);
        }

        Path logDirectoryPath = mainDirectoryPath.resolve("logs-SPECTRA");

        if (!Files.exists(logDirectoryPath)) {
            Files.createDirectory(logDirectoryPath);
        }

        if (logType == LogType.INICIALIZACAO) {
            String logFileName = "log_inicializacao_" + fileCount + ".txt";
            logFilePath = logDirectoryPath.resolve(logFileName);

            String message = "/****************/\n" +
                    "* Nome da Empresa: SPECTRA\n" +
                    "* Tipo de Documento: Confidencial\n" +
                    "* \n" +
                    "* Este documento contém informações confidenciais\n" +
                    "* da empresa SPECTRA. A divulgação, distribuição,\n" +
                    "* ou cópia deste documento é estritamente proibida\n" +
                    "* sem autorização prévia.\n" +
                    "/****************/\n\n";
            fw = new FileWriter(logFilePath.toFile(), true);
            bw = new BufferedWriter(fw);
            bw.write(message);
            bw.flush();
        } else {
            String logFileName = "log_de_seguranca_" + fileCount + ".txt";
            logFilePath = logDirectoryPath.resolve(logFileName);
            fw = new FileWriter(logFilePath.toFile(), true);
            bw = new BufferedWriter(fw);
        }

        fileCount++;
    }
}