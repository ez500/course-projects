package io.github.ez500.CS180_GPJ;


import io.github.ez500.CS180_GPJ.util.Layout;
import io.github.ez500.CS180_GPJ.util.Timeslot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * Testing the Restaurant class.
 *
 * @author Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 9, 2025
 *
 */

public class RestaurantTests {
    @Test
    @DisplayName("Restaurant 'Gourmet Haven'")
    void restaurantDisplay() {
        Table table1 = new Table(4, 25.00, new Layout(4, 4, "SOUTH"));
        Table table2 = new Table(5, 25.50, new Layout(6, 4, "SOUTH"));
        Table table3 = new Table(2, 15.00, new Layout(2, 3, "NORTH"));
        Table table4 = new Table(8, 30.00, new Layout(10, 5, "EAST"));
        Restaurant restaurant = new Restaurant("Gourmet Haven", Timeslot.timeOf("11.00", "22.00"),
            List.of(table1, table2, table3, table4));
        assertEquals("Gourmet Haven", restaurant.getName(),
            "Restaurant name should be 'Gourmet Haven'");
        String expectedValue = "Gourmet Haven,(11.00-22.00):4,25.00,((4.0,4.0),SOUTH):5,25.50,((6.0,4.0),SOUTH):" +
                "2,15.00,((2.0,3.0),NORTH):8,30.00,((10.0,5.0),EAST)";
        assertEquals(expectedValue, restaurant.toData(), "Restaurant string should be " + expectedValue);
    }
}
