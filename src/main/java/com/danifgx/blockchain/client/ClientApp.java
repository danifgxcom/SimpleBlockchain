package com.danifgx.blockchain.client;

import java.io.IOException;

public class ClientApp {
    public static void main(String[] args) {
        Client client = new Client("localhost", 8080);
        try {
            client.start();

            String request = "GET_BLOCKCHAIN";
            client.sendMessage(request);
            String response = client.receiveMessage();
            System.out.println("Server replied: " + response);

            client.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
