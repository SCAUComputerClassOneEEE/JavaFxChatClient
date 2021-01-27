package service;

import controller.ChatController;
import javafx.stage.Stage;

public class ChangeService {//更改页面的公共变量 当是login就是这个stage就是login的stage，点击login就进入chat界面，stage就相应改变。
    public static Stage stage;
    public static ChatController chatController=new  ChatController();

}
