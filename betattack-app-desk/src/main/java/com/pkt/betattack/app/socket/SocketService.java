package com.pkt.betattack.app.socket;

import com.pkt.betattack.app.api.config.AuthConfig;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

public class SocketService {

    public Socket socket;

    public SocketService() {
        try {
            this.socket = IO.socket(AuthConfig.SOCKET_SERVER);
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    socket.emit("connect-ui", AuthConfig.currentToken);
                    System.out.println("Server is connected");                }

            }).on("event", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    System.out.println(args[0]);
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Server is disconnected");
                }
            });
            this.socket.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return this.socket;
    }
}
