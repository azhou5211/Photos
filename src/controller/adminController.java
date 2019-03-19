package controller;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.*;

/**
 * 
 * @author Andrew Zhou, Bang An
 *
 */

public class adminController {

	@FXML private ListView<User> userList;
	@FXML private TextField newUserName;

	public void initialize() throws IOException, ClassNotFoundException {
		FileInputStream fos = new FileInputStream("files/users.txt");
		ObjectInputStream oos = new ObjectInputStream(fos);
		@SuppressWarnings("unchecked")
		ArrayList<User> listOfUsers = (ArrayList<User>) oos.readObject();
		oos.close();
		
		for(User u:  listOfUsers) {
			userList.getItems().add(u);
		}
	}
	
	public void logoff(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
		Scene scene = new Scene(root);
		
		Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.setTitle("Photos");
		window.show();
	}

	public void addUser(ActionEvent e) throws IOException {
		String name = newUserName.getText();
		newUserName.setText("");
		if(name.equals("")) {
			setAlert("Cannot have empty username!");
			return;
		}
		if(checkDuplicate(name)) {
			setAlert("Cannot have duplicate usernames!");
			return;
		}
		User u = new User(name);
		userList.getItems().add(u);
		sortList();
		write();
	}

	public void deleteUser(ActionEvent e) throws IOException {
		User u = userList.getSelectionModel().getSelectedItem();
		if(u==null) {
			setAlert("Cannot delete without selecting a user to delete!");
			return;
		}
		if(u.getUsername().equals("admin")) {
			setAlert("Cannot delete admin!");
			return;
		}
		
		userList.getItems().remove(u);
		write();
	}

	public boolean checkDuplicate(String name) {
		ObservableList<User> tempList = userList.getItems();
		for (int i = 0; i < tempList.size(); i++) {
			if(name.equalsIgnoreCase(tempList.get(i).getUsername())) {
				return true;
			}
		}
		return false;
	}
	
	public void sortList() {
		class userSorter implements Comparator<User> {
			@Override
			public int compare(User a, User b) {
				return a.toString().compareToIgnoreCase(b.toString());
			}
		}
		ObservableList<User> tempList = userList.getItems();
		Collections.sort(tempList, new userSorter());
	}
	
	public void write() throws IOException {
		ObservableList<User> listOfUsersO = userList.getItems();
		ArrayList<User> listOfUsers = new ArrayList<User>();
		for(User traverse: listOfUsersO) {
			listOfUsers.add(traverse);
		}
		
		FileOutputStream fos = new FileOutputStream("files/users.txt");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(listOfUsers);
		oos.close();
	}
	
	public void setAlert(String alertString) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(alertString);
		alert.showAndWait();
	}
	
}
