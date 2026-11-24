package io.github.ez500.CS180_GPJ;


import io.github.ez500.CS180_GPJ.util.Layout;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * Testing the Table class.
 *
 * @author Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 9, 2025
 *
 */

public class TableTests {
    @Test
    @DisplayName("Table 5,12.50,((3.0,5.0),WEST)")
    void tableDisplay() {
        Table table = new Table(5, 12.50, new Layout(3, 5, "WEST"));
        assertEquals("5,12.50,((3.0,5.0),WEST)", table.toData(),
                "Table string should be 5,12.50,((3.0,5.0),WEST)");
    }

    @ParameterizedTest(name = "Table {3}", quoteTextArguments = false)
    @CsvSource(textBlock = """
            2,  14.79,    '((3,5),WEST)',     '2,14.79,((3.0,5.0),WEST)'
            8,  12.29,  '((12,8),SOUTH)',   '8,12.29,((12.0,8.0),SOUTH)'
            3,  21.99,  '((5,14),NORTH)',   '3,21.99,((5.0,14.0),NORTH)'
            2,  10.50,  '((22,15),EAST)',   '2,10.50,((22.0,15.0),EAST)'
            """)
    void tableStrings(int size, double price, String layoutData, String expectedString) {
        Table table = new Table(size, price, Layout.of(layoutData));
        assertEquals(expectedString, table.toData(), "Table string should be " + expectedString);
    }
}
