package com.epam.rd.java.basic.practice5;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class Part5 implements Runnable {

    private static final int LINE_LENGTH = 20 + System.lineSeparator().length();
    private static final int FILE_LENGTH = 10 * LINE_LENGTH;
    private static final String FILE_NAME = "part.txt";

    private final int number;

    private Part5(int number) {
        this.number = number;
    }

    public static void main(final String[] args) {
        if (createFile()) {
            return;
        }
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new Part5(i));
        }
        Spam.runThreadArray(threads);
        try {
            showFile();
        } catch (IOException e) {
            Logger.getGlobal().severe(e.getMessage());
        }
    }

    private static boolean createFile() {
        try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "rw")) {
            file.setLength(FILE_LENGTH);
        } catch (IOException e) {
            Logger.getGlobal().severe(e.getMessage());
            return true;
        }
        return false;
    }

    private static void showFile() throws IOException {
        byte[] data = new byte[FILE_LENGTH];
        int size = 0;
        try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "rw")) {
            file.seek(0);
            size = file.read(data);
        } catch (IOException e) {
            Logger.getGlobal().severe(e.getMessage());
            throw new IOException();
        }
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
        try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "rw")) {
            file.seek((long) LINE_LENGTH * number);
            int n = '0' + number;
            for (int i = 0; i < 20; i++) {
                file.write(n);
                pause();
            }
            file.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
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
