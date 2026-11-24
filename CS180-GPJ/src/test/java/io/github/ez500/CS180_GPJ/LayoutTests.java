package io.github.ez500.CS180_GPJ;


import io.github.ez500.CS180_GPJ.util.Layout;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * Testing the Layout class.
 *
 * @author Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 9, 2025
 *
 */

public class LayoutTests {
    @Test
    @DisplayName("Layout ((3.0,5.0),WEST)")
    void layoutDisplay() {
        Layout layout = new Layout(3, 5, "WEST");
        assertEquals("((3.0,5.0),WEST)", layout.toString(),
                "Layout string should be ((3.0,5.0),WEST)");
    }

    @ParameterizedTest(name = "Layout '{1}'", quoteTextArguments = false)
    @CsvSource(textBlock = """
			1,  5,  NORTH,   '((1.0,5.0),NORTH)'
			2,  5,  SOUTH,   '((2.0,5.0),SOUTH)'
			5,  5,   EAST,    '((5.0,5.0),EAST)'
			10, 5,   WEST,   '((10.0,5.0),WEST)'
			""")
    void layoutStrings(int x, int y, String direction, String expectedString) {
        Layout layout = new Layout(x, y, direction);
        assertEquals(expectedString, layout.toString(), "Layout string should be " + expectedString);
    }
}
