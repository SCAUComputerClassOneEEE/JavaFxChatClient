package controller;

import client.ReceiveThread;
import client.SendThread;
import javafx.application.Application;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

import static javafx.application.Application.launch;

public class LoginController extends Application {
	@FXML
	private TextField text_password;
	@FXML
	private TextField text_username;
	@FXML
	private Button btn_clear;
	@FXML
	private Button btn_login;


	@FXML
	private void login() throws IOException {  //登录
		SendThread.socket = new Socket("169.254.73.36", 12705);
		ReceiveThread.socket = SendThread.socket;

		String username = text_username.getText();
		String password = text_password.getText();
		SendThread sendThread1 = new SendThread(1, username, password); //type为1是发送登录信息
		ReceiveThread receiveThread = new ReceiveThread();
		receiveThread.start();
		sendThread1.sendLogIn();
	}

	@FXML
	private void clear() { //清除登录信息
		text_username.clear();
		text_password.clear();
	}

	public static void main(String[] args) {
		launch(args);
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		System.out.println(getClass().getClassLoader().getResource("login.fxml"));
		loader.setLocation(getClass().getClassLoader().getResource("login.fxml"));
		Parent root  = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
