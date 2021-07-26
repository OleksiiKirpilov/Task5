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
        work();
    }

    public Part3(int numberOfThreads, int numberOfIterations, boolean sync) {
        this(numberOfThreads, numberOfIterations);
        this.sync = sync;
    }

    private void work() {
        // start all threads and join them
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Logger.getGlobal().severe(e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void createThreads(int numberOfThreads, int numberOfIterations) {
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread(() -> worker(numberOfIterations));
        }
    }

    private void worker(int numberOfIterations) {
        for (int count = 0; count < numberOfIterations; count++) {
            if (sync) {
                compareSync();
            } else {
                compare();
            }
            counter++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Logger.getGlobal().severe(e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
            counter2++;
        }
    }

    public static void main(final String[] args) {
        Part3 p = new Part3(2, 10);
        System.err.println(p);
        p = new Part3(2, 10, true);
        System.err.println(p);
    }

    public void compare() {
        System.out.printf("%d %s %d%n", counter, (counter == counter2) ? "==" : "!=", counter2);
    }

    public void compareSync() {
        boolean eq;
        synchronized (Part3.class) {
            eq = (counter == counter2);
            System.out.printf("%d %s %d%n", counter, eq ? "==" : "!=", counter2);
        }

    }

}
