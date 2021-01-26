package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SendThread extends Thread {
    public static Socket socket;
    private int type;//1是发送登录信息，2是发送聊天信息，3是发送下线信息//
    private String userName = null;
    private String password = null;
    private String user1 = null;
    private String user2 = null;
    private String message = null;

    //利用构造函数来传递线程的参数
    public SendThread(int typeID,String userName_,String password_){//登录时使用的构造函数
        this.type = typeID;
        this.userName = userName_;
        this.password = password_;
    }
    public SendThread(int typeID,String user1_,String user2_,String message_){//发送信息时使用的构造函数
        this.type = typeID;
        this.user1 = user1_;
        this.user2 = user2_;
        this.message = message_;
    }
    public SendThread(int typeID,String user1_){//登出时使用的构造函数
        this.type = typeID;
        this.user1 = user1_;
    }

    public void run(){//
        if(type == 1){
            sendLogIn();
        }else if (type == 2){
            sendMessage();
        }else if (type == 3) {
            sendLogOut();
        }
    }

    private void sendLogOut() {
        try {
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.write("W,"+user1);
            pw.write("W,"+user1);
            pw.flush();

        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void sendMessage() {
        try {
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.write("Y,"+user1+","+user2+","+message);
            pw.flush();

        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void sendLogIn() {

        try {
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.write("N,"+userName+","+password);
            pw.flush();
            System.out.println(userName+"登录成功！");
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{

        socket = new Socket("169.254.73.36", 12705);

    }
}