package com.danifgx.blockchain.client;

import com.danifgx.blockchain.server.authentication.UserAuthentication;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final String serverName;
    private final int serverPort;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    public Client(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public void start() throws IOException {
        socket = new Socket(serverName, serverPort);
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());

        System.out.print(in.readUTF());
        out.writeUTF("user1");

        System.out.print(in.readUTF());
        out.writeUTF("password1");

        String authStatus = in.readUTF();
        if ("SUCCESS".equals(authStatus)) {
            System.out.println("Authentication successful.");
        } else {
            System.out.println("Authentication failed.");
        }

        System.out.println("Connected to server " + serverName + " on port " + serverPort);
    }



    public void authenticate() throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Username: ");
        String username = scanner.nextLine();
        out.writeUTF(username);

        System.out.print("Password: ");
        String password = scanner.nextLine();
        if (password.isEmpty()) {
            System.out.println("La contraseña está vacía");
        } else {
            out.writeUTF(password);
        }

        boolean authStatus = in.readBoolean();
        if (authStatus) {
            System.out.println("Authentication successful.");
        } else {
            System.out.println("Authentication failed.");
        }

        scanner.close();
    }


    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
    }

    public String receiveMessage() throws IOException {
        return in.readUTF();
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
