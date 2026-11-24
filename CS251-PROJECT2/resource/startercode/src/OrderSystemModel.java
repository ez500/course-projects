/**
 * CS 251: Data Structures and Algorithms
 * Project 2: Part 4
 *
 * TODO: Complete OrderSystemModel.
 *
 * @author Chirayu Garg, TODO: add your name here
 * @username garg104, TODO: add your Purdue username here
 * @sources TODO: list your sources here
 */

import java.security.NoSuchAlgorithmException;

public class OrderSystemModel {
    private BetterCustomerOrderQueue orderList;
    private int capacityThreshold;
    private int ordersDelayed;
    private int ordersOnTime;
    private int ordersCanceled;
    private int time;
    private int totalDelayTime;

    public int getOrdersDelayed() {
        return ordersDelayed;
    }

    public int getOrdersOnTime() {
        return ordersOnTime;
    }

    public int getOrdersCanceled() {
        return ordersCanceled;
    }

    public int getTotalDelayTime() {
        return totalDelayTime;
    }

    public BetterCustomerOrderQueue getOrderList() {
        return orderList;
    }

    /**
     * Constructor of the class.
     *
     * Initialize a new OrderSystemModel and OrderSystemModel
     *
     */
    public OrderSystemModel(int capacityThreshold) {
        this.capacityThreshold = capacityThreshold;
        this.orderList = new BetterCustomerOrderQueue(this.capacityThreshold);
        this.ordersDelayed = 0;
        this.ordersOnTime = 0;
        this.ordersCanceled = 0;
        this.time = 0;
        this.totalDelayTime = 0;
    }


    /**
     *
     *
     */
    public String process(String name, int orderTime, int deliveryTime) throws NoSuchAlgorithmException {
        CustomerOrder customer = new CustomerOrder(name, orderTime, deliveryTime);
        int queue = orderList.insert(customer);
        if (queue == -1) {
            if (customer.compareTo(orderList.getMax()) == 1) {
                if (time > customer.getOrderDeliveryTime()) { ordersDelayed++; } else { ordersOnTime++; }
                time++;
                return null;
            } else {
                completeNextOrder();
                orderList.insert(customer);
            }
        }
        return name;
    }

    /**
     *
     *
     */
    public String completeNextOrder() throws NoSuchAlgorithmException {
        if (orderList.isEmpty()) { return null; }
        else {
            CustomerOrder earliest = orderList.delMax();
            if (time > earliest.getOrderDeliveryTime()) { ordersDelayed++; } else { ordersOnTime++; }
            time++;
            return earliest.getName();
        }
    }

    /**
     *
     *
     */
    public String updateOrderTime(String name, int newDeliveryTime) throws NoSuchAlgorithmException {
        CustomerOrder customer = orderList.get(name);
        if (customer == null) { return null; }
        if (newDeliveryTime < orderList.getMax().getOrderDeliveryTime()) {
            if (newDeliveryTime < time) {
                cancelOrder(name);
            } else {
                orderList.update(name, newDeliveryTime);
                completeNextOrder();
            }
            return name;
        }
        else {
            orderList.update(name, newDeliveryTime);
        }
        return null;
    }

    /**
     *
     */
    public CustomerOrder cancelOrder(String name) throws NoSuchAlgorithmException {
        CustomerOrder canceled = orderList.remove(name);
        if (canceled == null) { return null; }
        ordersCanceled++;
        return canceled;
    }
}
