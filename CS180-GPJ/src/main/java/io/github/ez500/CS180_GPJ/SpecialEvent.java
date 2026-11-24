package io.github.ez500.CS180_GPJ;

import io.github.ez500.CS180_GPJ.interfaces.Reservable;
import io.github.ez500.CS180_GPJ.interfaces.Reservation;
import io.github.ez500.CS180_GPJ.util.*;
import java.util.*;

/**
 *
 * Represents a timeslot associated to at least one table, requested by a user account.
 *
 * @author Max Koeninger (8bitty), Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 9, 2025
 *
 */

public class SpecialEvent implements Reservation {
    private Timeslot timeslot;
    private final List<String> members;
    private final List<Reservable> reservables;

    public SpecialEvent(
        Timeslot timeslot,
        List<String> members
    ) {
        this.timeslot = timeslot;
        this.members = members;
        this.reservables = new ArrayList<>();
    }

    public SpecialEvent(
        Timeslot timeslot,
        List<String> members,
        List<Reservable> reservables
    ) {
        this.timeslot = timeslot;
        this.members = members;
        this.reservables = reservables;
        for (Reservable r : reservables) {
            r.addReservation(this);
        }
    }

    public Timeslot getTimeslot() {
        return this.timeslot;
    }

    public List<String> getMembers() {
        return this.members;
    }

    public List<Reservable> getReservables() {
        return this.reservables;
    }

    @Override
    public String toData() {
        // Format: timeslot,(member1,member2,...):reservable1:reservable2:...
        return String.format("%s,(%s):%s",
            this.timeslot.toString(),
            String.join(",", this.members),
            String.join(":", this.reservables.stream().map(Reservable::toData).toList())
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

    @Override
    public boolean addReservable(Reservable reservable) {
        return this.reservables.add(reservable);
    }

    @Override
    public boolean removeReservable(Reservable reservable) {
        return this.reservables.remove(reservable);
    }
}
