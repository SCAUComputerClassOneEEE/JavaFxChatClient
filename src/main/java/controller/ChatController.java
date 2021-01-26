package controller;

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
import java.util.ResourceBundle;

import static javafx.application.Application.launch;

public class ChatController extends Application implements Initializable  {

    @FXML
    private Button btn_send;

    @FXML
    private FlowPane messageList;

    @FXML
    private TextArea text_input;

    @FXML
    private ScrollPane scrollPane_history;

    private boolean last = true;

    public static String MyId="000";//自己的id

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
        scrollPane_history.setVvalue(1);

        //左边界面添加监听器
        addListener2leftPaneListener();
    }



    public void addMessageBox(String[] message) {//socket要给我一个数组里面存了谁发的，发给谁，发什么。调用这个方法就可以实现消息盒子的更新了
        Label messageFromWho = new Label(message[0]);
        messageFromWho.setWrapText(true);
        messageFromWho.setMaxWidth(220);


        Label messageBubble = new Label(message[2]);
        messageBubble.setWrapText(true);
        messageBubble.setMaxWidth(220);
        messageBubble.setStyle("-fx-background-color: rgb(179,231,244); -fx-background-radius: 8px;");
        messageBubble.setPadding(new Insets(6));
        messageBubble.setFont(new Font(14));
        HBox.setMargin(messageBubble, new Insets(8, 0, 0, 0));

        boolean isMine = message[2] == MyId;
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
        triangle.setFill(Color.rgb(179,231,244));
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

        last = scrollPane_history.getVvalue() == 1.0;
        messageList.getChildren().add(messageBox);
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

    /**
     * 给leftPaneListener添加监听器
     */
    private void addListener2leftPaneListener(){
        leftPaneListener.addListener(((observable, oldValue, newValue) -> {
            Platform.runLater(this::upDateLeftPane);
        }));
    }

    /*
    假定有一个存放所有user名字的list
     */
    private ArrayList<String> memberNameList = new ArrayList<>();
    private void upDateLeftPane(){
        /*
        根据users列表更新子节点
         */
        //处理节点位置
        switch (changeType) {
            case "用户上线"://新增
                memberNameList.add(0,memberName);
                break;
            case "用户下线"://移除
                memberNameList.remove(memberName);
                break;
            case "收到消息"://移到前面
                memberNameList.remove(memberName);
                memberNameList.add(0,memberName);
                break;
        }
        //更新界面
        fillMember(memberName);
    }

    /**
     * 加载节点 memberName用于收到消息时，给memberName对应的节点标红
     * @param memberName
     */
    private void fillMember(String memberName){
        //先清空所有节点
        leftPane.getChildren().clear();
        for(int i=0;i<memberNameList.size();i++){
            Button eachMember = new Button(memberNameList.get(i));
            eachMember.setStyle("-fx-background-color: gray");
            if (changeType.equals("收到消息") && eachMember.getText().equals(memberName)){
                eachMember.setStyle("-fx-background-color: red");
            }
            eachMember.setOnAction(event -> {
                eachMember.setStyle("-fx-background-color: gray");
                // eachMember.getText();
                /*
                这里用于点击事件，加载聊天框
                 */
            });
            leftPane.getChildren().add(eachMember);
        }
    }


    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        System.out.println(getClass().getClassLoader().getResource("chat.fxml"));
        loader.setLocation(getClass().getClassLoader().getResource("chat.fxml"));
        Parent root  = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
