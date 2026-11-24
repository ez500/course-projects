package io.github.ez500.CS180_GPJ;

import io.github.ez500.CS180_GPJ.interfaces.Facility;
import io.github.ez500.CS180_GPJ.interfaces.Reservable;
import io.github.ez500.CS180_GPJ.util.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 *
 * Represents a restaurant containing a group of tables that can be reserved by a user account.
 *
 * @author Max Koeninger (8bitty), Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 9, 2025
 *
 */

public class Restaurant implements Facility {
    private String name;
    private Timeslot operatingHours;
    private final List<Reservable> reservables;

    public Restaurant(String name) {
        this.name = name;
        this.operatingHours = Timeslot.of(
            LocalDateTime.of(LocalDate.MIN, LocalTime.MIDNIGHT),
            LocalDateTime.of(LocalDate.MIN, LocalTime.MIDNIGHT));
        this.reservables = new ArrayList<>();
    }

    public Restaurant(String name, Timeslot operatingHours) {
        this.name = name;
        this.operatingHours = operatingHours;
        this.reservables = new ArrayList<>();
    }

    public Restaurant(String name, Timeslot operatingHours, List<Reservable> reservables) {
        this.name = name;
        this.operatingHours = operatingHours;
        this.reservables = reservables;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Timeslot getOperatingHours() {
        return this.operatingHours;
    }

    @Override
    public List<Reservable> getReservables() {
        return this.reservables;
    }

    @Override
    public String toData() {
        // Format: name,operatingHours:reservable1:reservable2:...
        return String.format("%s,%s:%s", this.name, this.operatingHours.toString(),
                String.join(":", this.reservables.stream().map(Reservable::toData).toList()));
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setOperatingHours(Timeslot operatingHours) {
        this.operatingHours = operatingHours;
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
