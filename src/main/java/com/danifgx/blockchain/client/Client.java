package com.danifgx.blockchain.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
        Scanner scanner = new Scanner(System.in);
        System.out.print(in.readUTF());
        out.writeUTF(scanner.nextLine());
        System.out.print(in.readUTF());
        out.writeUTF(scanner.nextLine());
        boolean authStatus = in.readBoolean();
        if (authStatus) {
            System.out.println("Authentication successful.");
        } else {
            System.out.println("Authentication failed.");
        }
        System.out.println("Connected to server " + serverName + " on port " + serverPort);
        // Aquí puedes continuar con el flujo de la aplicación una vez que el cliente esté autenticado
    }


    public void authenticate() throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Username: ");
        String username = scanner.nextLine();
        out.writeUTF(username);

        System.out.print("Password: ");
        String password = scanner.nextLine();
        out.writeUTF(password);

        boolean authStatus = in.readBoolean();
        if (authStatus) {
            System.out.println("Authentication successful.");
        } else {
            System.out.println("Authentication failed.");
        }
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
