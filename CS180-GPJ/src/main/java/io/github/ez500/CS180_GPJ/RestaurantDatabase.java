package io.github.ez500.CS180_GPJ;

import io.github.ez500.CS180_GPJ.interfaces.*;
import io.github.ez500.CS180_GPJ.util.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 *
 * Handles reading from and writing to a file that store user information
 * (accountFile) and a file that stores facility information (facilityFile).
 *
 * @author Max Koeninger (8bitty), Eugene Zhang (ez500, zhan4713), L11 T3
 * @version November 9th, 2025
 *
 */

public class RestaurantDatabase implements Database {

    private final String accountFile;
    // accountFile format:
    // admin1,name1,password1|reservation1|reservation2|...
    // reservation format: timeslot,(member1,member2,...):reservable1:rerservable2:...
    // reservable format: id,size,price,layout
    // One account per line

    private final String facilityFile;
    // facilityFile format:
    // name1,operatingHours1:reservable1:reservable2:...
    // reservable format: id,size,price,layout
    // One reservable per line

    private Facility facility;
    private final List<Account> accounts;

    private static RestaurantDatabase db;

    public static RestaurantDatabase getInstance() {
        if (db == null) {
            db = new RestaurantDatabase(
                "src/main/resources/account_file.txt",
                "src/main/resources/facility_file.txt"
            );
            db.importData();
        }
        return db;
    }

    public static void resetInstance() {
        db.saveAll();
        db = null;
    }

    public RestaurantDatabase(String accountFile, String facilityFile) {
        this.accountFile = accountFile;
        this.facilityFile = facilityFile;
        this.facility = null;
        this.accounts = new ArrayList<>();
    }

    @Override
    public Facility getFacility() {
        return this.facility;
    }

    public Optional<Reservable> getReservableByPartySize(int partySize) {
        return this.facility.getReservables()
            .stream()
            .filter(reservable -> reservable.getSize() >= partySize)
            .min(Comparator.comparingInt(Reservable::getSize));
    }

    public Optional<Reservable> getReservableById(int id) {
        return this.facility.getReservables()
            .stream()
            .filter(reservable -> reservable.getId() == id)
            .findFirst();
    }

    @Override
    public List<Account> getAccounts() {
        return this.accounts;
    }

    @Override
    public Optional<Account> getAccount(String name) {
        return this.accounts.stream()
            .filter(account -> account.getName().equals(name))
            .findFirst();
    }

    @Override
    public Optional<Account> authenticate(String username, String password) {
        Optional<Account> accountOpt = this.getAccount(username);
        return accountOpt.isPresent() &&
            accountOpt.get().getPassword().equals(password)
            ? accountOpt
            : Optional.empty();
    }

    @Override
    public void setFacility(Facility facility) {
        this.facility = facility;
        saveAll();
    }

