package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;

public class ChatController {

    @FXML
    private Button btn_send;

    @FXML
    private FlowPane messageList;

    @FXML
    private TextArea text_input;

    @FXML
    private ScrollPane scrollPane_history;

}
