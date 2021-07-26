package com.epam.rd.java.basic.practice5;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class Part5 implements Runnable {

    private static final int LINE_LENGTH = 20 + System.lineSeparator().length();
    private static final int FILE_LENGTH = 10 * LINE_LENGTH;

    private static RandomAccessFile outputFile;

    private final int number;

    private Part5(int number) {
        this.number = number;
    }

    public static void main(final String[] args) {
        if (createFile(FILE_LENGTH)) {
            return;
        }
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new Part5(i));
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Logger.getGlobal().severe(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
        try {
            showFile();
            outputFile.close();
        } catch (IOException e) {
            Logger.getGlobal().severe(e.getMessage());
        }
    }

    private static boolean createFile(int len) {
        try {
            RandomAccessFile file = new RandomAccessFile("part5.txt", "rw");    //NOSONAR
            file.setLength(len);
            outputFile = file;
        } catch (IOException e) {
            Logger.getGlobal().severe(e.getMessage());
            return true;
        }
        return false;
    }

    private static void showFile() throws IOException {
        outputFile.seek(0);
        byte[] data = new byte[FILE_LENGTH];
        int size = outputFile.read(data);
        if (size != FILE_LENGTH) {
            throw new IOException("Incorrect file size.");
        }
        System.out.print(new String(data));
    }

    @Override
    public void run() {
        processDigit(number);
    }

    private static synchronized void processDigit(int number) {
        try {
            outputFile.seek((long) LINE_LENGTH * number);
            int n = '0' + number;
            for (int i = 0; i < 20; i++) {
                outputFile.write(n);
                pause();
            }
            outputFile.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            Logger.getGlobal().severe(e.getMessage());
        }
    }

    public static void pause() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Logger.getGlobal().severe(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
