package com.epam.rd.java.basic.practice5;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class Part2 {

    private static final InputStream STD_IN = System.in;
    private static final InputStream MOCK_STREAM = new MockStream();

    private static final class MockStream extends InputStream {

        private boolean done = false;

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
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            for (int i = off; i < off + len ; i++) {
                int tmp = read();
                b[i] = (byte)tmp;
            }
            return len;
        }

    }

    public static void main(final String[] args) {

        System.setIn(MOCK_STREAM);
        Thread t = new Thread() {
            @Override
            public void run() {
                Spam.main(null);
            }
        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            Logger.getGlobal().severe(e.getMessage());
            t.interrupt();
        }
        System.setIn(STD_IN);
    }

}
