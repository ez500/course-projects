package io.github.ez500.CS180_GPJ;

import io.github.ez500.CS180_GPJ.interfaces.Reservable;
import io.github.ez500.CS180_GPJ.interfaces.Reservation;
import io.github.ez500.CS180_GPJ.util.Layout;
import io.github.ez500.CS180_GPJ.util.Timeslot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

public class ClientGUI extends JFrame implements Runnable {
    private Socket socket;
    private BufferedReader serverIn;
    private PrintWriter serverOut;

    private static final RestaurantDatabase DB = RestaurantDatabase.getInstance();

    // general window layout
    private final JPanel cardPanel;
    private final CardLayout cardLayout;

    // Card names
    private static final String CARD_SELECT_OPTION = "SELECT_OPTION";
    private static final String CARD_LOGIN = "LOGIN";
    private static final String CARD_DASHBOARD = "DASHBOARD";
    private static final String CARD_VIEW_RES = "VIEW_RES";
    private static final String CARD_CREATE_RES = "CREATE_RES";

    private String loginPanelSubmissionMode = "login";

    // widgets whose content we update from server
    private JPanel reservationsListPanel;

    // layout helpers
    private final Map<Rectangle, Table> tableHitAreas = new HashMap<>();
    private static final int TABLE_WIDTH = 40;
    private static final int TABLE_HEIGHT = 40;
    private static final int GRID_SCALE = 50; // pixels per coordinate unit
    private static final int GRID_OFFSET_X = 50;
    private static final int GRID_OFFSET_Y = 50;

    public static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm");

    public ClientGUI(String serverHost, int serverPort) {
        setTitle("Restaurant Reservation Client");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(cardPanel, BorderLayout.CENTER);

        try {
            this.socket = new Socket(serverHost, serverPort);
            this.serverIn = new BufferedReader(
                    new InputStreamReader(this.socket.getInputStream()));
            this.serverOut = new PrintWriter(
                    new OutputStreamWriter(this.socket.getOutputStream()), true);

            buildPanels();

            JOptionPane.showMessageDialog(
                    this,
                    "Connected to server " + serverHost + ":" + serverPort,
                    "Connection",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not connect to server: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException ignored) {
            }
            // close window
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ClientGUI("localhost", 4242));
    }

    @Override
    public void run() {
        if (socket == null || socket.isClosed()) {
            return;
        }
        setVisible(true);
        cardLayout.show(cardPanel, CARD_SELECT_OPTION);

        Thread reader = new Thread(() -> {
            try {
                String line;
                List<String> reservationLines = null;   // NEW

                while ((line = serverIn.readLine()) != null) {
                    if (line.equals("Your current reservations:")) {
                        reservationLines = new ArrayList<>();
                        SwingUtilities.invokeLater(() -> {
                            cardLayout.show(cardPanel, CARD_VIEW_RES);
                        });
                        continue;
                    }

                    if (reservationLines != null) {
                        if (line.equals("End of reservations.")) {
                            List<String> finalLines = new ArrayList<>(reservationLines);
                            reservationLines = null;
                            SwingUtilities.invokeLater(() -> renderReservations(finalLines));
                        } else {
                            reservationLines.add(line);
                        }
                        continue;
                    }

                    String finalLine = line;
                    SwingUtilities.invokeLater(() -> handleServerMessage(finalLine));
                }
            } catch (IOException e) {
                closeConnection();
            }
        });
        reader.start();
    }

    private void renderReservations(List<String> reservationLines) {
        if (reservationsListPanel == null) {
            return;
        }
        reservationsListPanel.removeAll();

        if (reservationLines.isEmpty()) {
            JLabel empty = new JLabel("You have no current reservations.");
            empty.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            reservationsListPanel.add(empty);
        } else {
            for (String line : reservationLines) {
                JPanel card = createReservationCard(line);
                reservationsListPanel.add(card);
            }
        }

        reservationsListPanel.revalidate();
        reservationsListPanel.repaint();
    }

