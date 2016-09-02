package ru.sbt.threadpool;

/**
 * Created by i.viktor on 02/09/16.
 */
public interface ThreadPool {
    void execute(Runnable runnable);

    void start();
}
