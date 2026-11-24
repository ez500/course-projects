package io.github.ez500.CS180_GPJ.interfaces;

import io.github.ez500.CS180_GPJ.util.Timeslot;

import java.util.*;

/**
 *
 * Represents a timeslot associated to at least one reservable, requested by an account.
 *
 * @author Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 4, 2025
 *
 */

public interface Reservation {
    Timeslot getTimeslot();
    List<String> getMembers();
    List<Reservable> getReservables();
    String toData();
    void setTimeslot(Timeslot timeslot);
    boolean addMember(String member);
    boolean removeMember(String member);
    boolean addReservable(Reservable reservable);
    boolean removeReservable(Reservable reservable);
}
