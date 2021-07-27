package com.epam.rd.java.basic.practice5;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Part4 {

    private static int[][] matrix;

    public static void main(final String[] args) {
        loadMatrix("part4.txt");
        findMaxParallel();
        findMax();
    }

    private static void findMax() {
        long t1 = System.currentTimeMillis();
        int max = matrix[0][0];
        for (int[] row : matrix) {
            max = findMaxInRow(row);
        }
        long t2 = System.currentTimeMillis();
        System.out.println(max);
        System.out.println(t2 - t1);
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
        long t1 = System.currentTimeMillis();
        int numberOfThreads = matrix.length;
        int[] results = new int[numberOfThreads];
        Thread[] threads = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new MyThread(i, results);
        }
        Spam.runThreadArray(threads);
        int max = findMaxInRow(results);
        long t2 = System.currentTimeMillis();
        System.out.println(max);
        System.out.println(t2 - t1);
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

    public static void loadMatrix(String fileName) {
        List<int[]> rows = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                try (Scanner line = new Scanner(scanner.nextLine())) {
                    List<Integer> row = new ArrayList<>();
                    while (line.hasNextInt()) {
                        row.add(line.nextInt());
                    }
                    if (!row.isEmpty()) {
                        addRowToList(rows, row);
                    }
                }
            }
            convertListToArray(rows);
        } catch (IOException ex) {
            Logger.getGlobal().severe(ex.getMessage());
        }
    }

    private static void addRowToList(List<int[]> rows, List<Integer> row) {
        int[] arr = new int[row.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = row.get(i);
        }
        rows.add(arr);
    }

    private static void convertListToArray(List<int[]> rows) {
        matrix = new int[rows.size()][rows.get(0).length];
        for (int i = 0; i < rows.size(); i++) {
            matrix[i] = rows.get(i);
        }
    }
}
