package org.app.client.util.websocket;

import io.socket.client.IO;
import io.socket.client.Socket;
import org.app.client.dao.entity.DarkStore;
import org.app.client.dao.entity.Empresa;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Websocket {

    public static Socket initializeWebsocketClient(Empresa empresa, DarkStore darkStore, String macAddress) throws URISyntaxException {
        Map<String, List<String>> nome = new HashMap<>();
        String nomeEmpresa = empresa.getNome();
        String nomeDarkStore = darkStore.getNome();
        nome.put("token", List.of("123"));
        nome.put("Empresa", List.of(nomeEmpresa));
        nome.put("DarkStore", List.of(nomeDarkStore));
        nome.put("MacAddress", List.of(macAddress));
        IO.Options options = IO.Options.builder().setExtraHeaders(nome).build();
        Socket socket = IO.socket("http://127.0.0.1:3001", options).connect();
        socket.on(Socket.EVENT_CONNECT, objects -> System.out.println("Conectado"));
        socket.on(Socket.EVENT_DISCONNECT, objects -> System.out.println("Desconectado"));
        return socket;
    }

    public static void defineEventMessage(String message, Empresa empresa, DarkStore darkStore, String macAddress) throws URISyntaxException {
        initializeWebsocketClient(empresa, darkStore, macAddress).emit("send_message_%s".formatted(empresa.getNome()), message);
        initializeWebsocketClient(empresa, darkStore, macAddress).emit("send_message_%s_%s".formatted(empresa.getNome(), darkStore.getNome()), message);
    }
}
