package io.github.ez500.CS180_GPJ;

import io.github.ez500.CS180_GPJ.interfaces.*;
import io.github.ez500.CS180_GPJ.util.Timeslot;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;

import static io.github.ez500.CS180_GPJ.ClientGUI.INPUT_FORMAT;

public class ClientHandler implements Runnable, Server {

    private Socket clientSocket;
    private BufferedReader br;
    private PrintWriter pw;
    private Account loggedInUser;
    private boolean sessionFinished;

    private static final RestaurantDatabase DB =
        RestaurantDatabase.getInstance();

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            this.br = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream())
            );
            this.pw = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.loggedInUser = null;
        this.sessionFinished = false;
    }

    @Override
    public void run() {
        try {
            initialize();
            while (!isFinished()) {
                execute();
            }
            end(false);
        } catch (IOException e) {
            sessionFinished = true;
            e.printStackTrace();
        }
    }

    @Override
    public void initialize() throws IOException {
        // Login
        pw.println("Welcome to the Restaurant Manager System!");
        initialInterface:
        while (true) {
            pw.println("Login, register, leave?");
            String choice = br.readLine();
            if (choice == null) {
                pw.println("Connection closed by client.");
                clientSocket.close();
                return;
            }
            choice = choice.trim().toLowerCase();
            String username;
            String password;
            switch (choice) {
                case "register":
                    pw.println("Please enter your desired username:");
                    username = br.readLine();
                    if (username == null) {
                        sessionFinished = true;
                        return;
                    }
                    pw.println("Please enter your desired password:");
                    password = br.readLine();
                    if (password == null) {
                        sessionFinished = true;
                        return;
                    }

                    if (
                        DB.addAccount(
                            new User(
                                false,
                                username,
                                password,
                                new ArrayList<>()
                            )
                        )
                    ) {
                        pw.println(
                            "Successfully created! You may now log in normally."
                        );
                    } else {
                        pw.println(
                            "Something went wrong with creating your account."
                        );
                    }
                    break;
                case "login":
                    pw.println("Please enter your username:");
                    username = br.readLine();
                    if (username == null) {
                        continue;
                    }
                    pw.println("Please enter your password:");
                    password = br.readLine();
                    if (password == null) {
                        continue;
                    }

                    Account account = DB.authenticate(username, password).orElse(null);
                    if (account != null) {
                        loggedInUser = account;
                        pw.println(
                            "Login successful! Welcome, " + loggedInUser.getName() + "."
                        );
                        return;
                    } else {
                        pw.println("Invalid credentials. Please try again.");
                    }
                    break;
                case "leave", "":
                    pw.println("Goodbye!");
                    sessionFinished = true;
                    return;
                default:
                    System.out.println("Invalid choice.");
                    pw.println(
                            "Invalid choice. Please enter 'login' or 'register'."
                    );
                    break;
            }
        }
    }

    @Override
    public void execute() throws IOException {
        // Reservations
        pw.println(
            "What would you like to do? (view/create/cancel/logout/delete/edit)"
        );
        String action = br.readLine();
        if (action == null) {
            sessionFinished = true;
            return;
        }
        action = action.trim().toLowerCase();
        switch (action) {
            case "view" -> {
                List<Reservation> reservations =
                    loggedInUser.getCurrentReservations();
                if (reservations.isEmpty()) {
                    pw.println("You have no current reservations.");
                } else {
                    pw.println("Your current reservations:");
                    for (Reservation res : reservations) {
                        pw.println(res.toData());
                    }
                    pw.println("End of reservations.");
                }
                String _continueOn = br.readLine();
            }
            case "create" -> {
                pw.println(
                    "Enter reservation details (e.g., datetime,tableId):"   // CHANGED
                );
                String details = br.readLine();
                if (details == null) {
                    sessionFinished = true;
                    return;
                }
                String[] parts = details.split(",");
                if (parts.length != 2) {
                    pw.println("Invalid format. Please try again.");
                    return;
                }
                try {
                    int tableId = Integer.parseInt(parts[1]);
                    Reservable table = DB.getReservableById(tableId)
                        .orElse(null);
                    if (table == null) {
                        pw.println(
                            "No table found with id " + tableId + "."
                        );
                        return;
                    }

                    // create reservation: reservations are always assumed to be one hour
                    String dateTimeString = parts[0];

                    LocalDateTime startDateTime = LocalDateTime.parse(dateTimeString, INPUT_FORMAT);
                    LocalDateTime endDateTime = startDateTime.plusHours(1);

                    Timeslot requestedTimeslot = Timeslot.of(startDateTime, endDateTime);

                    // check for conflicts
                    boolean tableHasConflict = false;
                    for (Reservation tableReservation : table.getReservations()) {
                        if (
                            tableReservation
                                .getTimeslot()
                                .overlapsTimeOnly(requestedTimeslot)
                        ) {
                            tableHasConflict = true;
                            break;
                        }
                    }

                    if (tableHasConflict) {
                        pw.println(
                            "Table is not available for the requested time."
                        );
                        return;
                    }

                    // check for operating hours conflict
                    Timeslot operatingHours =
                        DB.getFacility().getOperatingHours();
                    boolean withinHours = operatingHours.overlapsTimeOnly(
                        requestedTimeslot
                    );
                    if (!withinHours) {
                        pw.println(
                            "Restaurant is not open during the requested time. Operating hours: " +
                                operatingHours
                        );
                        return;
                    }

                    // validate user has not already reserved the table
                    for (Reservation userReservation : loggedInUser.getCurrentReservations()) {
                        if (
                            userReservation
                                .getTimeslot()
                                .overlapsTimeOnly(requestedTimeslot)
                        ) {
                            pw.println(
                                "You already have a reservation for this time period."
                            );
                            return;
                        }
                    }

                    Reservation newRes = new Booking(
                        requestedTimeslot,
                        table,
                        List.of(loggedInUser.getName())
                    );
                    loggedInUser.getCurrentReservations().add(newRes);
                    table.addReservation(newRes);
                    pw.println("Reservation created: " + newRes);
                } catch (NumberFormatException e) {
                    pw.println("Invalid party size. Please enter a number.");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    pw.println("Error creating reservation: " + e.getMessage());
                }
            }
            case "cancel" -> {
                pw.println("Select which reservation to cancel:");
                List<Reservation> userReservations =
                    loggedInUser.getCurrentReservations();
                for (Reservation res : userReservations) {
                    pw.println(
                        (userReservations.indexOf(res) + 1) + res.toData()
                    );
                }
                String details = br.readLine();
                if (details == null) {
                    sessionFinished = true;
                    return;
                }
                switch (details) {
                    case "1", "2", "3", "4", "5", "6", "7", "8", "9" -> {
                        int index = Integer.parseInt(details) - 1;
                        if (index >= userReservations.size()) {
                            pw.println("Invalid selection.");
                            return;
                        }
                        Reservation toCancel = userReservations.get(index);
                        loggedInUser.getCurrentReservations().remove(toCancel);

                        // remove from table's reservation list
                        List<Reservable> reservables =
                            toCancel.getReservables();
                        if (!reservables.isEmpty()) {
                            Reservable table = reservables.get(0);
                            table.removeReservation(toCancel);
                        }

                        pw.println("Reservation canceled: " + toCancel);
                    }
                    default -> pw.println("Invalid selection.");
                }
            }
            case "logout" -> {
                pw.println("Logging out...");
                loggedInUser = null;
                initialize();
            }
            case "delete" -> {
                pw.println(
                    "Are you sure you want to delete your account? (yes/no)"
                );
                String confirm = br.readLine();
                if (confirm == null) {
                    sessionFinished = true;
                    return;
                }
                confirm = confirm.trim().toLowerCase();
                if (confirm.equals("yes")) {
                    if (DB.removeAccount(loggedInUser)) {
                        pw.println("Account deleted. Returning to main menu.");
                        loggedInUser = null;
                        initialize();
                    } else {
                        pw.println("Error deleting account. Please try again.");
                    }
                } else {
                    pw.println("Account deletion canceled.");
                }
            }
            case "edit" -> {
                if (!loggedInUser.isAdmin()) {
                    pw.println("Unauthorized action.");
                    return;
                }
                pw.println("Showing table layout (GUI only)");
            }
            default -> pw.println("Invalid action. Please try again.");
        }
    }

    @Override
    public void end(boolean interrupted) throws IOException {
        // Logout, database save
        if (clientSocket != null && !clientSocket.isClosed()) {
            RestaurantDatabase.resetInstance();
            clientSocket.close();
        }
    }

    @Override
    public void cancel() throws IOException {
        // Interruption/security issues
        this.end(true);
    }

    @Override
    public boolean isFinished() {
        // Universal termination condition if needed
        return sessionFinished;
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(4242)) {
            System.out.println(
                "Restaurant manager server started. Waiting for connection..."
            );

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println(
                    "Client connected: " + clientSocket.getInetAddress()
                );

                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
