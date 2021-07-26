package com.epam.rd.java.basic.practice5;

import java.util.Scanner;
import java.util.logging.Logger;

public class Spam {

    private final Thread[] threads;

    public Spam(final String[] messages, final int[] delays) {
        int num = messages.length;
        threads = new Worker[num];
        for (int i = 0; i < num; i++) {
            threads[i] = new Worker(messages[i], delays[i]);
        }
    }

    public static void main(final String[] args) {
        Spam spam = new Spam(new String[]{"@@@", "bbbbbbb"}, new int[]{500, 333});
        spam.start();
        Scanner in = new Scanner(System.in);
        while (!in.hasNextLine() || !in.nextLine().isEmpty()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Logger.getGlobal().severe(e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }
        spam.stop();
    }

    public void start() {
        for (Thread t : threads) {
            t.start();
        }
    }

    public void stop() {
        for (Thread t : threads) {
            t.interrupt();
            try {
                t.join();
            } catch (InterruptedException e) {
                Logger.getGlobal().severe(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    private static class Worker extends Thread {

        private final String message;
        private final int delay;

        private Worker(String message, int delay) {
            this.message = message;
            this.delay = delay;
        }

        @Override
        public void run() {
            while (true) {
                System.out.println(message);
                try {
                    sleep(delay);
                } catch (InterruptedException e) {
                    currentThread().interrupt();
                    break;
                }
            }
        }
    }

}
