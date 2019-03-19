package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

/**
 * 
 * @author Andrew Zhou, Bang An
 *
 */

public class loginController {

	@FXML private TextField username;

	public void signIn(ActionEvent e) throws IOException, ClassNotFoundException {
		String name = username.getText();

		if (name.equals("admin")) {
			Parent root = FXMLLoader.load(getClass().getResource("/view/admin.fxml"));
			Scene scene = new Scene(root);
			
			Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
			window.setScene(scene);
			window.setTitle("Photos (Admin)");
			window.show();
			return;
		}
		
		/*
		  // Add admin and stock 
		 User admin = new User("admin"); User stock = new
		 User("stock"); ObservableList<User> listOfUsers;
		 listOfUsers.add(admin); listOfUsers.add(stock);
		 
		 FileOutputStream fos = new FileOutputStream("files/users.txt");
		 ObjectOutputStream oos = new ObjectOutputStream(fos);
		 oos.writeObject(listOfUsers); oos.close();
		 */

		FileInputStream fos = new FileInputStream("files/users.txt");
		ObjectInputStream oos = new ObjectInputStream(fos);
		@SuppressWarnings("unchecked")
		ArrayList<User> listOfUsers = (ArrayList<User>) oos.readObject();
		oos.close();
		for (User u : listOfUsers) {
			if (name.equals(u.getUsername())) {
				return;
			}
		}
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText("Username does not exist!");
		alert.showAndWait();

	}
}