    // Build one reservation "card" panel from a single toData() line
    private JPanel createReservationCard(String data) {
        JPanel card = new JPanel();
        System.out.println(data);

        // Expected format from Booking/SpecialEvent:
        // timeslot,(member1,member2,...):reservableData[ :reservableData2... ]
        String[] parts = data.split(":");
        String[] subParts = parts[0].split(",");
        String timeslotPart = subParts[0];
        String membersPart = subParts[1];
        System.out.println(membersPart);
        String[] reservableParts = Arrays.copyOfRange(parts, 1, parts.length);
        String reservableIDs = String.join(", ",
            Arrays.stream(reservableParts)
                .map(rData -> {
                    String[] rAttributes = rData.split(",");
                    return rAttributes[0]; // reservable ID is first attribute
                })
                .toList()
        );
        String reservableSizes = String.join(", ",
            Arrays.stream(reservableParts)
                .map(rData -> {
                    String[] rAttributes = rData.split(",");
                    return rAttributes[1]; // reservable size is second attribute
                })
                .toList()
        );
        String reservablePrices = String.join(", ",
            Arrays.stream(reservableParts)
                .map(rData -> {
                    String[] rAttributes = rData.split(",");
                    return rAttributes[2]; // reservable price is third attribute
                })
                .toList()
        );

        JLabel timeLabel = new JLabel("Time: " + timeslotPart + " ");
        timeLabel.setFont(timeLabel.getFont().deriveFont(Font.BOLD));
        card.add(timeLabel);

        String membersPretty = membersPart.startsWith("(") && membersPart.endsWith(")")
            ? membersPart.substring(1, membersPart.length() - 1)
            : membersPart;
        card.add(new JLabel("Members: " + membersPretty + " "));
        card.add(new JLabel("Table ID(s): " + reservableIDs + " "));
        card.add(new JLabel("Table Size(s): " + reservableSizes + " "));
        card.add(new JLabel("Price(s): " + reservablePrices + " "));


        return card;
    }

