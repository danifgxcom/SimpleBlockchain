package com.danifgx.blockchain.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private final String serverName;
    private final int serverPort;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public void start() throws IOException {
        socket = new Socket(serverName, serverPort);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        System.out.println("Connected to server " + serverName + " on port " + serverPort);
    }

    public void sendMessage(Object message) throws IOException {
        out.writeObject(message);
    }

    public Object receiveMessage() throws IOException, ClassNotFoundException {
        return in.readObject();
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
