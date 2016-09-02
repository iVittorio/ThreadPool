package ru.sbt.threadpool.fixed;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by i.viktor on 02/09/16.
 */
public class Worker implements Runnable {
    private final Queue<Runnable> tasks;
    private final Object lock;

    private Worker(Queue<Runnable> tasks, Object lock) {
        this.tasks = tasks;
        this.lock = lock;
    }

    public static Runnable newInstance(Queue<Runnable> tasks, Object lock) {
        return new Worker(tasks, lock);
    }


    @Override
    public void run() {
        while (true) {
            synchronized (lock) {
                while (tasks.size() == 0) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException("Thread: " + Thread.currentThread().getName() + " Interrupted");
                    }
                }
            }
            Runnable task = tasks.poll();
            try {
                task.run();
            } catch (Exception e) {
                throw new RuntimeException("Exception in run method", e);
            }
        }
    }
}
