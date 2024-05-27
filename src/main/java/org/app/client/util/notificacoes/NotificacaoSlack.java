package org.app.client.util.notificacoes;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class NotificacaoSlack {
    private static final String WEBHOOK_URL;

    static {
        String url = "";
        try {
            url = new String(Files.readAllBytes(Paths.get("webhook_url")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        WEBHOOK_URL = url.trim();
    }
    public static void main(String[] args) {
        try {
            EnviarNotificacaoSlack("Teste funcionando com gitignore!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void EnviarNotificacaoSlack(String message) throws Exception {
        String payload = "{\"text\":\"" + message + "\"}";

        URL url = new URL(WEBHOOK_URL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setDoOutput(true);
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Accept", "application/json");

        try (OutputStream os = httpConn.getOutputStream()) {
            byte[] input = payload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        int responseCode = httpConn.getResponseCode();
        System.out.println("Response Code: " + responseCode);
    }
}