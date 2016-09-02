package ru.sbt.threadpool.fixed;

import ru.sbt.threadpool.ThreadPool;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by i.viktor on 02/09/16.
 */
public class FixedThreadPool implements ThreadPool {

    private final Queue<Runnable> tasks = new ArrayDeque<>();
    private final Object lock = new Object();
    private final int countThread;


    public FixedThreadPool(int countThread) {
        this.countThread = countThread;
    }

    public void start() {
        for (int i = 0; i < countThread; i++) {
            new Thread(getThread()).start();
        }
    }


    @Override
    public void execute(Runnable task) {
        synchronized (lock) {
            tasks.add(task);
            lock.notify();
        }
    }


    private Runnable getThread() {
        return Worker.newInstance(tasks, lock);
    }

}
