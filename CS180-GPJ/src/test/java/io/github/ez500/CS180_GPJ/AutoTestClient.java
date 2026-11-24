package io.github.ez500.CS180_GPJ;

import java.io.*;
import java.net.Socket;

/**
 * Automated test client for the Restaurant Manager System
 *
 * @author Max Koeninger, CS180 lab L11
 * @version Nov 24 2025
 */
public class AutoTestClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 4242;

    public static void main(String[] args) {
        runAutomatedTestSequence();
    }

    public static void runAutomatedTestSequence() {
        System.out.println("=== Running Automated Test Sequence ===");

        try (
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            BufferedReader serverIn = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            PrintWriter serverOut = new PrintWriter(
                socket.getOutputStream(),
                true
            )
        ) {
            testUserRegistration(serverIn, serverOut);
            testUserLogin(serverIn, serverOut);
            testReservationOperations(serverIn, serverOut);

            System.out.println("=== Automated Test Sequence Complete ===");
        } catch (Exception e) {
            System.err.println("Error in automated test: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testUserRegistration(
        BufferedReader serverIn,
        PrintWriter serverOut
    ) throws IOException {
        System.out.println("\n--- Testing Registration ---");

        String response = serverIn.readLine();
        System.out.println("Server: " + response);

        response = serverIn.readLine();
        System.out.println("Server: " + response);

        System.out.println("Sending: register");
        serverOut.println("register");

        response = serverIn.readLine();
        System.out.println("Server: " + response);
        System.out.println("Sending: testUser");
        serverOut.println("testUser");

        response = serverIn.readLine();
        System.out.println("Server: " + response);
        System.out.println("Sending: testPass");
        serverOut.println("testPass");

        // expected: account created successfully
        // if this is not the case, check that the account does not already exist
        response = serverIn.readLine();
        System.out.println("Server: " + response);

        // expected: main menu
        response = serverIn.readLine();
        System.out.println("Server: " + response);
    }

    private static void testUserLogin(
        BufferedReader serverIn,
        PrintWriter serverOut
    ) throws IOException {
        System.out.println("\n--- Testing Login ---");

        System.out.println("Sending: login");
        serverOut.println("login");

        String response = serverIn.readLine();
        System.out.println("Server: " + response);
        System.out.println("Sending: testUser");
        serverOut.println("testUser");

        response = serverIn.readLine();
        System.out.println("Server: " + response);
        System.out.println("Sending: testPass");
        serverOut.println("testPass");

        response = serverIn.readLine();
        System.out.println("Server: " + response);

        response = serverIn.readLine();
        System.out.println("Server: " + response);

        if (!response.contains("What would you like to do?")) {
            throw new IOException(
                "Login failed - cannot continue reservation tests"
            );
        }
    }

    private static void testReservationOperations(
        BufferedReader serverIn,
        PrintWriter serverOut
    ) throws IOException {
        System.out.println("\n--- Testing Reservation Operations ---");

        // expected: no reservations
        System.out.println("Sending: view");
        serverOut.println("view");

        String response = serverIn.readLine();
        System.out.println("Server: " + response);

        response = serverIn.readLine();
        System.out.println("Server: " + response);

        System.out.println("Sending: create");
        serverOut.println("create");

        response = serverIn.readLine();
        System.out.println("Server: " + response);

        System.out.println("Sending: 2024.12.25,19.00,4");
        serverOut.println("2024.12.25,19.00,4");

        // expected: reservation created successfully
        // if this is not the case, check if the file contains appropriate table
        response = serverIn.readLine();
        System.out.println("Server: " + response);

        response = serverIn.readLine();
        System.out.println("Server: " + response);

        System.out.println("Sending: view");
        serverOut.println("view");

        // expected: should now have a reservation
        response = serverIn.readLine();
        System.out.println("Server: " + response);

        response = serverIn.readLine();
        System.out.println("Server: " + response);

        response = serverIn.readLine();
        System.out.println("Server: " + response);

        System.out.println("Sending: logout");
        serverOut.println("logout");

        // expected: logout message
        response = serverIn.readLine();
        System.out.println("Server: " + response);

        // expected: nothing more
        response = serverIn.readLine();
        if (response == null) {
            System.out.println("Server: Connection closed (logout successful)");
        } else {
            System.out.println(
                "There was a problem closing the connection to the server."
            );
        }
    }
}
