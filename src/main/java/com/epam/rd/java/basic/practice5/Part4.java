package com.epam.rd.java.basic.practice5;


import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Part4 {

    private static int[][] matrix;

    public static void main(final String[] args) {
        loadMatrix();
        findMax();
        findMaxParallel();
    }

    private static void findMax() {
        long t1 = System.nanoTime();
        int max = matrix[0][0];
        for (int[] row : matrix) {
            for (int n : row) {
                max = getMax(max, n);
            }
        }
        long t2 = System.nanoTime();
        System.out.println(max);
        System.out.println((t2 - t1) / 1_000_000);
    }

    private static class MyThread extends Thread {

        final int row;
        final int[] results;

        MyThread(int row, int[] results) {
            this.row = row;
            this.results = results;
        }

        @Override
        public void run() {
            results[row] = findMaxInRow(matrix[row]);
        }
    }

    private static void findMaxParallel() {
        int numberOfThreads = matrix.length;
        int[] results = new int[numberOfThreads];
        Thread[] threads = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads ; i++) {
            threads[i] = new MyThread(i, results);
        }
        long t1 = System.nanoTime();
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
        int max = findMaxInRow(results);
        long t2 = System.nanoTime();
        System.out.println(max);
        System.out.println((t2 - t1) / 1_000_000);
    }

    private static int findMaxInRow(int[] m) {
        int max = m[0];
        for (int n : m) {
            max = getMax(max, n);
        }
        return max;
    }

    private static int getMax(int max, int n) {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Logger.getGlobal().severe(e.getMessage());
            Thread.currentThread().interrupt();
        }
        return Math.max(max, n);
    }

    private static void loadMatrix() {
        String input = getInput("part4.txt");
        String[] lines = input.split(System.lineSeparator());
        matrix = new int[lines.length][];
        int row = 0;
        for (String line : lines) {
            String[] words = line.split("\\s+");
            if (words.length < 2) {
                continue;
            }
            int[] numberRow = new int[words.length];
            for (int j = 0; j < words.length; j++) {
                numberRow[j] = Integer.parseInt(words[j]);
            }
            matrix[row++] = numberRow;
        }
        int[][] tmp = new int[row][];
        System.arraycopy(matrix, 0, tmp, 0, row);
        matrix = tmp;
    }

    public static String getInput(String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File(fileName), "utf-8");
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine()).append(System.lineSeparator());
            }
            scanner.close();
            return sb.toString().trim();
        } catch (IOException ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage(), ex);
        }
        return sb.toString();
    }
}
