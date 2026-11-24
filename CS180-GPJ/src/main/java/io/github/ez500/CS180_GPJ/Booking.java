package io.github.ez500.CS180_GPJ;

import io.github.ez500.CS180_GPJ.interfaces.Reservable;
import io.github.ez500.CS180_GPJ.interfaces.Reservation;
import io.github.ez500.CS180_GPJ.util.*;
import java.util.*;

/**
 *
 * Represents a timeslot associated to exactly one table, requested by a user account.
 *
 * @author Max Koeninger (8bitty), Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 9, 2025
 *
 */

public class Booking implements Reservation {
    private Timeslot timeslot;
    private Reservable reservable;
    private final List<String> members;

    public Booking(
        Timeslot timeslot,
        Reservable reservable,
        List<String> members
    ) {
        this.timeslot = timeslot;
        this.reservable = reservable;
        reservable.addReservation(this);
        this.members = members;
    }

    @Override
    public Timeslot getTimeslot() {
        return this.timeslot;
    }

    @Override
    public List<String> getMembers() {
        return this.members;
    }

    @Override
    public List<Reservable> getReservables() {
        return List.of(this.reservable);
    }

    @Override
    public String toData() {
        // Format: timeslot,(member1,member2,...):reservableData
        return String.format("%s,(%s):%s",
            this.timeslot.toString(),
            String.join(",", this.members),
            this.reservable.toData()
        );
    }

    @Override
    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    @Override
    public boolean addMember(String member) {
        return this.members.add(member);
    }

    @Override
    public boolean removeMember(String member) {
        return this.members.remove(member);
    }

    public void setReservable(Reservable reservable) {
        this.reservable = reservable;
    }

    @Override
    public boolean addReservable(Reservable reservable) {
        return false;
    }

    @Override
    public boolean removeReservable(Reservable reservable) {
        return false;
    }
}
