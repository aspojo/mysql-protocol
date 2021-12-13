package top.logbug;

import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("start cl-mysql on 3306");
        ServerSocket serverSocket= new ServerSocket(3306);
        while (true){
            Socket socket = serverSocket.accept();
            String greating="""
                    4a 00 00 00 0a 38 2e 30 2e 31 39 00 fd 02 00 00
                    53 01 71 2b 4b 6e 37 52 00 ff ff ff 02 00 ff c7
                    15 00 00 00 00 00 00 00 00 00 00 4b 4f 61 4e 31
                    0e 0e 6b 39 5d 56 0a 00 63 61 63 68 69 6e 67 5f
                    73 68 61 32 5f 70 61 73 73 77 6f 72 64 00
                                        
                    """.replaceAll("\s","");
            byte[] bytes = HexUtil.decodeHex(greating);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();


            BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String s = reader.readLine();
            System.out.println("s = " + s);
            String ok= """
                    07 00 00 05 00 00 00 02 00 00 00
                    """.replaceAll("\s","");
            bytes = HexUtil.decodeHex(ok);
            System.out.println("ok = " + ok);
            outputStream.write(bytes);
            outputStream.flush();

        }
    }
}
