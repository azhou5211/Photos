package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
import controller.albumController;

/**
 * 
 * @author Andrew Zhou, Bang An
 *
 */

public class loginController {

	@FXML private TextField username;
	static ArrayList<User> listOfUsers;
	
	//public ArrayList<User> listOfUsers;
	
	public void startup() throws IOException, ClassNotFoundException {
		FileInputStream fos = new FileInputStream("files/users.txt");
		ObjectInputStream oos = new ObjectInputStream(fos);
		listOfUsers = (ArrayList<User>) oos.readObject();
		oos.close();
	}
	
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
		
		
		for (User u : listOfUsers) {
			if (name.equals(u.getUsername())) {
				//Parent root = FXMLLoader.load(getClass().getResource("/view/album.fxml"));
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/album.fxml")); 
				Parent root = (Parent)fxmlLoader.load();
				Scene scene = new Scene(root);
				
				//System.out.println(u.getAlbumList().size());
				albumController controller = fxmlLoader.getController();
				controller.setUp(u);
				//System.out.println(u.getAlbumList().size());
				
				Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
				window.setScene(scene);
				window.setTitle("Photos");
				window.show();
				return;
			}
		}
		//System.out.println(listOfUsers.get(1).getAlbumList().size());
		
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText("Username does not exist!");
		alert.showAndWait();
		
	}

	public void shutdown() throws IOException {
		FileOutputStream fos = new FileOutputStream("files/users.txt");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(listOfUsers);
		oos.close();
		System.out.println("Files have been saved");
	}
}
