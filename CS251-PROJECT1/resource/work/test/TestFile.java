import org.junit.Test;

public class TestFile {

    @Test
    public void testlol() {
        CyclicDoubleQueue<Integer> queue = new CyclicDoubleQueue<>(15, 3, 0.3);
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                queue.enqueueFront(i);
            } else {
                queue.enqueueBack(i);
            }
            System.out.println(queue.getFront() + " " + queue.getBack() + " " + queue.getSize() + " " + queue.getQueueLength());
            try {
                System.out.println(queue.peekFront() + " " + queue.peekBack());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        while (queue.getSize() > 8) {
            try {
                queue.dequeueBack();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println(queue.getFront() + " " + queue.getBack() + " " + queue.getSize() + " " + queue.getQueueLength());
            try {
                System.out.println(queue.peekFront() + " " + queue.peekBack());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        while (queue.getSize() > 2) {
            try {
                queue.dequeueFront();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println(queue.getFront() + " " + queue.getBack() + " " + queue.getSize() + " " + queue.getQueueLength());
            try {
                System.out.println(queue.peekFront() + " " + queue.peekBack());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        try {
            queue.dequeueBack();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(queue.getFront() + " " + queue.getBack() + " " + queue.getSize() + " " + queue.getQueueLength());
        try {
            System.out.println(queue.peekFront() + " " + queue.peekBack());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
