package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

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
    }



    private void addMessageBox(String[] message) {//socket要给我一个数组里面存了谁发的，发给谁，发什么。调用这个方法就可以实现消息盒子的更新了
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
}
