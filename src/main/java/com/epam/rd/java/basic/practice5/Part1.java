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
            }
        }
    }

    @Override
    public void run() {
        worker();
    }

    public static void main(String[] args) {
        Thread t1 = new Part1();
        Thread t2 = new Thread(Part1::worker);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            Logger.getGlobal().severe(e.getMessage());
        }
        t2.start();
        try {
            t2.join();
        } catch (InterruptedException e) {
            Logger.getGlobal().severe(e.getMessage());
        }
    }

}
