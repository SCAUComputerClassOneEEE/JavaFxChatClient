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
    private ChatController chatController;

    public void run() {
        try {
            InputStream in = socket.getInputStream();
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(in));
            String str;
            while (true) {
                str = reader.readLine();//不断接收服务端发送的信息
                if (str != null) {
                    if (str.startsWith("Y")) {//更新信息
                        updataChat(str);
                    }
                    else if (str.startsWith("N")) {//登入信息
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
        if(str.equals("N,N")){

            //显示登录失败信息

        }else {//初始化在线用户
            str = str.substring(2,str.length());//删除标识  N,
            String[] olineUserList = str.split(",");//olineUserList是在线用户列表，用于初始化

            //登录成功，显示上线信息
        }
    }

    private void offLine(String str) {
        str = str.substring(2,str.length());//删除标识  W,
        //此时str是下线的用户的标识

        //显示下线信息
    }

    private void updataChat(String str) {
        str = str.substring(2,str.length());//删除标识  Y,
        String[] message = str.split(",");
        /*
        message[0]是接收者的标识
        message[1]是发送者的标识
        message[2]是发送的消息
         */

        chatController.addMessageBox(message);

        ChatController.leftPaneListener.setValue(ChatController.leftPaneListener.getValue()+1);
        ChatController.changeType = "";//修改左边界面的原因：1新增用户 2用户下线 3用户发来消息
        ChatController.memberName = "";//改变的那个用户的名字
        //更新信息界面
    }

/*

    public static void main(String[] args) throws UnknownHostException, IOException {
        socket = new Socket("169.254.73.36", 12705);//端口号？

    }

 */
}