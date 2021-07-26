package com.epam.rd.java.basic.practice5;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class Part5 implements Runnable {

    private static RandomAccessFile outputFile;
    private static int lineLength;
    private static int fileLength;

    private final int number;

    private Part5(int number) {
        this.number = number;
    }

    public static void main(final String[] args) {
        lineLength = 20 + System.lineSeparator().length();
        fileLength = 10 * lineLength;
        if (createFile(fileLength)) {
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
        byte[] data = new byte[fileLength];
        int size = outputFile.read(data);
        if (size != fileLength) {
            throw new IOException("Incorrect file size.");
        }
        System.out.print(new String(data));
    }

    @Override
    public void run() {
        synchronized (Part5.class) {
            try {
                outputFile.seek((long) lineLength * number);
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
    }

    public void pause() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Logger.getGlobal().severe(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
