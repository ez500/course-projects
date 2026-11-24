package io.github.ez500.CS180_GPJ.interfaces;

import io.github.ez500.CS180_GPJ.util.Timeslot;

import java.util.*;

/**
 *
 * Represents a generic physical organization containing a group of units that can be requested by an account.
 *
 * @author Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 4, 2025
 *
 */

public interface Facility {
    String getName();
    Timeslot getOperatingHours();
    List<Reservable> getReservables();
    String toData();
    void setName(String name);
    void setOperatingHours(Timeslot timeslot);
    boolean addReservable(Reservable reservable);
    boolean removeReservable(Reservable reservable);
}
