# CS180-GPJ
Team Project - Reservation Manager

#### Manual Testing with Telnet
(Starting from project root)
1. Start the server: `./gradlew run`
2. Connect with telnet in a new terminal window: `telnet localhost 4242`
3. Test the following commands:
   - `register` - Create new account
   - `login` - Login to existing account  
   - `view` - View current reservations
   - `create` - Create new reservation (format: date,time,partySize)
   - `cancel` - Cancel existing reservation
   - `logout` - Return to login screen
   - `delete` - Delete account
   - `leave` - Exit the system
4. Done!

#### Automated Client Testing
(Starting from project root)
1. Use the AutoTestClient class for easy testing: `./gradlew runAutoTestClient`
2. Done!

NOTE: Max Koeninger submitted Phase 1 on Vocareum

## Project Structure
- `interfaces` - Java interfaces for the project
  - `Account` - Contains methods for generic storage of information within an entity
  - `Database` - Contains methods for generic database operations i.e., deleting a user entry from the database
  - `Facility` - Represents a generic physical organization containing a group of requestable units by an account
  - `Reservable` - Represents a generic unit that an account is able to request
  - `Reservation` - Represents a generic request made by an account for a reservable unit within a facility with an association to a timeslot
- `util` - Utility classes for convenient data handling
  - `Coordinate` - Represents an ordered pair on the 2D Cartesian plane
  - `Layout` - Represents a layout of an object (e.g., table) as mapped onto a chart (i.e., a coordinate for its origin and a direction in which the object is facing)
  - `Pair` - A generic storage type of two references
  - `TimeSlot` - Represents a time period, independent of timezones
- `Booking` - Represents a timeslot associated to exactly one table, requested by a user account. Contains a `Timeslot`, a `Reservable` (`Table`), and a list of members (of type `String`) who each may or may not have an associated account
  - `BookingTests` (within the `test` directory) - Unit tests for the `Booking` class, verifying correct functionality of its methods
- `Restaurant` - Represents a facility containing multiple reservable tables. Contains a name, location (of type `Coordinate`), and a list of `Table` objects
  - `RestaurantTests` - Unit tests for the `Restaurant` class, verifying correct functionality of its methods
- `RestaurantDatabase` - Handles reading from and writing to a file that store user information and a file that stores facility information, containing methods to update database and import data into the object types specified here
- `SpecialEvent` - Represents a timeslot associated to at least one table, requested by a user account. Effectively a `Booking` but with a list of `Reservable`s (`Table`s) instead of just one.
  - `SpecialEventTests` - Unit tests for the `SpecialEvent` class, verifying correct functionality of its methods
- `Table` - Represents a table from a restaurant that a user account is able to reserve, containing size, price, layout (of type `Layout`), and a list of reservations (of type `Booking` and `SpecialEvent`) that the table contains
  - `TableTests` - Unit tests for the `Table` class, verifying correct functionality of its methods
- `User` - A storage unit of information for a restaurant's user account, containing a name, password, admin boolean, and a list of reservations (of type `Booking` and `SpecialEvent`) that the user is involved in
  - `UserTests` - `UserTest` - Unit tests for the `User` class, verifying correct functionality of its methods
