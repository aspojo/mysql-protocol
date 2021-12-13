package top.logbug;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("start cl-mysql on 3306");
        ServerSocket serverSocket= new ServerSocket(3306);
        while (true){
            Socket socket = serverSocket.accept();
            Thread thread = new Thread(
                    () -> {

                        try {
                            System.out.println("new connection: " + socket);
                            String greeting="""
                    4a 00 00 00 0a 38 2e 30 2e 31 39 00 fd 02 00 00
                    53 01 71 2b 4b 6e 37 52 00 ff ff ff 02 00 ff c7
                    15 00 00 00 00 00 00 00 00 00 00 4b 4f 61 4e 31
                    0e 0e 6b 39 5d 56 0a 00 63 61 63 68 69 6e 67 5f
                    73 68 61 32 5f 70 61 73 73 77 6f 72 64 00
                                        
                    """.replaceAll("[\s\n]","");
                            byte[] bytes = HexUtil.decodeHex(greeting);
                            OutputStream outputStream = socket.getOutputStream();
                            outputStream.write(bytes);
                            outputStream.flush();
                            System.out.println("write greeting  "+greeting);


                            byte[] readBytes=new byte[1024];
                            InputStream ins = socket.getInputStream();

                            ins.read(readBytes);
                            String s= new String(readBytes);
                            System.out.println("read msg:  " + s);
                            String ok= """
                    07 00 00 05 00 00 00 02 00 00 00
                    """.replaceAll("[\s\n]","");
                            bytes = HexUtil.decodeHex(ok);
                            outputStream.write(bytes);
                            outputStream.flush();
                            System.out.println("write ok " + ok);

                            byte[] readBytes2=new byte[1024];
//
                            ins.read(readBytes2);

                            int packetLength = ByteUtil.bytesToInt(ArrayUtil.sub(readBytes2,0,3));
                            System.out.println("packetLength = " + packetLength);
                            int packetNumber = ByteUtil.bytesToInt(ArrayUtil.sub(readBytes2,3,4));
                            System.out.println("packetNumber = " + packetNumber);
                            System.out.println("sql = " + new String(ArrayUtil.sub(readBytes2,4,1024)));
                        }catch (Exception e){
                            e.printStackTrace();
                        }

            });
            thread.start();


        }
    }
}
