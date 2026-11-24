/**
 * CS 251: Data Structures and Algorithms
 * Project 2: Part 1
 *
 * @author ez500
 * @username zhan4713
 * @sources none
 */


public class CustomerOrderQueue {
    private CustomerOrder[] orderList;
    private int numOrders;

    /**
     *
     * @return return the priority queue
     *
     */
    public CustomerOrder[] getOrderList() {
        return orderList;
    }

    /**
     *
     * @return return the number of orders
     *
     */
    public int getNumOrders() {
        return this.numOrders;
    }

    /**
     * Constructor of the class.
     * Initialize a new CustomerOrder array with the argument passed.
     *
     */
    public CustomerOrderQueue(int capacity) {
        orderList = new CustomerOrder[capacity];
        numOrders = 0;
    }

    /**
     *
     * @return return the index at which the customer was inserted
     *
     */
    public int insert(CustomerOrder c) {
        if (orderList.length == getNumOrders()) { return -1; }
        numOrders++;
        int last = getNumOrders() - 1;
        try {
            orderList[last] = c;
            c.setPosInQueue(getNumOrders() - 1);
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
    public CustomerOrder delMax() {
        if (isEmpty()) { return null; }
        numOrders--;
        CustomerOrder del = orderList[0];
        del.setPosInQueue(-1);
        orderList[0] = orderList[getNumOrders()];
        orderList[getNumOrders()] = null;
        sinkDown(orderList, 0);
        return del;
    }

    public int swimUp(CustomerOrder[] q, int start) {
        int child = start;
        while (true) {
            int parent = (child - 1) / 2;
            if (q[parent].compareTo(q[child]) == 1 || q[parent].equals(q[child])) {
                return child;
            }
            swap(q, parent, child);
            child = parent;
        }
    }

    public void sinkDown(CustomerOrder[] q, int start) {
        int parent = start;
        while (true) {
            if (parent >= q.length / 2) { return; }
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
     * @return return true if the queue is empty; false else
     *
     */
    public boolean isEmpty() {
        return this.numOrders == 0;
    }

    /**
     *
     * @return return the customer with the maximum priority
     *
     */
    public CustomerOrder getMax() {
        return orderList[0];
    }

}
