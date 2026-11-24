package io.github.ez500.CS180_GPJ.interfaces;

import java.util.*;

/**
 *
 * Represents a database that reads/writes to a file
 * of facilities, and a file of user accounts.
 *
 * @author Max Koeninger (8bitty), Eugene Zhang (ez500, zhan4713), L11 T3
 * @version November 10th, 2025
 *
 */

public interface Database {
    Facility getFacility();
    List<Account> getAccounts();
    Optional<Account> getAccount(String name);
    Optional<Account> authenticate(String username, String password);
    void setFacility(Facility facility);
    boolean addAccount(Account account);
    boolean removeAccount(Account account);
}
