package io.github.ez500.CS180_GPJ;

import io.github.ez500.CS180_GPJ.interfaces.Account;
import io.github.ez500.CS180_GPJ.interfaces.Reservation;

import java.util.*;

/**
 *
 * A storage unit of information for a restaurant's user account.
 *
 * @author Max Koeninger (8bitty), Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 9, 2025
 *
 */

public class User implements Account {
    private boolean admin;
    private String name;
    private String password;
    private final List<Reservation> currentReservations;

    public User(boolean admin, String name, String password) {
        this.admin = admin;
        this.name = name;
        this.password = password;
        this.currentReservations = new ArrayList<>();
    }

    public User(
        boolean admin,
        String name,
        String password,
        List<Reservation> currentReservations
    ) {
        this.admin = admin;
        this.name = name;
        this.password = password;
        this.currentReservations = currentReservations;
    }

    public static boolean validate(String username, String password) {
        return username.indexOf(',') == -1 && username.indexOf('|') == -1 && username.indexOf(':') == -1 &&
                password.indexOf(',') == -1 && password.indexOf('|') == -1 && password.indexOf(':') == -1;
    }

    @Override
    public boolean isAdmin() {
        return admin;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public List<Reservation> getCurrentReservations() {
        return currentReservations;
    }

    @Override
    public String toData() {
        // Format: admin,name,password|reservation1|reservation2:...
        return String.format("%b,%s,%s|%s", this.admin, this.name, this.password,
                String.join("|", this.currentReservations.stream().map(Reservation::toData).toList()));
    }

    @Override
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean addReservation(Reservation reservation) {
        return this.currentReservations.add(reservation);
    }

    @Override
    public boolean removeReservation(Reservation reservation) {
        return this.currentReservations.remove(reservation);
    }
}
