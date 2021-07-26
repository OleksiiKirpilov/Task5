package com.epam.rd.java.basic.practice5;

import java.util.logging.Logger;

public class Part3 {

    private int counter;
    private int counter2;

    private boolean sync = false;

    private final Thread[] threads;

    public Part3(int numberOfThreads, int numberOfIterations) {
        threads = new Thread[numberOfThreads];
        createThreads(numberOfThreads, numberOfIterations);
    }

    private void work() {
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void createThreads(int numberOfThreads, int numberOfIterations) {
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread() {
                @Override
                public void run() {
                    for (int count = 0; count < numberOfIterations; count++) {
                        long t1 = System.nanoTime() / 1_000_000;
                        if (sync) {
                            compareSync();
                        } else {
                            compare();
                        }
                        counter++;
                        long t2 = System.nanoTime() / 1_000_000;
                        try {
                            sleep(100 - (t2 - t1));
                        } catch (InterruptedException e) {
                            Logger.getGlobal().severe(e.getMessage());
                            interrupt();
                        }
                        counter2++;
                    }
                }
            };
        }
    }

    public static void main(final String[] args) {
        Part3 p = new Part3(2, 10);
        p.work();
        System.out.println();
        p = new Part3(2, 10);
        p.sync = true;
        p.work();
    }

    public void compare() {
        System.out.printf("counter %s counter2%n", (counter == counter2) ? "==" : "!=");
    }

    public void compareSync() {
        boolean eq;
        synchronized (Part3.class) {
            eq = (counter == counter2);
        }
        System.out.printf("counter %s counter2%n", eq ? "==" : "!=");

    }

}
