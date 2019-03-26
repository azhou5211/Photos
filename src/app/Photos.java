package app;

import java.io.IOException;
import controller.loginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class Photos extends Application {
	
	static loginController controller;
	
	@Override
	public void start(Stage primaryStage) throws IOException, ClassNotFoundException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		controller = loader.getController();
		controller.startup();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Photos");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	@Override
	public void stop() throws IOException, ClassNotFoundException {
		controller.shutdown();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
