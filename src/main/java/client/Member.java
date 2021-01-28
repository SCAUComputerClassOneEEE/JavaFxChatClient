package client;

import controller.ChatController;
import javafx.scene.control.Button;

import java.util.ArrayList;

/**
 * @Author: Sky
 * @Date: 2021/1/28 9:17
 */
public class Member extends Button {

    public ArrayList<String[]> chatRecord = new ArrayList<>();

    public Member(String name){
        this.setText(name);
        //this.setStyle("-fx-background-color: white");
        this.setOnAction(event -> {
            ChatController.receiver = this.getText();
            this.setStyle("-fx-background-color: #d696af");//点击
            for(Member m:ChatController.memberNameList){
                   if(m.getText()!=ChatController.receiver)
                    m.setStyle("-fx-background-color: #ffd4d5");
            }
            System.out.println("click "+this.getText());
            /*
            点击用户的事件
             */
            ChatController.updateChat.setValue(ChatController.updateChat.getValue()+1);
        });
    }


}
