package io.github.ez500.CS180_GPJ.interfaces;

import java.util.*;

/**
 *
 * A storage of information for a generic entity.
 *
 * @author Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 4, 2025
 *
 */

public interface Account {
    boolean isAdmin();
    String getName();
    String getPassword();
    List<Reservation> getCurrentReservations();
    String toData();
    void setAdmin(boolean admin);
    void setName(String name);
    void setPassword(String password);
    boolean addReservation(Reservation reservation);
    boolean removeReservation(Reservation reservation);
}
