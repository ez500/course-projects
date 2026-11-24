package io.github.ez500.CS180_GPJ;

import io.github.ez500.CS180_GPJ.interfaces.Reservable;
import io.github.ez500.CS180_GPJ.interfaces.Reservation;
import io.github.ez500.CS180_GPJ.util.*;
import java.util.*;

/**
 *
 * Represents a table from a restaurant that a user account is able to reserve.
 *
 * @author Max Koeninger (8bitty), Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 9, 2025
 *
 */

public class Table implements Reservable {

    private int id;
    private int size;
    private double price;
    private Layout tableLayout;
    private final List<Reservation> reservations;

    public Table(int id, int size, double price, Layout tableLayout) {
        this.id = id;
        this.size = size;
        this.price = price;
        this.tableLayout = tableLayout;
        this.reservations = new ArrayList<>();
    }

    public Table(
        int id,
        int size,
        double price,
        Layout tableLayout,
        List<Reservation> reservations
    ) {
        this.id = id;
        this.size = size;
        this.price = price;
        this.tableLayout = tableLayout;
        this.reservations = reservations;
    }

    public static Optional<Table> of(String data) {
        // Format: id,size,price,layout
        String[] reservableAttributes = data.split(",");
        if (reservableAttributes.length != 6) {
            return Optional.empty();
        }
        int id = Integer.parseInt(reservableAttributes[0]);
        int size = Integer.parseInt(reservableAttributes[1]);
        double price = Double.parseDouble(reservableAttributes[2]);
        Layout tableLayout = Layout.of(
            String.join(",", Arrays.copyOfRange(reservableAttributes, 3, 6))
        );
        return Optional.of(new Table(id, size, price, tableLayout));
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public double getPrice() {
        return this.price;
    }

    @Override
    public Layout getLayout() {
        return this.tableLayout;
    }

    @Override
    public List<Reservation> getReservations() {
        return this.reservations;
    }

    @Override
    public String toData() {
        // format: id,size,price,layout
        return String.format(
            "%d,%d,%.2f,%s",
            this.id,
            this.size,
            this.price,
            this.tableLayout
        );
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public void setLayout(Layout layout) {
        this.tableLayout = layout;
    }

    @Override
    public boolean addReservation(Reservation reservation) {
        // check if the new reservation's timeslot conflicts with existing ones
        for (Reservation existing : this.reservations) {
            if (
                existing
                    .getTimeslot()
                    .overlapsTimeOnly(reservation.getTimeslot())
            ) {
                return false;
            }
        }
        return this.reservations.add(reservation);
    }

    @Override
    public boolean removeReservation(Reservation reservation) {
        return this.reservations.remove(reservation);
    }
}
