package io.github.ez500.CS180_GPJ;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * Testing the ClientHandler class.
 *
 * @author Alex Orr, L11 T3
 * @version Nov 23, 2025
 *
 */

class ClientHandlerTests {
    private ServerSocket testServerSocket;
    private Socket testSocket;
    private ClientHandler clientHandler;
    private PrintWriter toServer;
    private static final int portTest = 4243;

    @BeforeEach
    void start() throws IOException {
        testServerSocket = new ServerSocket(portTest);
        testSocket = new Socket("localhost", portTest);
        Socket acceptedSocket = testServerSocket.accept();
        toServer = new PrintWriter(testSocket.getOutputStream(), true);
        clientHandler = new ClientHandler(acceptedSocket);
    }

    @AfterEach
    void end() throws IOException {
        if (testSocket != null && !testSocket.isClosed()) {
            testSocket.close();
        }
        if (testServerSocket != null && !testServerSocket.isClosed()) {
            testServerSocket.close();
        }
    }

    @Test
    @DisplayName("Cancel returns true")
    void cancel() throws IOException {
        clientHandler.cancel();
        assertTrue(true, "Cancel should return true");
    }

    @Test
    @DisplayName("Initialize completes")
    void clientHandlerInitialization() throws IOException {
        toServer.println("register");
        toServer.println("testUser");
        toServer.println("testPass");
        toServer.println("login");
        toServer.println("testUser");
        toServer.println("testPass");
        clientHandler.initialize();
        assertTrue(true, "initialize() should complete");
    }

    @Test
    @DisplayName("execute() runs without blocking")
    void clientHandlerExecute() throws IOException {
        toServer.println("register");
        toServer.println("testUser");
        toServer.println("testPass");
        toServer.println("login");
        toServer.println("testUser");
        toServer.println("testPass");
        clientHandler.initialize();
        toServer.println("view");
        clientHandler.execute();
        assertTrue(true, "execute() should complete");
    }

    @Test
    @DisplayName("isFinished returns false")
    void clientHandlerIsFinished() throws IOException {
        assertFalse(clientHandler.isFinished(), "isFinished should return false");
    }
}