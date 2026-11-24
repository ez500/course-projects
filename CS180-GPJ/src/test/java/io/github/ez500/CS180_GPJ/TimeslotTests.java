package io.github.ez500.CS180_GPJ;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.ez500.CS180_GPJ.util.Timeslot;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 *
 * Testing the Timeslot class.
 *
 * @author Eugene Zhang (ez500, zhan4713), Max Koeninger (8bitty) L11 T3
 * @version Nov 9, 2025
 *
 */

public class TimeslotTests {

    @Test
    @DisplayName("Timeslot (09.00-10.00)")
    void timeslotDisplay() {
        Timeslot timeslot = Timeslot.timeOf("09.00", "10.00");
        assertEquals(
            "(09.00-10.00)",
            timeslot.toString(),
            "Timeslot string should be (09:00-10:00)"
        );
    }

    @ParameterizedTest(name = "Timeslot '{0}-{1}'", quoteTextArguments = false)
    @CsvSource(
        textBlock = """
        08.00, 09.00,   '(08.00-09.00)'
        10.00, 11.00,   '(10.00-11.00)'
        12.00, 13.00,   '(12.00-13.00)'
        14.00, 15.00,   '(14.00-15.00)'
        """
    )
    void timeslotStrings(
        String startTime,
        String endTime,
        String expectedString
    ) {
        Timeslot timeslot = Timeslot.timeOf(startTime, endTime);
        assertEquals(
            expectedString,
            timeslot.toString(),
            "Timeslot string should be " + expectedString
        );
    }

    @Test
    @DisplayName("Timeslot time-only utility methods")
    void timeslotTimeOnlyMethods() {
        Timeslot operatingHours = Timeslot.timeOf("09.00", "17.00");
        Timeslot bookingTime = Timeslot.timeOf("12.00", "13.00");
        Timeslot outsideHours = Timeslot.timeOf("18.00", "20.00");

        assertEquals(
            9,
            operatingHours.getStartTime().getHour(),
            "Start time should be 09:00"
        );
        assertEquals(
            0,
            operatingHours.getStartTime().getMinute(),
            "Start time should be 09:00"
        );
        assertEquals(
            17,
            operatingHours.getEndTime().getHour(),
            "End time should be 17:00"
        );
        assertEquals(
            0,
            operatingHours.getEndTime().getMinute(),
            "End time should be 17:00"
        );

        assertTrue(
            operatingHours.containsTime(LocalTime.of(12, 0)),
            "Should contain 12:00"
        );
        assertTrue(
            operatingHours.containsTime(LocalTime.of(9, 0)),
            "Should contain start time 09:00"
        );
        assertTrue(
            operatingHours.containsTime(LocalTime.of(17, 0)),
            "Should contain end time 17:00"
        );
        assertFalse(
            operatingHours.containsTime(LocalTime.of(8, 59)),
            "Should not contain 08:59"
        );
        assertFalse(
            operatingHours.containsTime(LocalTime.of(17, 1)),
            "Should not contain 17:01"
        );

        assertTrue(
            operatingHours.overlapsTimeOnly(bookingTime),
            "Should overlap with booking time"
        );
        assertFalse(
            operatingHours.overlapsTimeOnly(outsideHours),
            "Should not overlap with outside hours"
        );

        Timeslot adjacentStart = Timeslot.timeOf("17.00", "18.00");
        Timeslot adjacentEnd = Timeslot.timeOf("08.00", "09.00");
        assertTrue(
            operatingHours.overlapsTimeOnly(adjacentStart),
            "Should overlap at start boundary"
        );
        assertTrue(
            operatingHours.overlapsTimeOnly(adjacentEnd),
            "Should overlap at end boundary"
        );
    }

