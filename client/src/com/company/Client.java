package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Client {

    public static void main(String[] args) throws Exception {

        Executor servicePool = Executors.newCachedThreadPool();
        for(int i = 0 ; i < 100 ; i++){
            EchoClient client = new EchoClient("localhost", 9877,i);
            servicePool.execute(client);
        }

    }

    public static class EchoClient implements Runnable {
        private final Socket mSocket;
        private int index;
        public EchoClient(String host, int port, int index) throws IOException {
            mSocket = new Socket(host, port);
            this.index= index;
        }

        @Override
        public void run() {
            try {
                Thread readerThread = new Thread(this::readResponse);
                readerThread.start();

                OutputStream out = mSocket.getOutputStream();
                for (int i = 0; i < 100; i++) {
                    out.write(("hello from index=" + index + " request" + i + "\n").getBytes("utf-8"));
                    Thread.sleep(1000);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private void readResponse() {
            try {
                InputStream in = mSocket.getInputStream();
                byte[] buffer = new byte[1024];
                int n;
                while ((n = in.read(buffer)) > 0) {
                    System.out.write(buffer, 0, n);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
