package com.epam.rd.java.basic.practice5;

import java.util.logging.Logger;

public class Part3 {

    private int counter;
    private int counter2;

    private final int numberOfThreads;
    private final int numberOfIterations;

    private boolean sync = false;

    private Thread[] threads;

    public Part3(int numberOfThreads, int numberOfIterations) {
        this.numberOfThreads = numberOfThreads;
        this.numberOfIterations = numberOfIterations;
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
            if (sync) {
                threads[i] = new Thread(() -> worker(numberOfIterations));
            } else {
                threads[i] = new Thread(() -> workerSync(numberOfIterations));
            }
        }
    }

    private void worker(int numberOfIterations) {
        for (int count = 0; count < numberOfIterations; count++) {
            if (sync) {
                countersCompareSync();
            } else {
                countersCompare();
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

    private synchronized void workerSync(int numberOfIterations) {
        worker(numberOfIterations);
    }

    public static void main(final String[] args) {
        Part3 p = new Part3(2, 10);
        p.compareSync();
        p.compare();
    }

    public void compare() {
        threads = new Thread[numberOfThreads];
        createThreads(numberOfThreads, numberOfIterations);
        work();
    }

    public void compareSync() {
        this.sync = true;
        compare();
    }

    private void countersCompare() {
        System.out.printf("%d %d %s %d%n",
                System.nanoTime()/1000000, counter, (counter == counter2) ? "==" : "!=", counter2);
    }

    private void countersCompareSync() {
        boolean eq;
        synchronized (this) {
            eq = (counter == counter2);
            System.out.printf("%d %d %s %d%n",
                    System.nanoTime()/1000000, counter, eq ? "==" : "!=", counter2);
        }
    }

}
