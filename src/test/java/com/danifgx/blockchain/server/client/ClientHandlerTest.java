package com.danifgx.blockchain.server.client;

import com.danifgx.blockchain.model.Blockchain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientHandlerTest {

    private Socket socket;
    private Blockchain blockchain;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;
    private ClientHandler clientHandler;

    @BeforeEach
    public void setUp() throws IOException {
        socket = mock(Socket.class);
        blockchain = mock(Blockchain.class);
    }

    @Test
    public void testAuthenticationSuccess() throws IOException {
        Socket socket = mock(Socket.class);
        DataInputStream input = new DataInputStream(new ByteArrayInputStream("user1\npassword1\n".getBytes()));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(outContent);

        when(socket.getInputStream()).thenReturn(input);
        when(socket.getOutputStream()).thenReturn(output);

        ClientHandler clientHandler = new ClientHandler(socket, blockchain);
        clientHandler.setAuthenticator(new TestAuthenticator());

        clientHandler.run();

        DataInputStream resultStream = new DataInputStream(new ByteArrayInputStream(outContent.toByteArray()));

        assertTrue(outContent.toString().contains("SUCCESS"));
    }



    @Test
    public void testAuthenticationFailure() throws IOException {
        Socket socket = mock(Socket.class);
        DataInputStream input = new DataInputStream(new ByteArrayInputStream("user1\ncontraseña\n".getBytes()));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(outContent);

        when(socket.getInputStream()).thenReturn(input);
        when(socket.getOutputStream()).thenReturn(output);

        ClientHandler clientHandler = new ClientHandler(socket, blockchain);
        clientHandler.setAuthenticator(new TestAuthenticator());

        clientHandler.run();

        DataInputStream resultStream = new DataInputStream(new ByteArrayInputStream(outContent.toByteArray()));

        assertTrue(outContent.toString().contains("FAILURE"));
    }


    @Test
    public void testIOExceptionWhileAuthenticating() throws IOException {
        when(socket.getInputStream()).thenThrow(new IOException("Fake IOException"));

        assertThrows(IOException.class, () -> new ClientHandler(socket, blockchain));
    }




    class TestAuthenticator implements Authenticator {
        @Override
        public boolean authenticate(String username, String password) {
            return "user1".equals(username) && "password1".equals(password);
        }
    }
}
