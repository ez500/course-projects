package io.github.ez500.CS180_GPJ.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * Represents a time period, independent of timezones.
 *
 * @author Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 4, 2025
 *
 */

public class Timeslot implements Pair<LocalDateTime, LocalDateTime> {

    private LocalDateTime start;
    private LocalDateTime end;

    public Timeslot(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public static Timeslot of(LocalDateTime start, LocalDateTime end) {
        return new Timeslot(start, end);
    }

    // use unix epoch (1970-01-01) as default when doing time-only.
    // this is not a meaningful date
    public static Timeslot timeOf(String start, String end) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH.mm");
        LocalTime startTime = LocalTime.parse(start, timeFormatter);
        LocalTime endTime = LocalTime.parse(end, timeFormatter);
        LocalDate epoch = LocalDate.of(1970, 1, 1);
        return of(
            LocalDateTime.of(epoch, startTime),
            LocalDateTime.of(epoch, endTime)
        );
    }

    public static Timeslot timeOf(String data) {
        // format: "HH.mm-HH.mm" or "(HH.mm-HH.mm)"
        String workingData = data;

        // remove parens to make date formatting less error-prone
        if (workingData.startsWith("(") && workingData.endsWith(")")) {
            workingData = workingData.substring(1, workingData.length() - 1);
        }

        String[] parts = workingData.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException(
                "Invalid time range format: " + data
            );
        }
        return timeOf(parts[0], parts[1]);
    }

    public static Timeslot dateTimeOf(String start, String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "yyyy.MM.dd HH.mm"
        );
        return of(
            LocalDateTime.parse(start, formatter),
            LocalDateTime.parse(end, formatter)
        );
    }

    public static Timeslot dateTimeOf(String data) {
        // format: "(yyyy.MM.dd HH.mm-yyyy.MM.dd HH.mm)"
        String[] parts = data.split("-");
        return dateTimeOf(
            parts[0].substring(1),
            parts[1].substring(0, parts[1].length() - 1)
        );
    }

    public LocalDateTime getStart() {
        return getFirst();
    }

    @Override
    public LocalDateTime getFirst() {
        return this.start;
    }

    public LocalDateTime getEnd() {
        return getSecond();
    }

    @Override
    public LocalDateTime getSecond() {
        return this.end;
    }

    public void setStart(LocalDateTime start) {
        setFirst(start);
    }

    @Override
    public void setFirst(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        setSecond(end);
    }

    @Override
    public void setSecond(LocalDateTime end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return String.format(
            "%s-%s",
            this.start.format(DateTimeFormatter.ofPattern("HH.mm")),
            this.end.format(DateTimeFormatter.ofPattern("HH.mm"))
        );
    }

    // useful for checking if a booking time falls within operating hours.
    // returns true if ranges overlap.
    public boolean overlapsTimeOnly(Timeslot other) {
        LocalTime thisStart = this.start.toLocalTime();
        LocalTime thisEnd = this.end.toLocalTime();
        LocalTime otherStart = other.start.toLocalTime();
        LocalTime otherEnd = other.end.toLocalTime();

        return thisStart.isBefore(otherEnd) && otherStart.isBefore(thisEnd);
    }

    // useful for checking if a time is contained within a timeslot.
    // returns true if time is within the timeslot.
    public boolean containsTime(LocalTime time) {
        LocalTime start = this.start.toLocalTime();
        LocalTime end = this.end.toLocalTime();
        return !time.isBefore(start) && !time.isAfter(end);
    }

    public LocalTime getStartTime() {
        return start.toLocalTime();
    }

    public LocalTime getEndTime() {
        return end.toLocalTime();
    }
}
