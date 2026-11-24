/**
 * CS 251: Data Structures and Algorithms
 * Project 2: Part 3
 *
 * @author ez500
 * @username zhan4713
 * @sources none
 */

import java.security.NoSuchAlgorithmException;


public class BetterCustomerOrderQueue {
    private CustomerOrder[] orderList;
    private CustomerOrderHash table;
    private int numOrders;

    /**
     *
     * Return the CustomerOrderQueue
     *
     */
    public CustomerOrder[] getOrderList() {
        return orderList;
    }

    /**
     *
     * Return the number of orders in the queue
     *
     */
    public int getNumOrders() {
        return numOrders;
    }


    /**
     * Constructor of the class.
     * Initialize a new CustomerOrderQueue and CustomerOrderHash
     *
     */
    public BetterCustomerOrderQueue(int capacity) {
        orderList = new CustomerOrder[capacity];
        table = new CustomerOrderHash(capacity);
        numOrders = 0;
    }

    /**
     *
     * @return return the index at which the customer was inserted;
     * return -1 if the Customer could not be inserted
     *
     */
    public int insert(CustomerOrder c) throws NoSuchAlgorithmException {
        if (orderList.length == getNumOrders()) { return -1; }
        numOrders++;
        int last = numOrders - 1;
        try {
            c.setPosInQueue(getNumOrders() - 1);
            orderList[last] = c;
            table.put(c);
            return swimUp(orderList, last);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     *
     * @return return the customer removed
     *
     */
    public CustomerOrder delMax() throws NoSuchAlgorithmException {
        if (isEmpty()) { return null; }
        numOrders--;
        CustomerOrder del = orderList[0];
        del.setPosInQueue(-1);
        table.remove(del.getName());
        orderList[0] = orderList[numOrders];
        orderList[numOrders] = null;
        if (orderList[0] != null) {
            orderList[0].setPosInQueue(0);
            sinkDown(orderList, 0);
        }
        return del;
    }

    /**
     *
     * @return return the customer with the maximum priority
     *
     */
    public CustomerOrder getMax() {
        return orderList[0];
    }

    /**
     *
     * @return return true if the queue is empty; false else
     *
     */
    public boolean isEmpty() {
        return numOrders == 0;
    }

    /**
     *
     * @return return the number of Customers currently in the queue
     *
     */
    public int size() {
        return numOrders;
    }

    /**
     *
     */
    public CustomerOrder get(String name) throws NoSuchAlgorithmException { return table.get(name); }

    /**
     *
     */
    public CustomerOrder remove(String name) throws NoSuchAlgorithmException {
        CustomerOrder c = table.remove(name);
        if (isEmpty() || c == null) { return null; }
        numOrders--;
        orderList[c.getPosInQueue()] = orderList[numOrders];
        orderList[numOrders] = null;
        if (orderList[c.getPosInQueue()] != null) {
            orderList[c.getPosInQueue()].setPosInQueue(c.getPosInQueue());
            String lastLeaf = orderList[c.getPosInQueue()].getName();
            sinkDown(orderList, c.getPosInQueue());
            swimUp(orderList, table.get(lastLeaf).getPosInQueue());
        }
        c.setPosInQueue(-1);
        return c;
    }

    /**
     *
     *
     */
    public void update(String name, int newTime) throws NoSuchAlgorithmException {
        CustomerOrder c = table.get(name);
        int current = c.getOrderDeliveryTime();
        c.setOrderDeliveryTime(newTime);
        if (current < newTime) { sinkDown(orderList, c.getPosInQueue()); }
        else { swimUp(orderList, c.getPosInQueue()); }
    }

    public int swimUp(CustomerOrder[] q, int start) {
        int child = start;
        while (true) {
            int parent = (child - 1) / 2;
            if (q[parent].compareTo(q[child]) == 1 || child == 0) {
                return child;
            }
            swap(q, parent, child);
            child = parent;
        }
    }

    public void sinkDown(CustomerOrder[] q, int start) {
        int parent = start;
        while (true) {
            if (parent >= numOrders / 2) { return; }
            int leftChild = parent * 2 + 1;
            int rightChild = parent * 2 + 2;
            if (q[rightChild] != null) {
                int earlierChild = q[leftChild].compareTo(q[rightChild]) == 1 ? leftChild : rightChild;
                if (q[parent].compareTo(q[earlierChild]) == -1) {
                    swap(q, parent, earlierChild);
                    parent = earlierChild;
                } else { return; }
            } else {
                if (q[parent].compareTo(q[leftChild]) == -1) {
                    swap(q, parent, leftChild);
                    parent = leftChild;
                } else { return; }
            }
        }
    }

    public void swap(CustomerOrder[] q, int parent, int child) {
        CustomerOrder temp = q[parent];
        q[parent] = q[child];
        q[child] = temp;
        q[parent].setPosInQueue(parent);
        q[child].setPosInQueue(child);
    }
}
