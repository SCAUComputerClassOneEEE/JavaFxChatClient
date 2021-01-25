package client;

import controller.ChatController;
import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;


public class ReceiveThread extends Thread {
    public static Socket socket;

    public void run() {
        try {//
            InputStream in = socket.getInputStream();
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(in));
            String str;
            while (true) {
                str = reader.readLine();
                if (str != null) {
                    if (str.startsWith("Y")) {
                        updataChat(str);
                    }
                    else if (str.startsWith("N")) {
                        logIn(str);

                    }else if (str.startsWith("W")){//下线信息
                        offLine(str);
                    }
                }
            }

        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void logIn(String str) {
        if(str.equals("N/N")){
            //显示登录失败信息
        }else {
            str = str.substring(2,str.length());//删除标识N
            //登录成功，显示上线信息
        }
    }

    private void offLine(String str) {
        str = str.substring(2,str.length());//删除标识W
        //显示下线信息
    }

    private void updataChat(String str) {
        str = str.substring(2,str.length());//删除标识Y
        String[] message = str.split("//");
        //更新信息界面
    }



    public static void main(String[] args) throws UnknownHostException, IOException {
        socket = new Socket("169.254.40.2",6666);//端口号？

    }
}
