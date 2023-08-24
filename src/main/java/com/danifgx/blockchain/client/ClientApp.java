package com.danifgx.blockchain.client;

import com.danifgx.blockchain.server.client.RequestTypes;

import java.io.IOException;

public class ClientApp {
    public static void main(String[] args) {
        Client client = new Client("localhost", 8080);
        try {
            client.start();

            System.out.print(client.receiveMessage());
            client.sendMessage("user1");

            System.out.print(client.receiveMessage());
            client.sendMessage("password1");

            boolean isAuthenticated = Boolean.parseBoolean(client.receiveMessage());

            client.stop();

            if (isAuthenticated) {
                System.out.println("Authenticated!");
                String request = RequestTypes.GET_BLOCKCHAIN;
                client.sendMessage(request);
                String response = client.receiveMessage();
                System.out.println("Server replied: " + response);
            } else {
                System.out.println("Authentication failed!");
            }

            client.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

