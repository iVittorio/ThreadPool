package ru.sbt.threadpool;

import ru.sbt.threadpool.fixed.FixedThreadPool;
import ru.sbt.threadpool.scalable.ScalableThreadPool;

/**
 * Created by i.viktor on 02/09/16.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        FixedThreadPool fixedThreadPool = new FixedThreadPool(10);


        for (int i = 0; i < 50; i++) {
            int j = i;
            fixedThreadPool.execute(() -> {
                try {
                    System.out.println("Fixed_Pool: Thread #" + j + " start");
                    Thread.sleep(500);
                    System.out.println("Fixed_Pool: Thread #" + j + " stop");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        fixedThreadPool.start();

        Thread.sleep(5_000);


        ScalableThreadPool scalableThreadPool = new ScalableThreadPool(10, 20);

        for (int i = 0; i < 20; i++) {
            int j = i;
            scalableThreadPool.execute(() -> {
                try {
                    System.out.println("Scalable_Pool: Thread #" + j + " start");
                    Thread.sleep(100);
                    System.out.println("Scalable_Pool: Thread #" + j + " stop");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        scalableThreadPool.start();

        Thread.sleep(3_000);


        for (int i = 21; i < 30; i++) {
            int j = i;
            scalableThreadPool.execute(() -> {
                try {
                    System.out.println("Scalable_Pool: Thread #" + j + " start");
                    Thread.sleep(1000);
                    System.out.println("Scalable_Pool: Thread #" + j + " stop");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        scalableThreadPool.start();


    }
}
