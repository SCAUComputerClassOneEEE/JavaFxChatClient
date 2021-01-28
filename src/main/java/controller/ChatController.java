package controller;

import client.Member;
import client.OpenAction;
import client.SendThread;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;

import static javafx.application.Application.launch;

public class ChatController  implements Initializable {

    @FXML
    private Button btn_send;

    @FXML
    private FlowPane messageList;

    @FXML
    private TextArea text_input;

    @FXML
    private ScrollPane scrollPane_history;

    private boolean last = true;

    public static String MyId;//自己的id

    public void initialize(URL location, ResourceBundle resources) {
        VBox.setVgrow(messageList, Priority.ALWAYS);
        //监听滚动，得在最低端添加信息
        scrollPane_history.vvalueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if (last) {
                    scrollPane_history.setVvalue(1.0);
                    last = false;
                }
            }
        });

        System.out.println("这里确实是有吧Vvale设置成1了的");
        scrollPane_history.setVvalue(1);

        //左边界面添加监听器
        addListener2leftPaneListener();
        updateChatListener();
    }

    public static String[] message = null;

    public static SimpleObjectProperty<Integer> updateChat = new SimpleObjectProperty<>(0);

    private void updateChatListener(){
        updateChat.addListener(((observable, oldValue, newValue) -> {
            Platform.runLater(this::addMessageBox);
        }));
    }
    /*
    往右边的flowpane里面加一条消息
     */
    public void addMessageBox() {//socket要给我一个数组里面存了发给谁，谁发的，发什么。调用这个方法就可以实现消息盒子的更新了
        Member member = null;
        for(Member each:memberNameList){
            if(each.getText().equals(receiver)){
                member = each;
                break;
            }
        }
        if (member!=null){
            messageList.getChildren().clear();
            for(String[] message:member.chatRecord){
                System.out.println("进来addmessageBOx了mamam?????");
                Label messageFromWho = new Label(message[0]);
                messageFromWho.setWrapText(true);
                messageFromWho.setMaxWidth(220);
                HBox.setMargin(messageFromWho, new Insets(8, 0, 0, 0));

                Label messageBubble = new Label(message[2]);
                messageBubble.setWrapText(true);
                messageBubble.setMaxWidth(220);
                messageBubble.setStyle("-fx-background-color: rgb(179,231,244); -fx-background-radius: 8px;");
                messageBubble.setPadding(new Insets(6));
                messageBubble.setFont(new Font(14));
                HBox.setMargin(messageBubble, new Insets(8, 0, 0, 0));

                boolean isMine = message[1] == MyId;
                double[] points;
                if (!isMine) {
                    points = new double[]{
                            0.0, 5.0,
                            10.0, 0.0,
                            10.0, 10.0
                    };
                } else {
                    points = new double[]{
                            0.0, 0.0,
                            0.0, 10.0,
                            10.0, 5.0
                    };
                }

                Polygon triangle = new Polygon(points);
                triangle.setFill(Color.rgb(179, 231, 244));

                HBox messageBox = new HBox();
                messageBox.setPrefWidth(366);
                messageBox.setPadding(new Insets(10, 5, 10, 5));

                if (isMine) {
                    HBox.setMargin(triangle, new Insets(15, 10, 0, 0));
                    messageBox.getChildren().addAll(messageBubble, triangle, messageFromWho);
                    messageBox.setAlignment(Pos.TOP_RIGHT);
                } else {
                    HBox.setMargin(triangle, new Insets(15, 0, 0, 10));
                    messageBox.getChildren().addAll(messageFromWho, triangle, messageBubble);
                }


                System.out.println("scrollPane_history.getVvalue()"+scrollPane_history.getVvalue());
                last =(scrollPane_history.getVvalue() == 1.0);
                System.out.println("我来到添加之前的这一句了！");
                messageList.getChildren().add(messageBox);
            }
        }
    }


    /**
     * sky's workspace
     */
    @FXML
    private VBox leftPane;//左边的用户框

    //整型监听器，每当用户列表改变时，这个变量+1，就可以触发监听线程
    public static SimpleObjectProperty<Integer> leftPaneListener = new SimpleObjectProperty<Integer>(0);
    public static String memberName = null;//修改左边界面的原因：1新增用户 2用户下线 3用户发来消息
    public static String changeType = null;//改变的那个用户的名字

    public static String receiver = "all";
    /**
     * 给leftPaneListener添加监听器
     */
    private void addListener2leftPaneListener() {
        Platform.runLater(()-> {
            Member all = new Member("all");
            memberNameList.add(all);
            fillMember(null);
        });
        leftPaneListener.addListener(((observable, oldValue, newValue) -> {
            Platform.runLater(this::upDateLeftPane);
        }));
    }

    /*
    假定有一个存放所有user名字的list
     */
    private final ArrayList<Member> memberNameList = new ArrayList<>();

    private void upDateLeftPane() {
        /*
        根据users列表更新子节点
         */
        //处理节点位置
        switch (changeType) {
            /*
            这里面的代码只添加、删除用户或者调整顺序
             */
            case "用户上线":
                System.out.println("memberName="+memberName);//lyx,
                String[] onlineUserList = memberName.split(",");//onlineUserList是在线用户列表，用于初始化
                onlineUserList[onlineUserList.length-1] = "";
                for(String each:onlineUserList){
                    if (each.equals(""))continue;
                    System.out.println("name="+each);
                    memberNameList.add(new Member(each));
                }
                break;
            case "用户下线"://移除
                memberNameList.removeIf(each -> each.getText().equals(memberName));
                break;
            case "收到消息"://移到前面
                for (Member each:memberNameList){
                    if (memberName.equals(each.getText())){
                        if (! memberName.equals("all")){
                            memberNameList.remove(each);
                            memberNameList.add(1,each);
                        }
                        each.chatRecord.add(message);
                        break;
                    }
                }

                break;
        }
        //只更新左边的界面
        fillMember(memberName);
    }

    /**
     * 加载节点 memberName用于收到消息时，给memberName对应的节点标红
     *
     * @param memberName
     */
    private void fillMember(String memberName) {
        //先清空所有节点
        leftPane.getChildren().clear();
        for (Member eachMember : memberNameList) {
            eachMember.setPrefWidth(leftPane.getPrefWidth());
            eachMember.setStyle("-fx-background-color: White");
            if (memberName!=null && changeType.equals("收到消息") && eachMember.getText().equals(memberName)) {
                /*
                找到对应的接受者的Member对象
                 */
                eachMember.setStyle("-fx-background-color: #ffb700");
                /*
                添加记录
                 */
                /*if (message!=null){
                    addMessageBox();
                }*/
            }
            //将所有Member加入左边的列表进行显示
            leftPane.getChildren().add(eachMember);
        }
    }

    @FXML
    private void send(){
        String message =  text_input.getText();
        text_input.setText("");
        System.out.println(receiver+","+MyId+","+message);

        SendThread sendThread = new SendThread(2,receiver,MyId,message);
        sendThread.sendMessage();

        for(Member each:memberNameList){
            if(each.getText().equals(receiver)){
                /*
                找到接受者
                 */
                String[] record = new String[]{receiver,MyId,message};
                each.chatRecord.add(record);//加新消息数组 0发给谁 1谁发的 2发了什么
                receiver = each.getText();
                updateChat.setValue(updateChat.getValue()+1);
            }
        }
    }
}
