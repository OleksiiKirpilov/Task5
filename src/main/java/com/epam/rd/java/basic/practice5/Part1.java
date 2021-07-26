package com.epam.rd.java.basic.practice5;

import java.util.logging.Logger;

public class Part1 extends Thread {

    public static void worker() {
        for (int i = 0; i < 4; i++) {
            System.out.println(currentThread().getName());
            try {
                sleep(500);
            } catch (InterruptedException e) {
                Logger.getGlobal().severe(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void run() {
        worker();
    }

    private static void processThread(Thread t) {
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            Logger.getGlobal().severe(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        processThread(new Part1());
        processThread(new Thread(Part1::worker));
    }

}
