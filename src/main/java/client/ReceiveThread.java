package client;

import com.sun.javafx.binding.StringFormatter;
import controller.ChatController;
import javafx.application.Platform;
import service.ChangeService;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;


public class ReceiveThread extends Thread {
    public static Socket socket;

    private static boolean isLoginSuccess = false;

    public static boolean isLogin(){
        return isLoginSuccess;
    }

    public void run() {
        try {
            System.out.println("进来recieve的run方法了");
            InputStream in = socket.getInputStream();
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(in));
            String str;
            char[] temp = new char[256];
            while (true) {
                int len = reader.read(temp);//不断接收服务端发送的信息
                System.out.println("我是read的下一句");
                str = String.valueOf(temp);
                System.out.println("有收到服务器的消息吗？");
                if (len > 0) {
                    if (str.startsWith("Y")) {//更新信息
                        System.out.println("进来更新消息这里了");
                        System.out.println("str是啥"+str);
                        updataChat(str);
                    }
                    else if (str.startsWith("N")) {//登入信息
                        System.out.println("str="+str);
                        logIn(str);
                    }else if (str.startsWith("W")){//下线信息
                        offLine(str);
                    }
                }
                System.out.println("len为0");
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void logIn(String str) throws Exception {
        synchronized (ReceiveThread.class) {
            if(str.equals("N,N")){
                isLoginSuccess = false;
                ReceiveThread.class.notifyAll();
                //显示登录失败信息
            }else {//初始化在线用户
                str = str.substring(2);//删除标识  N,
                //String[] onlineUserList = str.split(",");//onlineUserList是在线用户列表，用于初始化
                isLoginSuccess = true;
                ReceiveThread.class.notifyAll();
                //登录成功，显示上线信息
                ChatController.memberName = str;
                ChatController.changeType = "用户上线";
                System.out.println("111");
                ChatController.leftPaneListener.setValue(ChatController.leftPaneListener.getValue()+1);
            }
        }
    }


    private void offLine(String str) {
        str = str.substring(2,str.length());//删除标识  W,
        //此时str是下线的用户的标识
        //显示下线信息
        ChatController.changeType = "用户下线";
    }

    private void updataChat(String str) {
        System.out.println("进来updatachat这里了");
        System.out.println("str是啥"+str);//
        str = str.substring(2,str.length());//删除标识  Y,
        String[] message = str.split(",");
        /*
        message[0]是接收者的标识
        message[1]是发送者的标识
        message[2]是发送的消息
         */
        int i=0;
        for (String s:message){
            System.out.println("message["+i+"]是："+s);
        }

        ChatController.message = message;

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