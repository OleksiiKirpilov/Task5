package com.epam.rd.java.basic.practice5;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Logger;

public class Part5 implements Runnable {

    private static RandomAccessFile outputFile;

    public static void main(final String[] args) {
        if (!createFile(10 * (20 + System.lineSeparator().length()))) {
            return;
        }
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread();
        }

    }

    private static boolean createFile(int len) {
        RandomAccessFile file;
        try {
            file = new RandomAccessFile("part5.txt", "rw");
        } catch (FileNotFoundException e) {
            Logger.getGlobal().severe(e.getMessage());
            return true;
        }
        try {
            file.setLength(len);
        } catch (IOException e) {
            Logger.getGlobal().severe(e.getMessage());
            return true;
        }
        outputFile = file;
        return false;
    }

    @Override
    public void run() {

    }
}
