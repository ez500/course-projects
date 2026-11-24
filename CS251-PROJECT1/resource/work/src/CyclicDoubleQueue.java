/**
 * CS 251: Data Structures and Algorithms
 * Project 1: Part 1
 *
 * @author Eugene Zhang
 * @username zhan4713
 * @sources none
 *
 *
 *
 */

@SuppressWarnings("unchecked")
public class CyclicDoubleQueue<T> {

    // initial capacity of the queue
    private int initialCapacity = 7;

    // queue (array of items)
    private T[] queue;

    // front of the queue
    private int front;

    // back of the queue
    private int back;

    // keeps track of the size;
    private int size;

    // increase factor for resizing the queue
    private int increaseFactor = 2;

    // decrease factor for resizing the queue
    private double decreaseFactor = 0.50;


    /**
     * Constructor of the class.
     * initial capacity = 7;
     *
     */
    @SuppressWarnings("unchecked")
    public CyclicDoubleQueue() {
        this.queue = (T[]) new Object[this.initialCapacity];
        this.front = 0;
        this.back = 0;
        this.size = 0;
    }


    /**
     * Constructor of the class.
     * initial capacity = 7;
     */
    @SuppressWarnings("unchecked")
    public CyclicDoubleQueue(int initialCapacity, int increaseFactor, double decreaseFactor) {
        this.initialCapacity = initialCapacity;
        this.increaseFactor = increaseFactor;
        this.decreaseFactor = decreaseFactor;
        this.queue = (T[]) new Object[this.initialCapacity];
        this.front = 0;
        this.back = 0;
        this.size = 0;
    }


    public int getFront() {
        return this.front;
    }

    public int getBack() {
        return this.back;
    }

    public int getSize() {
        return this.size;
    }

    public int getQueueLength() {
        return this.queue.length;
    }


    /**
     *
     *
     *
     */
    public void enqueueFront(T t) {
        if (isFull()) {
            T[] newQueue = (T[]) new Object[this.queue.length * this.increaseFactor];
            if (this.front < this.back) {
                for (int i = 0; i < this.queue.length; i++) { newQueue[i + 1] = this.queue[i]; }
            } else {
                for (int i = this.front; i < this.queue.length; i++) {
                    int shift = i - this.front + 1;
                    newQueue[shift] = this.queue[i];
                }
                for (int i = 0; i <= this.back; i++) {
                    int shift = i + this.queue.length - this.front + 1;
                    newQueue[shift] = this.queue[i];
                }
            }
            this.queue = newQueue;
            this.front = 1;
            this.back = this.size;
        }
        if (this.size == 0) {
            this.front = 0;
            this.back = 0;
        } else if (this.front == 0) {
            this.front = this.queue.length - 1;
        } else {
            this.front--;
        }
        queue[this.front] = t;
        this.size++;
    }


    /**
     *
     *
     *
     */
    public T dequeueFront() throws Exception {
        if (isEmpty()) { throw new Exception("Queue is empty"); }
        T t = this.queue[this.front];
        this.queue[this.front] = null;
        if (this.front == this.queue.length - 1) {
            this.front = 0;
        } else {
            this.front++;
        }
        this.size--;
        if (this.size == 0) {
            this.front = 0;
            this.back = 0;
        } else if (this.size == 1) {
            this.queue[0] = this.queue[this.front];
            this.queue[this.front] = this.front == 0 ? this.queue[this.front] : null;
            this.front = 0;
            this.back = 0;
        } else if (4 * this.size < this.queue.length) {
            T[] newQueue = (T[]) new Object[(int) (this.queue.length * this.decreaseFactor)];
            if (this.front < this.back) {
                for (int i = this.front; i <= this.back; i++) {
                    int shift = i - this.front;
                    newQueue[shift] = this.queue[i];
                }
            } else {
                replaceCircularQueue(newQueue);
            }
            this.queue = newQueue;
            this.front = 0;
            this.back = this.size - 1;
        }
        return t;
    }


    /**
     *
     *
     *
     */
    public void enqueueBack(T t) {
        if (isFull()) {
            T[] newQueue = (T[]) new Object[this.queue.length * this.increaseFactor];
            if (this.front < this.back) {
                for (int i = 0; i < this.queue.length; i++) { newQueue[i] = this.queue[i]; }
            } else {
                replaceCircularQueue(newQueue);
            }
            this.queue = newQueue;
            this.front = 0;
            this.back = this.size - 1;
        }
        if (this.size == 0) {
            this.front = 0;
            this.back = 0;
        } else if (this.back == this.queue.length - 1) {
            this.back = 0;
        } else {
            this.back++;
        }
        queue[this.back] = t;
        this.size++;
    }

    private void replaceCircularQueue(T[] newQueue) {
        for (int i = this.front; i < this.queue.length; i++) {
            int shift = i - this.front;
            newQueue[shift] = this.queue[i];
        }
        for (int i = 0; i <= this.back; i++) {
            int shift = i + this.queue.length - this.front;
            newQueue[shift] = this.queue[i];
        }
    }


    /**
     *
     *
     *
     */
    public T dequeueBack() throws Exception {
        if (isEmpty()) { throw new Exception("Queue is empty"); }
        T t = this.queue[this.back];
        this.queue[this.back] = null;
        if (this.back == 0) {
            this.back = this.queue.length - 1;
        } else {
            this.back--;
        }
        this.size--;
        if (this.size == 0) {
            this.front = 0;
            this.back = 0;
        } else if (this.size == 1) {
            this.queue[0] = this.queue[this.front];
            this.queue[this.front] = this.front == 0 ? this.queue[this.front] : null;
            this.front = 0;
            this.back = 0;
        } else if (4 * this.size < this.queue.length) {
            T[] newQueue = (T[]) new Object[(int) (this.queue.length * this.decreaseFactor)];
            if (this.front < this.back) {
                for (int i = this.front; i <= this.back; i++) {
                    int shift = i - this.front;
                    newQueue[shift] = this.queue[i];
                }
            } else {
                replaceCircularQueue((T[]) newQueue);
            }
            this.queue = newQueue;
            this.front = 0;
            this.back = this.size - 1;
        }
        return t;
    }


    /**
     *
     *
     *
     */
    public T peekFront() throws Exception {
        if (isEmpty()) { throw new Exception("Queue is empty"); }
        return this.queue[this.front];
    }


    /**
     *
     *
     *
     */
    public T peekBack() throws Exception {
        if (isEmpty()) { throw new Exception("Queue is empty"); }
        return this.queue[this.back];
    }


    /**
     *
     *
     *
     */
    public boolean isEmpty() { return this.size == 0; }

    public boolean isFull() { return this.size == this.queue.length; }

    public String listAll() {
        String all = "";
        if (this.front > this.back) {
            for (int i = this.front; i < this.queue.length; i++) {
                all += this.queue[i];
            }
            for (int i = 0; i <= this.back; i++) {
                all += this.queue[i];
            }
        } else {
            for (int i = this.front; i <= this.back; i++) {
                all += this.queue[i];
            }
        }
        return all;
    }
    

}