    // connection cleanup helper
    private void closeConnection() {
        try {
            if (serverOut != null) {
                serverOut.flush();
                serverOut.close();
            }
            if (serverIn != null) {
                serverIn.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
    }

    // server message handling (unchanged except for layout strings)
    private void handleServerMessage(String msg) {
        if (msg.startsWith("Login, register, leave?")) {
            cardLayout.show(cardPanel, CARD_SELECT_OPTION);
        } else if (msg.startsWith("Successfully created! You may now log in normally.")) {
            JOptionPane.showMessageDialog(
                    this,
                    msg,
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE
            );
            cardLayout.show(cardPanel, CARD_SELECT_OPTION);
        } else if (msg.startsWith("Something went wrong with creating your account.")) {
            JOptionPane.showMessageDialog(
                    this,
                    msg,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            cardLayout.show(cardPanel, CARD_SELECT_OPTION);
        } else if (msg.startsWith("Invalid credentials. Please try again.")) {
            JOptionPane.showMessageDialog(
                    this,
                    msg,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            cardLayout.show(cardPanel, CARD_LOGIN);
        } else if (msg.startsWith("What would you like to do? (view/create/cancel/logout/delete/edit)")) {
            cardLayout.show(cardPanel, CARD_DASHBOARD);
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    msg,
                    "Server",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    // Panel builders
    private void buildPanels() {
        buildSelectOptionPanel();
        buildLoginPanel();
        buildDashboardPanel();
        buildViewReservationPanel();
        buildCreateReservationPanel();
    }

    private void buildSelectOptionPanel() {
        JPanel selectOptionPanel = new JPanel(new FlowLayout());
        selectOptionPanel.add(new JLabel("Login, register, or leave?"));

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        loginBtn.addActionListener(e -> {
            loginPanelSubmissionMode = "login";
            cardLayout.show(cardPanel, CARD_LOGIN);
        });

        registerBtn.addActionListener(e -> {
            loginPanelSubmissionMode = "register";
            cardLayout.show(cardPanel, CARD_LOGIN);
        });

        selectOptionPanel.add(loginBtn);
        selectOptionPanel.add(registerBtn);

        cardPanel.add(selectOptionPanel, CARD_SELECT_OPTION);
    }

    private void buildLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        JLabel userLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(15);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);
        JButton submitBtn = new JButton("Submit");
        JButton cancelBtn = new JButton("Cancel");

        submitBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            serverOut.println(loginPanelSubmissionMode);
            serverOut.println(username);
            serverOut.println(password);
            usernameField.setText("");
            passwordField.setText("");
        });

        cancelBtn.addActionListener(e ->
                cardLayout.show(cardPanel, CARD_SELECT_OPTION)
        );

        loginPanel.add(userLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passLabel);
        loginPanel.add(passwordField);
        loginPanel.add(submitBtn);
        loginPanel.add(cancelBtn);

        cardPanel.add(loginPanel, CARD_LOGIN);
    }

    private void buildDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new GridLayout(0, 1));

        JButton viewBtn = new JButton("View Reservations");
        JButton createBtn = new JButton("Create Reservation (Table View)");
        JButton cancelBtn = new JButton("Cancel Reservation");
        JButton logoutBtn = new JButton("Logout");
        JButton deleteBtn = new JButton("Delete Account");
        JButton editBtn = new JButton("Edit (Admin)");

        viewBtn.addActionListener(e -> {
            serverOut.println("view");
            cardLayout.show(cardPanel, CARD_VIEW_RES);
        });

        // go to "create" flow using table layout
        createBtn.addActionListener(e -> {
            serverOut.println("create");
            cardLayout.show(cardPanel, CARD_CREATE_RES);
        });

        cancelBtn.addActionListener(e -> {
            String choice = JOptionPane.showInputDialog(
                    this,
                    "Enter reservation index to cancel (1..n):",
                    "Cancel Reservation",
                    JOptionPane.QUESTION_MESSAGE
            );
            if (choice != null && !choice.isBlank()) {
                serverOut.println("cancel");
                serverOut.println(choice.trim());
            }
        });

        logoutBtn.addActionListener(e -> {
            serverOut.println("logout");
        });

        deleteBtn.addActionListener(e -> {
            serverOut.println("delete");
            String[] options = { "yes", "no" };
            String choice = (String) JOptionPane.showInputDialog(
                null,
                "Are you sure you want to delete your account?",
                "ClientGUI",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                null
            );
            serverOut.println(choice);
        });

        editBtn.addActionListener(e -> {
            serverOut.println("edit");
        });

        dashboardPanel.add(viewBtn);
        dashboardPanel.add(createBtn);
        dashboardPanel.add(cancelBtn);
        dashboardPanel.add(logoutBtn);
        dashboardPanel.add(deleteBtn);
        dashboardPanel.add(editBtn);

        cardPanel.add(dashboardPanel, CARD_DASHBOARD);
    }

    private void buildViewReservationPanel() {
        JPanel viewReservationPanel = new JPanel(new BorderLayout());
        reservationsListPanel = new JPanel();
        reservationsListPanel.setLayout(new BoxLayout(reservationsListPanel, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(reservationsListPanel);

        JButton backBtn = new JButton("Back");

        backBtn.addActionListener(e -> {
            // clear list when leaving, optional
            reservationsListPanel.removeAll();
            reservationsListPanel.revalidate();
            reservationsListPanel.repaint();
            cardLayout.show(cardPanel, CARD_DASHBOARD);
        });

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.add(backBtn);

        viewReservationPanel.add(scroll, BorderLayout.CENTER);
        viewReservationPanel.add(southPanel, BorderLayout.SOUTH);

        cardPanel.add(viewReservationPanel, CARD_VIEW_RES);
    }

    // Table layout grid and click handling
    private void buildCreateReservationPanel() {
        JPanel tableLayoutPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                tableHitAreas.clear();

                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                List<Reservable> reservables = DB.getFacility().getReservables();
                List<Table> tables = reservables.stream()
                        .filter(r -> r instanceof Table)
                        .map(r -> (Table) r)
                        .toList();

                for (Table table : tables) {
                    Layout layout = table.getLayout();
                    double xUnit = layout.getOrigin().x();
                    double yUnit = layout.getOrigin().y();

                    int x = GRID_OFFSET_X + (int) (xUnit * GRID_SCALE);
                    int y = GRID_OFFSET_Y + (int) (yUnit * GRID_SCALE);

                    boolean available = hasAnyAvailability(table);

                    if (available) {
                        g2d.setColor(Color.GREEN);
                    } else {
                        g2d.setColor(Color.RED);
                    }
                    g2d.fillRect(x, y, TABLE_WIDTH, TABLE_HEIGHT);

                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x, y, TABLE_WIDTH, TABLE_HEIGHT);
                    g2d.drawString(String.valueOf(table.getId()),
                            x + 10, y + 23);

                    Rectangle hit = new Rectangle(x, y, TABLE_WIDTH, TABLE_HEIGHT);
                    tableHitAreas.put(hit, table);
                }
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(600, 400);
            }
        };

        tableLayoutPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                for (Map.Entry<Rectangle, Table> entry : tableHitAreas.entrySet()) {
                    if (entry.getKey().contains(p)) {
                        Table table = entry.getValue();
                        if (!hasAnyAvailability(table)) {
                            JOptionPane.showMessageDialog(
                                    ClientGUI.this,
                                    "This table has no available time within operating hours.",
                                    "Unavailable",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                            return;
                        }
                        showTableReservationDialog(table);
                        break;
                    }
                }
            }
        });

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> {
            serverOut.println("");
            cardLayout.show(cardPanel, CARD_DASHBOARD);
        }
        );

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(tableLayoutPanel, BorderLayout.CENTER);
        wrapper.add(backBtn, BorderLayout.SOUTH);

        cardPanel.add(wrapper, CARD_CREATE_RES);
    }

    // Check if table has any free interval within facility operating hours
    private boolean hasAnyAvailability(Table table) {
        Timeslot operating = DB.getFacility().getOperatingHours();
        List<Reservation> reservations =
                table.getReservations();

        // crude heuristic: if there exists at least one 1-hour slot in operating hours
        // that does not overlap any reservation, return true.
        LocalDateTime start = operating.getStart();
        LocalDateTime end = operating.getEnd();

        LocalDate date = start.toLocalDate();
        for (int hour = start.getHour(); hour < end.getHour(); hour++) {
            LocalDateTime slotStart = LocalDateTime.of(date, start.toLocalTime().withHour(hour));
            LocalDateTime slotEnd = slotStart.plusHours(1);
            Timeslot probe = Timeslot.of(slotStart, slotEnd);

            boolean conflict = false;
            for (Reservation r : reservations) {
                if (r.getTimeslot().overlapsTimeOnly(probe)) {
                    conflict = true;
                    break;
                }
            }
            if (!conflict) {
                return true;
            }
        }
        return false;
    }

    // Dialog to choose start/end for a specific table and send "create" to server
    private void showTableReservationDialog(Table table) {
        JDialog dialog = new JDialog(this, "Create Reservation", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;

        dialog.add(new JLabel("Table size: " + table.getSize()), gbc);
        gbc.gridy++;
        dialog.add(new JLabel(String.format("Price: $%.2f", table.getPrice())), gbc);

        gbc.gridy++;
        dialog.add(new JLabel("Start (yyyy-MM-dd HH.mm):"), gbc);
        gbc.gridx = 1;
        JTextField startField = new JTextField(16);
        dialog.add(startField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        dialog.add(new JLabel("End (yyyy-MM-dd HH.mm):"), gbc);
        gbc.gridx = 1;
        JTextField endField = new JTextField(16);
        dialog.add(endField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttons = new JPanel(new FlowLayout());
        JButton submitBtn = new JButton("Submit");
        JButton cancelBtn = new JButton("Cancel");
        buttons.add(submitBtn);
        buttons.add(cancelBtn);
        dialog.add(buttons, gbc);

        submitBtn.addActionListener(e -> {
            String startTimeText = startField.getText().trim();
            try {
                int id = table.getId();

                String details = startTimeText + "," + id;
                serverOut.println(details);

                dialog.dispose();
                cardLayout.show(cardPanel, CARD_DASHBOARD);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Invalid datetime format. Use yyyy-MM-dd HH.mm",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}