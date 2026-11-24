package io.github.ez500.CS180_GPJ;


import io.github.ez500.CS180_GPJ.util.Coordinate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * Testing the Coordinate class.
 *
 * @author Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 9, 2025
 *
 */

public class CoordinateTests {
    @Test
    @DisplayName("Coordinate (2,5)")
    void coordinateDisplay() {
        Coordinate coordinate = new Coordinate(2, 5);
        assertEquals(2, coordinate.x(), "X coordinate should be 2");
        assertEquals(5, coordinate.y(), "Y coordinate should be 5");
    }

    @ParameterizedTest(name = "Coordinate '({0}, {1})'", quoteTextArguments = false)
    @CsvSource(textBlock = """
			1,  5,  1, 5
			2,  5,  2, 5
			5,  5,  5, 5
			10, 5, 10, 5
			""")
    void coordinateValues(int x, int y, int expectedX, int expectedY) {
        Coordinate coordinate = new Coordinate(x, y);
        assertEquals(expectedX, coordinate.x(), "X coordinate should be " + x);
        assertEquals(expectedY, coordinate.y(), "Y coordinate should be " + y);
    }
}
