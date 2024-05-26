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

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 1; i++) {
            generateLog("Arroz e feijão");
        }
    }

    public static void generateLog(String mensagem) throws IOException {
        Path mainDirectoryPath = Paths.get("C:\\Users\\angel\\OneDrive\\Área de Trabalho\\FACULDADE NATHAN");

        if (!Files.exists(mainDirectoryPath)) {
            Files.createDirectory(mainDirectoryPath);
        }

        Path logDirectoryPath = mainDirectoryPath.resolve("logs");

        if (!Files.exists(logDirectoryPath)) {
            Files.createDirectory(logDirectoryPath);
        }

        File logFile = new File(logDirectoryPath.toString(), "logs.txt");

        if (!logFile.exists()) {
            logFile.createNewFile();
        }

        FileWriter fw = new FileWriter(logFile, true);
        BufferedWriter bw = new BufferedWriter(fw);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);

        bw.write(timestamp + " - " + mensagem);
        bw.newLine();

        bw.close();
        fw.close();

        System.out.println("Log generated successfully!");
    }
}
