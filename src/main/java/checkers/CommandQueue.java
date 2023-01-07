package checkers;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CommandQueue {
    private Queue<String> queue = new ArrayDeque<>();
    private Lock lock = new ReentrantLock();
    private Condition condition = this.lock.newCondition();

    public void push(final String command) {
        lock.lock();
        try {
            queue.offer(command);
        } finally {
            lock.unlock();
        }
    }

    public String pop() {
        lock.lock();
        try {
            return queue.poll();
        } finally {
            lock.unlock();
        }
    }

    public void await() {
        while(!poll()) {
            // If await keeps throwing exceptions, this becomes a busy spinlock.
            try {
                condition.await();
            } catch(Exception e) {
            }
        }
    }

    public boolean poll() {
        lock.lock();
        try {
            return queue.peek() != null;
        } finally {
            lock.unlock();
        }
    }

    public void signal() {
        lock.lock();
        try {
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}
