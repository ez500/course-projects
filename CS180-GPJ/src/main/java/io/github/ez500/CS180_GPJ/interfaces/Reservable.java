package io.github.ez500.CS180_GPJ.interfaces;

import io.github.ez500.CS180_GPJ.util.Layout;

import java.util.*;

/**
 *
 * Represents a generic unit that an account is able to request.
 *
 * @author Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 4, 2025
 *
 */

public interface Reservable {
    int getId();
    int getSize();
    double getPrice();
    Layout getLayout();
    List<Reservation> getReservations();
    String toData();
    void setId(int id);
    void setSize(int size);
    void setPrice(double price);
    void setLayout(Layout layout);
    boolean addReservation(Reservation reservation);
    boolean removeReservation(Reservation reservation);
}
