package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(9877);

        Executor servicePool = Executors.newCachedThreadPool();

        while(true){
            Socket client = serverSocket.accept();
            servicePool.execute(new EchoServer(client));
        }
    }
    public static class EchoServer implements Runnable{
        private final Socket client;

        public EchoServer(Socket socket) throws IOException {
            this.client = socket;
        }

        @Override
        public void run(){
            try {
                PrintStream out = new PrintStream(client.getOutputStream());
                BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
                boolean flag = true;
                while(flag){
                    String str= buf.readLine();
                    if(str == null || "".equals(str)){
                        break;
                    }else{
                        if( "bye".equals( str)){
                            flag = false;
                        } else {
                            //将接收到的字符串前面加上echo，发送到对应的客户端
                            out.println("Server return:" + str + "\t" + Calendar.getInstance().getTime());
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
