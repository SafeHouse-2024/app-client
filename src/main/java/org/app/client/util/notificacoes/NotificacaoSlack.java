package org.app.client.util.notificacoes;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

public class NotificacaoSlack {
    private static final String WEBHOOK_URL = "https://hooks.slack.com/services/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";
    public static void main(String[] args) {
        try {
            EnviarNotificacaoSlack("Hello from Java!");
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