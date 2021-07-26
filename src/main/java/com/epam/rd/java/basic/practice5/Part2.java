package com.epam.rd.java.basic.practice5;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class Part2 {

    private static final InputStream STD_IN = System.in;
    private static final InputStream MOCK_STREAM = new MockStream();
    private static int INPUT_BYTE = -1;

    private static final class MockStream extends InputStream {

        boolean done = false;

        @Override
        public int read() throws IOException {
            if (done) {
                return -1;
            } else {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Logger.getGlobal().severe(e.getMessage());
                    Thread.currentThread().interrupt();
                }
                done = true;
                return '\n';
            }
    //            int out = INPUT_BYTE;
    //            INPUT_BYTE = -1;
    //            return out;
        }
    }

//    private static class Mocker implements Runnable {
//
//        @Override
//        public void run() {
//            while (true) {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    Logger.getGlobal().severe(e.getMessage());
//                    Thread.currentThread().interrupt();
//                    break;
//                }
//                INPUT_BYTE = (int) '\n';
//            }
//        }
//    }

    public static void main(final String[] args) {

        System.setIn(MOCK_STREAM);
        Thread t = new Thread() {
            @Override
            public void run() {
                Spam.main(null);
            }
        };
//        Thread mt = new Thread(new Mocker());
        t.start();
//        mt.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            Logger.getGlobal().severe(e.getMessage());
            t.interrupt();
        }
//        try {
//            mt.join();
//        } catch (InterruptedException e) {
//            Logger.getGlobal().severe(e.getMessage());
//            mt.interrupt();
//        }
        System.setIn(STD_IN);
    }

}
