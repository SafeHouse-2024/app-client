package org.app.client.notificacoes;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

public class NotificacaoSlack {
    private static final String WEBHOOK_URL = "https://hooks.slack.com/services/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";
    public static void main(String[] args) {
        try {
            sendSlackNotification("Hello from Java!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void sendSlackNotification(String message) throws Exception {
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