    @Test
    @DisplayName("Timeslot time-only methods with different dates")
    void timeslotDifferentDates() {
        Timeslot today = Timeslot.dateTimeOf(
            "2025.11.15 09.00",
            "2025.11.15 17.00"
        );
        Timeslot tomorrow = Timeslot.dateTimeOf(
            "2025.11.16 09.00",
            "2025.11.16 17.00"
        );
        Timeslot epochBased = Timeslot.timeOf("09.00", "17.00");

        assertEquals(
            today.getStartTime(),
            tomorrow.getStartTime(),
            "Start times should be equal"
        );
        assertEquals(
            today.getEndTime(),
            tomorrow.getEndTime(),
            "End times should be equal"
        );
        assertEquals(
            today.getStartTime(),
            epochBased.getStartTime(),
            "Start times should be equal"
        );
        assertEquals(
            today.getEndTime(),
            epochBased.getEndTime(),
            "End times should be equal"
        );

        assertTrue(
            today.overlapsTimeOnly(epochBased),
            "Should overlap time-only regardless of date"
        );
        assertTrue(
            tomorrow.overlapsTimeOnly(epochBased),
            "Should overlap time-only regardless of date"
        );
        assertTrue(
            today.containsTime(LocalTime.of(12, 0)),
            "Should contain time regardless of date"
        );
        assertTrue(
            tomorrow.containsTime(LocalTime.of(12, 0)),
            "Should contain time regardless of date"
        );
    }

    @Test
    @DisplayName("Timeslot containsTime edge cases")
    void timeslotContainsTimeEdgeCases() {
        Timeslot shortSlot = Timeslot.timeOf("12.30", "13.30");

        assertTrue(
            shortSlot.containsTime(LocalTime.of(12, 30)),
            "Should contain start time"
        );
        assertTrue(
            shortSlot.containsTime(LocalTime.of(13, 30)),
            "Should contain end time"
        );
        assertTrue(
            shortSlot.containsTime(LocalTime.of(13, 0)),
            "Should contain middle time"
        );
        assertFalse(
            shortSlot.containsTime(LocalTime.of(12, 29)),
            "Should not contain time before start"
        );
        assertFalse(
            shortSlot.containsTime(LocalTime.of(13, 31)),
            "Should not contain time after end"
        );

        Timeslot fractionalSlot = Timeslot.timeOf("14.15", "15.45");
        assertTrue(
            fractionalSlot.containsTime(LocalTime.of(14, 15)),
            "Should contain 14:15"
        );
        assertTrue(
            fractionalSlot.containsTime(LocalTime.of(15, 45)),
            "Should contain 15:45"
        );
        assertTrue(
            fractionalSlot.containsTime(LocalTime.of(15, 0)),
            "Should contain 15:00"
        );
        assertFalse(
            fractionalSlot.containsTime(LocalTime.of(14, 14)),
            "Should not contain 14:14"
        );
        assertFalse(
            fractionalSlot.containsTime(LocalTime.of(15, 46)),
            "Should not contain 15:46"
        );
    }

    @Test
    @DisplayName("Timeslot overlapsTimeOnly edge cases")
    void timeslotOverlapsTimeOnlyEdgeCases() {
        Timeslot mainSlot = Timeslot.timeOf("10.00", "14.00");

        Timeslot completeOverlap = Timeslot.timeOf("11.00", "13.00");
        assertTrue(
            mainSlot.overlapsTimeOnly(completeOverlap),
            "Should overlap with completely contained slot"
        );

        Timeslot partialStart = Timeslot.timeOf("09.00", "11.00");
        assertTrue(
            mainSlot.overlapsTimeOnly(partialStart),
            "Should overlap with partially overlapping start"
        );

        Timeslot partialEnd = Timeslot.timeOf("13.00", "15.00");
        assertTrue(
            mainSlot.overlapsTimeOnly(partialEnd),
            "Should overlap with partially overlapping end"
        );

        Timeslot before = Timeslot.timeOf("08.00", "09.30");
        assertFalse(
            mainSlot.overlapsTimeOnly(before),
            "Should not overlap with slot before"
        );

        Timeslot after = Timeslot.timeOf("15.00", "16.00");
        assertFalse(
            mainSlot.overlapsTimeOnly(after),
            "Should not overlap with slot after"
        );

        Timeslot exactStart = Timeslot.timeOf("14.00", "15.00");
        Timeslot exactEnd = Timeslot.timeOf("09.00", "10.00");
        assertTrue(
            mainSlot.overlapsTimeOnly(exactStart),
            "Should overlap at exact start boundary"
        );
        assertTrue(
            mainSlot.overlapsTimeOnly(exactEnd),
            "Should overlap at exact end boundary"
        );
    }
}
