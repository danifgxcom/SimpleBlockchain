package com.danifgx.blockchain.server.client;

import com.danifgx.blockchain.model.Blockchain;
import com.danifgx.blockchain.model.Transaction;
import com.danifgx.blockchain.server.authentication.UserAuthentication;
import com.danifgx.blockchain.util.StringUtil;
import lombok.Setter;

import java.io.*;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;

interface Authenticator {
    boolean authenticate(String username, String password);
}

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Blockchain blockchain;
    private final DataInputStream input;
    private final DataOutputStream output;
    @Setter
    private Authenticator authenticator;

    public ClientHandler(Socket socket, Blockchain blockchain) throws IOException {
        this.socket = socket;
        this.blockchain = blockchain;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        this.authenticator = new UserAuthenticator();
    }

    public ClientHandler(Socket socket, Blockchain blockchain, Authenticator authenticator) throws IOException {
        this.socket = socket;
        this.blockchain = blockchain;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        this.authenticator = authenticator != null ? authenticator : new UserAuthenticator();
    }

    @Override
    public void run() {
        try {
            authenticateUser();
        } catch (IOException e) {
            handleError("Client exception: ", e);
            throw new RuntimeException("Failed to authenticate user", e);
        }
    }


    private void authenticateUser() throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        writer.write("Please enter your username: ");
        writer.flush();
        String username = reader.readLine();

        writer.write("Please enter your password: ");
        writer.flush();
        String password = reader.readLine();

        boolean authenticated = authenticator.authenticate(username, password);
        System.out.println(authenticated);


        if (authenticated) {
            output.writeUTF("SUCCESS");
            System.out.println("Authenticated!");
            try {
                handleClientRequest();
            } catch (Exception e) {
                System.err.println("Error handling client request: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            output.writeUTF("FAILURE");
            System.out.println("Authentication failed!");
        }


    }

    private void handleClientRequest() throws IOException {
        String clientRequest = input.readUTF();
        RequestHandler requestHandler = new RequestHandler(socket, blockchain, input, output);
        requestHandler.handleRequest(clientRequest);
    }

    private void handleError(String message, Exception e) {
        System.out.println(message);
        e.printStackTrace();
    }

}

class RequestHandler {
    private final Socket socket;
    private final Blockchain blockchain;
    private final DataInputStream input;
    private final DataOutputStream output;

    public RequestHandler(Socket socket, Blockchain blockchain, DataInputStream input, DataOutputStream output) {
        this.socket = socket;
        this.blockchain = blockchain;
        this.input = input;
        this.output = output;
    }

    public void handleRequest(String clientRequest) throws IOException {
        if (clientRequest.startsWith(RequestTypes.ADD_TRANSACTION)) {
            handleAddTransaction(clientRequest);
        } else if (clientRequest.equals(RequestTypes.GET_BLOCKCHAIN)) {
            handleGetBlockchain();
        } else if (clientRequest.startsWith(RequestTypes.SYNC_BLOCKCHAIN)) {
            handleSyncBlockchain(clientRequest);
        } else if (clientRequest.equals(RequestTypes.VALIDATE_BLOCKCHAIN)) {
            handleValidateBlockchain();
        } else if (clientRequest.equals(RequestTypes.DISCONNECT)) {
            handleDisconnect();
        } else {
            output.writeUTF("ERROR:Unknown Request");
        }
    }

    private void handleAddTransaction(String request) throws IOException {
        String[] parts = request.split(":");
        if (parts.length == 5) {
            String senderString = parts[1];
            String receiverString = parts[2];
            double amount = Double.parseDouble(parts[3]);
            String privateKeyString = parts[4];

            PrivateKey privateKey = StringUtil.getPrivateKeyFromString(privateKeyString);
            PublicKey senderKey = StringUtil.getKeyFromString(senderString);
            PublicKey receiverKey = StringUtil.getKeyFromString(receiverString);

            Transaction transaction = new Transaction(senderKey, receiverKey, amount);
            transaction.signTransaction(privateKey);

            boolean success = blockchain.addTransaction(transaction);
            output.writeUTF(success ? "SUCCESS" : "ERROR");
        } else {
            output.writeUTF("ERROR:Malformed Request");
        }
    }


    private void handleGetBlockchain() throws IOException {
        // TODO: Serialize and send the blockchain data
        String serializedBlockchain = ""; // Serialize the blockchain object
        output.writeUTF(serializedBlockchain);
    }

    private void handleSyncBlockchain(String request) throws IOException {
        String serializedData = request.substring(15); // Extract serialized data
        // TODO: Deserialize and sync the blockchain data
        // Respond with SUCCESS or ERROR as needed
        output.writeUTF("SUCCESS");
    }

    private void handleValidateBlockchain() throws IOException {
        // TODO: Validate the blockchain
        boolean isValid = blockchain.isValid();
        output.writeUTF(isValid ? "VALID" : "INVALID");
    }

    private void handleDisconnect() throws IOException {
        System.out.println("DISCONNECTION!!!!");
        this.input.close();
        this.output.close();
        this.socket.close();

        System.out.println("Client disconnected.");
    }
}

class UserAuthenticator implements Authenticator {
    @Override
    public boolean authenticate(String username, String password) {
        return UserAuthentication.authenticate(username, password);
    }
}

class ResponseMessages {
    public static final String ERROR_UNKNOWN_REQUEST = "ERROR:Unknown Request";
}