    @Override
    public boolean addAccount(Account account) {
        if (
            this.getAccount(account.getName()).isEmpty() &&
            User.validate(account.getName(), account.getPassword())
        ) {
            this.accounts.add(account);
            saveAll();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAccount(Account account) {
        if (this.accounts.remove(account)) {
            saveAll();
            return true;
        }
        return false;
    }

    private void importData() {
        // Read facility and account file lines
        importFacility();
        importAccounts();
        saveAll();
    }

    private void importFacility() {
        try {
            List<String> facilityLines = Files.readAllLines(
                Paths.get(this.facilityFile)
            );
            if (facilityLines.isEmpty()) {
                return;
            }

            String facilityData = facilityLines.getFirst();
            String[] facilityParts = facilityData.split(":");
            if (facilityParts.length < 1) {
                return;
            }
            String[] facilityAttributes = facilityParts[0].split(",");
            if (facilityAttributes.length != 2) {
                return;
            }

            String name = facilityAttributes[0];
            String operatingHoursData = facilityAttributes[1];
            Timeslot operatingHours = Timeslot.timeOf(operatingHoursData);
            List<Reservable> facilityReservables = Arrays.stream(
                Arrays.copyOfRange(facilityParts, 1, facilityParts.length)
            ).collect(
                ArrayList::new,
                (list, reservableData) ->
                    Table.of(reservableData).ifPresent(list::add),
                ArrayList::addAll
            );

            this.facility = new Restaurant(
                name,
                operatingHours,
                facilityReservables
            );
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (this.facility == null) {
                System.out.println(
                    "No facility found! Creating default facility \"My Restaurant\" with operating hours 09:00-21:00."
                );
                this.facility = new Restaurant(
                    "My Restaurant",
                    Timeslot.timeOf("09:00", "21:00"),
                    new ArrayList<>()
                );
            }
        }
    }

    private void importAccounts() {
        try {
            List<String> accountLines = Files.readAllLines(
                Paths.get(this.accountFile)
            );
            if (accountLines.isEmpty()) {
                return;
            }

            for (String accountData : accountLines) {
                String[] accountParts = accountData.split("\\|");
                if (accountParts.length < 1) {
                    continue;
                }

                String[] accountAttributes = accountParts[0].split(",");
                if (accountAttributes.length != 3) {
                    continue;
                }
                boolean isAdmin = Boolean.parseBoolean(accountAttributes[0]);
                String accountName = accountAttributes[1];
                String accountPassword = accountAttributes[2];
                List<Reservation> reservations = Arrays.stream(
                    Arrays.copyOfRange(accountParts, 1, accountParts.length)
                ).collect(
                    ArrayList::new,
                    this::importReservation,
                    ArrayList::addAll
                );

                this.accounts.add(
                    new User(
                        isAdmin,
                        accountName,
                        accountPassword,
                        reservations
                    )
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (this.accounts.isEmpty()) {
                System.out.println(
                    "No accounts found! Creating default admin account with username 'admin' and password 'admin'."
                );
                this.accounts.add(
                    new User(true, "admin", "admin", new ArrayList<>())
                );
            }
        }
    }

    private void importReservation(
        List<Reservation> reservations,
        String reservationData
    ) {
        String[] reservationParts = reservationData.split(":");
        if (reservationParts.length < 2) {
            return;
        }

        String[] reservationAttributes = reservationParts[0].split("\\(");

        Timeslot timeslot =
                Timeslot.timeOf(reservationAttributes[0].substring(0, reservationAttributes[0].length() - 1));
        String membersData = reservationAttributes[1].substring(0, reservationAttributes[1].length() - 1);
        List<String> members = Arrays.asList(
            membersData.split(",")
        );

        List<Reservable> reservedReservables = Arrays.stream(
            Arrays.copyOfRange(reservationParts, 1, reservationParts.length)
        ).collect(
            ArrayList::new,
            (list, reservableData) ->
                Table.of(reservableData).ifPresent(list::add),
            ArrayList::addAll
        );

        reservations.add(
            reservedReservables.size() > 1
                ? new SpecialEvent(timeslot, members, reservedReservables)
                : new Booking(timeslot, reservedReservables.getFirst(), members)
        );
    }

    private void saveAll() {
        try (
            BufferedWriter fbw = new BufferedWriter(
                new FileWriter(facilityFile, false)
            );
            BufferedWriter abw = new BufferedWriter(
                new FileWriter(accountFile, false)
            )
        ) {
            fbw.write(facility.toData());

            for (Account account : accounts) {
                abw.write(account.toData());
                abw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(
            "legacy testing method, see README for new testing instructions"
        );
        /*
        Scanner in = new Scanner(System.in);
        RestaurantDatabase db = new RestaurantDatabase(
            "src/main/resources/account_file.txt",
            "src/main/resources/facility_file.txt"
        );
        db.importData();

        String username;

        while (true) {
            System.out.println(
                "Enter your username and password, one line at a time, to log in, or type 'NEW' to create a new user:"
            );
            String input = in.nextLine();

            if (input.equals("NEW")) {
                System.out.println("Please enter a username:");
                String newUsername = in.nextLine();
                System.out.println("Please enter a password:");
                String newPassword = in.nextLine();

                if (
                    db.addAccount(
                        new User(
                            false,
                            newUsername,
                            newPassword,
                            new ArrayList<>()
                        )
                    )
                ) {
                    System.out.println(
                        "Successfully created! You may now log in normally."
                    );
                    db.saveAll();
                } else {
                    System.out.println(
                        "Something went wrong with creating your account."
                    );
                }
            } else {
                String password = in.nextLine();
                if (db.authenticate(input, password).isPresent()) {
                    username = input;
                    System.out.println("Welcome, " + username + "!");
                    break;
                }
                System.out.println("Invalid username or password!");
            }
        }

        boolean exit = false;
        do {
            System.out.println(
                """
                What do you want to do?
                1: Create new reservation
                2: View available seats
                3: Cancel reservation
                4: Delete account
                5: Exit"""
            );
            int choice = in.nextInt();
            in.nextLine();
            switch (choice) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    if (
                        db.getAccount(username).isPresent() &&
                        db.removeAccount(db.getAccount(username).get())
                    ) {
                        db.saveAll();
                        exit = true;
                        System.out.println("Bye!");
                        break;
                    }
                    break;
                case 5:
                    System.out.println("Bye!");
                    exit = true;
                    break;
            }
        } while (!exit);

        db.saveAll();
        */
    }
}
