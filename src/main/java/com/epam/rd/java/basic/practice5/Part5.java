package com.epam.rd.java.basic.practice5;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class Part5 implements Runnable {

    private static final int LINE_LENGTH = 20 + System.lineSeparator().length();
    private static final int FILE_LENGTH = 10 * LINE_LENGTH;

    private static RandomAccessFile file;

    private final int number;

    private Part5(int number) {
        this.number = number;
    }

    public static void main(final String[] args) {
        // (3) You can use not more than one object of the RandomAccessFile class!
        try (RandomAccessFile file = new RandomAccessFile("part.txt", "rw")) {
            Part5.file = file;
            file.setLength(FILE_LENGTH);
            Thread[] threads = new Thread[10];
            for (int i = 0; i < 10; i++) {
                threads[i] = new Thread(new Part5(i));
            }
            Spam.runThreadArray(threads);
            showFile();
        } catch (IOException e) {
            Logger.getGlobal().severe(e.getMessage());
        }
    }

    private static void showFile() throws IOException {
        byte[] data = new byte[FILE_LENGTH];
        file.seek(0);
        file.readFully(data);
        System.out.print(new String(data));
    }

    @Override
    public void run() {
        processDigit(number);
    }

    private static synchronized void processDigit(int number) {
        try {
            file.seek((long) LINE_LENGTH * number);
            int n = '0' + number;
            for (int i = 0; i < 20; i++) {
                file.write(n);
                pause(1);
            }
            file.write(System.lineSeparator().getBytes());
        } catch (IOException e) {
            Logger.getGlobal().severe(e.getMessage());
        }
    }

    public static void pause(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Logger.getGlobal().severe(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
