package io.github.ez500.CS180_GPJ;


import io.github.ez500.CS180_GPJ.util.Layout;
import io.github.ez500.CS180_GPJ.util.Timeslot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * Testing the SpecialEvent class.
 *
 * @author Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 9, 2025
 *
 */

public class SpecialEventTests {
    @Test
    @DisplayName("SpecialEvent (18.00-20.00),()|6,25.0,((4,4),SOUTH)")
    void specialEventDisplay() {
        Timeslot timeslot = Timeslot.timeOf("18.00", "20.00");
        Table table1 = new Table(4, 25.00, new Layout(4, 4, "SOUTH"));
        Table table2 = new Table(5, 25.50, new Layout(6, 4, "SOUTH"));
        SpecialEvent specialEvent = new SpecialEvent(timeslot, List.of(
            "Birthday Boy", "MrClean", "Party Pooper", "DJ Spinmaster", "Mom", "Dad", "Uncle Joe", "John Doe", "Sally"
        ), List.of(table1, table2));
        String expected = "(18.00-20.00),(Birthday Boy,MrClean,Party Pooper,DJ Spinmaster,Mom,Dad,Uncle Joe,John " +
                "Doe,Sally):4,25.00,((4.0,4.0),SOUTH):5,25.50,((6.0,4.0),SOUTH)";
        assertEquals(expected, specialEvent.toData(), "SpecialEvent string should be " + expected);
    }
}
