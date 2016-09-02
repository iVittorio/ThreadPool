package ru.sbt.threadpool.scalable;

import ru.sbt.threadpool.ThreadPool;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by i.viktor on 02/09/16.
 */
public class ScalableThreadPool implements ThreadPool {

    private final Queue<Runnable> tasks = new ArrayDeque<>();
    private final Object lock = new Object();
    private final List<Thread> threadList = new ArrayList<>();
    private final int minThread;
    private final int maxThread;
    private int countThread = 0;

    public ScalableThreadPool(int minThread, int maxThread) {
        this.minThread = minThread;
        this.maxThread = maxThread;
    }

    public void start() {
        synchronized (lock) {
            for (int i = 0; i < minThread; i++) {
                startThread();
            }
        }
    }

    private void startThread() {
        Thread thread = new Worker();
        thread.start();
        countThread++;
        threadList.add(thread);
    }


    @Override
    public void execute(Runnable task) {
        synchronized (lock) {
            tasks.add(task);
            lock.notify();
            if (countThread >= minThread && countThread < maxThread) startThread();
        }
    }

    public class Worker extends Thread {

        @Override
        public void run() {
            while (true) {
                Runnable task;
                synchronized (lock) {
                    if (tasks.size() == 0 && threadList.size() > minThread) {
                        threadList.remove(Thread.currentThread());
                        break;
                    }
                    while (tasks.size() == 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException("Thread: " + Thread.currentThread().getName() + " Interrupted");
                        }
                    }
                    task = tasks.poll();
                    countThread++;
                }
                try {
                    task.run();
                } catch (Exception e) {
                    throw new RuntimeException("Exception in run method", e);
                } finally {
                    synchronized (lock) {
                        countThread--;
                    }
                }
            }
        }
    }
}
