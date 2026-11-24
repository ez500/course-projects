package io.github.ez500.CS180_GPJ;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.ez500.CS180_GPJ.util.Layout;
import io.github.ez500.CS180_GPJ.util.Timeslot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

/**
 *
 * Testing the User class.
 *
 * @author Alex Orr, Eugene Zhang (ez500, zhan4713), L11 T3
 * @version Nov 8, 2025
 *
 */

class UserTests {
    @Test
    @DisplayName("Username is Alex")
    void userNameDisplay() {
        User user = new User(false, "Alex", "password123");
        assertEquals("Alex", user.getName(), "User name should be 'Alex'");
    }

    @ParameterizedTest(name = "Username is '{2}'", quoteTextArguments = false)
    @CsvSource(textBlock = """
			Alex,  password 123,    Alex
			Bob,         qwerty,     Bob
			Michael,     123456, Michael
			Dave,       letmein,    Dave
			""")
    void userName(String inputName, String password, String expectedName) {
        User user = new User(false, inputName, password);
        user.setName(inputName);
        assertEquals(expectedName, user.getName(), "User name should be '" + expectedName + "'");
    }

    @Test
    @DisplayName("User is admin")
    void userAdminStatus() {
        User user = new User(false, "Alex", "password123");
        user.setAdmin(true);
        assertTrue(user.isAdmin(), "User should be admin after setAdmin(true)");
    }

    @ParameterizedTest(name = "User admin status is {1}", quoteTextArguments = false)
    @CsvSource(textBlock = """
			true,   true
			false, false
			""")
    void userSetAdmin(boolean setAdmin, boolean expectedAdmin) {
        User user = new User(false, "Alex", "password123");
        user.setAdmin(setAdmin);
        assertEquals(expectedAdmin, user.isAdmin(), "User admin status should be " + expectedAdmin);
    }

    @Test
    @DisplayName("User data")
    void userTestData() {
        Table table1 = new Table(4, 25.00, new Layout(4, 4, "SOUTH"));
        Table table2 = new Table(6, 32.50, new Layout(6, 5, "EAST"));
        Table table3 = new Table(2, 15.00, new Layout(2, 3, "NORTH"));
        Table table4 = new Table(3, 26.30, new Layout(8, 4, "NORTH"));

        Booking res = new Booking(Timeslot.timeOf("10.00", "11.00"), table2, List.of(
            "Mona Lisa", "Just Lisa", "Leonardo DiCaprio", "AdminUser"
        ));
        SpecialEvent tennisParty = new SpecialEvent(Timeslot.timeOf("18.00", "20.00"), List.of(
            "Roger Federer", "Carlos Alcaraz", "The Tennis Fanatic", "AdminUser"
        ), List.of(table1, table3, table4));
        User user = new User(true, "AdminUser", "securePass", List.of(res, tennisParty));
        String expectedData = "true,AdminUser,securePass|(10.00-11.00),(Mona Lisa,Just Lisa,Leonardo DiCaprio," +
                "AdminUser):6,32.50,((6.0,5.0),EAST)|(18.00-20.00),(Roger Federer,Carlos Alcaraz,The Tennis Fanatic," +
                "AdminUser):4,25.00,((4.0,4.0),SOUTH):2,15.00,((2.0,3.0),NORTH):3,26.30,((8.0,4.0),NORTH)";
        assertEquals(expectedData, user.toData(), "User data string should be " + expectedData);
    }
}

