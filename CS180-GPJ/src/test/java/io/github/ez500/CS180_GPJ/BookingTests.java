package io.github.ez500.CS180_GPJ;


import io.github.ez500.CS180_GPJ.util.Layout;
import io.github.ez500.CS180_GPJ.util.Timeslot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * Testing the Booking class.
 *
 * @author Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 9, 2025
 *
 */

public class BookingTests {
    @Test
    @DisplayName("Booking (09.00-10.00),():4,15.0,((2,3),NORTH)")
    void bookingDisplay() {
        Timeslot timeslot = Timeslot.timeOf("09.00", "10.00");
        Table table = new Table(4, 15.00, new Layout(2, 3, "NORTH"));
        Booking booking = new Booking(timeslot, table, List.of(
            "John Doe", "Jane Smith"
        ));
        assertEquals("(09.00-10.00),(John Doe,Jane Smith):4,15.00,((2.0,3.0),NORTH)", booking.toData(),
                "Booking string should be (09.00-10.00),(John Doe,Jane Smith):4,15.00,((2.0,3.0),NORTH)");
    }

    @ParameterizedTest(name = "Booking {3}", quoteTextArguments = false)
    @CsvSource(textBlock = """
            08.00, 09.00,     '4,15.00,((12,15),WEST)',   'All Alone',  '(08.00-09.00),(All Alone):4,15.00,((12.0,15.0),WEST)'
            22.00, 00.00,     '2,20.00,((5,7.5),EAST)',   'The Grinch;McNasty',    '(22.00-00.00),(The Grinch,McNasty):2,20.00,((5.0,7.5),EAST)'
            12.15, 13.35, '6,18.79,((10.2,8.5),SOUTH)',   'Eugene Zhang;Paul Rickert;Asvin Sivathanu;Alex Orr',  '(12.15-13.35),(Eugene Zhang,Paul Rickert,Asvin Sivathanu,Alex Orr):6,18.79,((10.2,8.5),SOUTH)'
            16.15, 19.45, '8,12.50,((17.3,15.9),WEST)',   'John Doe;Jane Smith;The Grinch;McNasty;Eugene Zhang;Paul Rickert;Asvin Sivathanu;Alex Orr',  '(16.15-19.45),(John Doe,Jane Smith,The Grinch,McNasty,Eugene Zhang,Paul Rickert,Asvin Sivathanu,Alex Orr):8,12.50,((17.3,15.9),WEST)'
            """)
    void bookingStrings(String startTime, String endTime, String tableData, String memberString, String expectedString) {
        String[] members = memberString.split(";");
        Booking booking = new Booking(Timeslot.timeOf(startTime, endTime), Table.of(tableData).get(), List.of(members));
        assertEquals(expectedString, booking.toData(), "Booking string should be " + expectedString);
    }
}
