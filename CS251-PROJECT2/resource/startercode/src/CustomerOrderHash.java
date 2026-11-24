/**
 * CS 251: Data Structures and Algorithms
 * Project 2: Part 2
 *
 * @author ez500
 * @username zhan4713
 * @sources none
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class CustomerOrderHash {
    private ArrayList<CustomerOrder>[] table;
    private int numOrders;
    private int tableCapacity;

    /**
     * Constructor of the class.
     * Initialize a new CustomerOrder array with the argument passed.
     *
     */
    @SuppressWarnings("unchecked")
    public CustomerOrderHash(int capacity){
       table = new ArrayList[getNextPrime(capacity)];
       for (int i = 0; i < table.length; i++) { table[i] = new ArrayList<>(); }
       numOrders = 0;
       tableCapacity = getNextPrime(capacity);
    }


    /**
     *
     */
    public CustomerOrder get(String name) throws NoSuchAlgorithmException {
        MessageDigest hashString = MessageDigest.getInstance("SHA-256");
        String hash = new String(hashString.digest(name.getBytes()));
        int index = hash.hashCode() % tableCapacity;
        index = index < 0 ? index + tableCapacity : index;
        for (CustomerOrder o : table[index]) {
            if (o.getName().equals(name)) {
                return o;
            }
        }
        return null;
    }

    /**
     *
     */
    public void put(CustomerOrder c) throws NoSuchAlgorithmException {
        MessageDigest hashString = MessageDigest.getInstance("SHA-256");
        String hash = new String(hashString.digest(c.getName().getBytes()));
        int index = hash.hashCode() % tableCapacity;
        index = index < 0 ? index + tableCapacity : index;
        if (table[index].contains(c)) { return; }
        table[index].add(c);
        numOrders++;
    }

    /**
     *
     */
    public CustomerOrder remove(String name) throws NoSuchAlgorithmException {
        MessageDigest hashString = MessageDigest.getInstance("SHA-256");
        String hash = new String(hashString.digest(name.getBytes()));
        int index = hash.hashCode() % tableCapacity;
        index = index < 0 ? index + tableCapacity : index;
        for (CustomerOrder o : table[index]) {
            if (o.getName().equals(name)) {
                table[index].remove(o);
                numOrders--;
                return o;
            }
        }
        return null;
    }

    /**
     *
     *
     */
    public int size() { return numOrders; }

    //get the next prime number p >= num
    private int getNextPrime(int num) {
        if (num == 2 || num == 3)
            return num;

        int rem = num % 6;

        switch (rem) {
            case 0:
            case 4:
                num++;
                break;
            case 2:
                num += 3;
                break;
            case 3:
                num += 2;
                break;
        }

        while (!isPrime(num)) {
            if (num % 6 == 5) {
                num += 2;
            } else {
                num += 4;
            }
        }

        return num;
    }

    //determines if a number > 3 is prime
    private boolean isPrime(int num) {
        if (num % 2 == 0) {
            return false;
        }

        int x = 3;

        for (int i = x; i < num; i += 2) {
            if (num % i == 0) {
                return false;
            }
        }

        return true;
    }

    public ArrayList<CustomerOrder>[] getArray() {
        return this.table;
    }

}
