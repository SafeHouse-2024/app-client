package org.app.client.util.websocket;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Websocket {

    public static Socket initializeWebsocketClient() throws URISyntaxException {
        Map<String, List<String>> nome = new HashMap<>();
        String nomeEmpresa = "Rappi";
        nome.put("token", List.of("123"));
        nome.put("Empresa", List.of(nomeEmpresa));
        IO.Options options = IO.Options.builder().setExtraHeaders(nome).build();
        Socket socket = IO.socket("http://127.0.0.1:3001", options).connect();
        socket.on(Socket.EVENT_CONNECT, objects -> System.out.println("Conectado"));
//        System.out.println(socket.connected());
//        Thread.sleep(3000);
//        socket.close();
        socket.on(Socket.EVENT_DISCONNECT, objects -> System.out.println("Desconectado"));
        return socket;
    }
}
