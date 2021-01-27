package client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OpenAction {

    public  OpenAction(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        primaryStage.show();
        System.out.println(getClass().getClassLoader().getResource("/view/chat.fxml"));
        loader.setLocation(getClass().getClassLoader().getResource("/view/chat.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
