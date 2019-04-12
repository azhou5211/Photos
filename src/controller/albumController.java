package controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

/**
 * 
 * @author Andrew Zhou, Bang An
 *
 */

public class albumController {

	@FXML private MenuBar myMenuBar;
	@FXML private ListView<Album> albumList;
	@FXML private TextField albumName;
	@FXML private TextField numberOfPhotos;
	@FXML private TextField creationDate;
	@FXML private TextField modifiedDate;
	@FXML private TextField renameTextField;
	@FXML private TextField newAlbumName;
	@FXML private TextField tagName1;
	@FXML private TextField tagName2;
	@FXML private TextField tagValue1;
	@FXML private TextField tagValue2;
	@FXML private DatePicker startDate;
	@FXML private DatePicker endDate;
	
	static User globalUser;
	static DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

	public void setUp(User u) {
		globalUser = u;
		
		albumList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Album>() {
			@Override
			public void changed(ObservableValue<? extends Album> observable, Album oldValue, Album newValue) {
				displayDetails(newValue);
			}
		});
		
		albumList.getItems().clear();
		ArrayList<Album> list = u.getAlbumList();
		for(Album a: list) {
			albumList.getItems().add(a);
		}
		sortList();
	}
	
	public void addAlbum(ActionEvent e) {
		//globalUser.getAlbumList().add(new Album("test"));
		
		// Check if the textfields are filled
		String newName = newAlbumName.getText();
		if(newName.equals("")) {
			setAlert("Cannot have empty Album Name!");
			return;
		}
		
		// check duplicate
		if(checkDuplicate(newName,-1)) {
			setAlert("Cannot have duplicate Album Name!");
			return;
		}
		
		Album newAlbum = new Album(newName);
		globalUser.getAlbumList().add(newAlbum);
		newAlbumName.setText("");
		albumList.getItems().add(newAlbum);
		sortList();
	}
	
	public void deleteAlbum(ActionEvent e) {
		//globalUser.getAlbumList().add(new Album("test"));
		// Check if listview has item selected
		Album albumToDelete = albumList.getSelectionModel().getSelectedItem();
		if(albumToDelete==null) {
			setAlert("Must select an Album to Delete!");
			return;
		}
		if(albumToDelete.toString().equals("Stock Photos") && globalUser.getUsername().equals("stock")) {
			setAlert("Cannot delete stock photos of stock user!");
			return;
		}
		
		globalUser.getAlbumList().remove(albumToDelete);
		albumList.getItems().remove(albumToDelete);
		sortList();
	}
	
	public void rename(ActionEvent e) {
		Album a = albumList.getSelectionModel().getSelectedItem();
		int index = albumList.getSelectionModel().getSelectedIndex();
		if(a==null) {
			setAlert("Must select an album to rename!");
			return;
		}
		
		// Check if the textfields are filled
		String name = renameTextField.getText();
		if(name.equals("")) {
			setAlert("Name cannot be empty!");
			return;
		}
		
		// check duplicate
		if (checkDuplicate(name,index)) {
			setAlert("Cannot have duplicate Album names!");
			return;
		}
		
		renameTextField.setText("");
		a.setAlbumName(name);
		
		sortList();
	}
	
	public void singleTagSearch(ActionEvent e) throws IOException {
		/*
		 * @FXML private TextField tagName1;
		 * @FXML private TextField tagName2;
		@FXML private TextField tagValue1;
		@FXML private TextField tagValue2;
		 */

		if(tagName1.getText().length()==0 || tagValue1.getText().length()==0) {
			setAlert("Tag name 1 and Tag Value 1 cannot be empty!");
			return;
		}
		
		ArrayList<Photo> list = new ArrayList<Photo>();
		Tag t = new Tag(tagName1.getText(),tagValue1.getText());
		
		for(Album a: globalUser.getAlbumList()) {
			for(Photo p: a.getPhotoList()) {
				for(Tag t1: p.getTags()) {
					if(t1.equals(t)) {
						if(list.contains(p)) continue;
						list.add(p);
					}
				}
			}
		}
		
		if(list.size() == 0) {
			setAlert("No photos found!");
			return;
		}
		Stage tempwindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
		searchResult(list, tempwindow);
		
		
	}
	
	public void andTagSearch(ActionEvent e) throws IOException {
		
		if(tagName1.getText().length()==0 || tagValue1.getText().length()==0) {
			setAlert("Tag name 1 and Tag Value 1 cannot be empty!");
			return;
		}
		
		if(tagName2.getText().length()==0 || tagValue2.getText().length()==0) {
			setAlert("Tag name 2 and Tag Value 2 cannot be empty!");
			return;
		}
		
		ArrayList<Photo> list = new ArrayList<Photo>();
		Tag t = new Tag(tagName1.getText(),tagValue1.getText());
		Tag t2 =  new Tag(tagName2.getText(),tagValue2.getText());
		boolean flag1 = false;
		boolean flag2 = false;
		for(Album a: globalUser.getAlbumList()) {
			for(Photo p: a.getPhotoList()) {
				flag1 = false;
				flag2 = false;
				for(Tag t1: p.getTags()) {
					if(t1.equals(t)) {
						flag1 = true;
					}
					if(t1.equals(t2)) {
						flag2 = true;
					}
				}
				if(flag1 && flag2) {
					if(list.contains(p)) continue;
					list.add(p);
				}
				
			}
		}
		
		if(list.size() == 0) {
			setAlert("No photos found!");
			return;
		}
		Stage tempwindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
		searchResult(list, tempwindow);
		
	}
	
	public void orTagSearch(ActionEvent e) throws IOException {
		
		if(tagName1.getText().length()==0 || tagValue1.getText().length()==0) {
			setAlert("Tag name 1 and Tag Value 1 cannot be empty!");
			return;
		}
		
		if(tagName2.getText().length()==0 || tagValue2.getText().length()==0) {
			setAlert("Tag name 2 and Tag Value 2 cannot be empty!");
			return;
		}
		
		ArrayList<Photo> list = new ArrayList<Photo>();
		Tag t = new Tag(tagName1.getText(),tagValue1.getText());
		Tag t2 =  new Tag(tagName2.getText(),tagValue2.getText());
		boolean flag1 = false;
		boolean flag2 = false;
		for(Album a: globalUser.getAlbumList()) {
			for(Photo p: a.getPhotoList()) {
				flag1 = false;
				flag2 = false;
				for(Tag t1: p.getTags()) {
					if(t1.equals(t)) {
						flag1 = true;
					}
					if(t1.equals(t2)) {
						flag2 = true;
					}
				}
				if(flag1 || flag2) {
					if(list.contains(p)) continue;
					list.add(p);
				}
				
			}
		}
		
		if(list.size() == 0) {
			setAlert("No photos found!");
			return;
		}
		Stage tempwindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
		searchResult(list, tempwindow);
		
		
	}
	
	public void dateSearch(ActionEvent e) throws IOException {
		//startDate
		//endDate
		if(startDate.getValue()==null || endDate.getValue()==null) {
			setAlert("Start Date and End Date cannot be empty!");
			return;
		}
		
		// Check if end date is after start date
		
		LocalDate d = startDate.getValue();
		Date d1 = java.sql.Date.valueOf(d);
		d = endDate.getValue();
		Date d2 = java.sql.Date.valueOf(d);
		//Date d2
		if(d1.after(d2)) {
			setAlert("Start Date should be before the End Date!");
			return;
		}
		//d1.compareTo(d2<=0)
		
		ArrayList<Photo> list = new ArrayList<Photo>();
		String day1 = dateFormat.format(d1);
		String day2 = dateFormat.format(d2);
		for(Album a: globalUser.getAlbumList()) {
			for(Photo p: a.getPhotoList()) {
				if((p.getDate().compareTo(d2)<0 || dateFormat.format(p.getDate()).equals(day2)) && (p.getDate().compareTo(d1)>0 || dateFormat.format(p.getDate()).equals(day1))) {
					if(list.contains(p)) continue;
					list.add(p);
				}			
			}
		}
		
		if(list.size() == 0) {
			setAlert("No photos found!");
			return;
		}
		Stage tempwindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
		searchResult(list, tempwindow);
	}
	
	public void searchResult(ArrayList<Photo> list, Stage primaryWindow) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/searchResult.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		searchResultController controller = loader.getController();
		Stage window = new Stage();
		controller.startUp(globalUser,list,primaryWindow);
		Scene scene = new Scene(root);
		window.setTitle("Search Results");
		window.setScene(scene);
		window.setResizable(false);
		window.initModality(Modality.APPLICATION_MODAL);
		window.show();
		//sortList();
		//window.show();
	}
	
	public void displayDetails(Album a) {
		if(a==null) {
			albumName.setText("");
			numberOfPhotos.setText("");
			creationDate.setText("");
			modifiedDate.setText("");
			return;
		}
		albumName.setText(a.toString());
		//numberOfPhotos.setText(String.format("%d", a.getNumberOfPhotos()));
		numberOfPhotos.setText(String.format("%d", a.getPhotoList().size()));
		if(a.getCreateDate()==null && a.getLatestDate()==null) {
			creationDate.setText("Not Available");
			modifiedDate.setText("Not Available");
			return;
		}
		creationDate.setText(dateFormat.format(a.getCreateDate()));
		modifiedDate.setText(dateFormat.format(a.getLatestDate()));
	}
	
	public void logoff(ActionEvent e) throws IOException, ClassNotFoundException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		loginController controller = loader.getController();
		controller.shutdown();
		Scene scene = new Scene(root);
		Stage window = (Stage) myMenuBar.getScene().getWindow();
		window.setScene(scene);
		window.setTitle("Photos");
		window.show();
	}
	
	public void viewAlbum(ActionEvent e) throws IOException, ClassNotFoundException {
		Album album = albumList.getSelectionModel().getSelectedItem();
		if(album==null) {
			setAlert("Need to select an Album to View!");
			return;
		}
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		loginController controller = loader.getController();
		controller.shutdown();
		FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/view/photo.fxml"));
		root = (AnchorPane) loader2.load();
		photoController controller2 = loader2.getController();
		controller2.startUp(album,globalUser);
		Scene scene = new Scene(root);
		Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.setTitle("Photos");
		window.show();
	}

	public boolean checkDuplicate(String name, int index) {
		ObservableList<Album> tempList = albumList.getItems();
		for (int i = 0; i < tempList.size(); i++) {
			if(index==i) {
				continue;
			}
			if(name.equalsIgnoreCase(tempList.get(i).toString())) {
				return true;
			}
		}
		return false;
	}
	
	public void sortList() {
		class albumSorter implements Comparator<Album> {
			@Override
			public int compare(Album a, Album b) {
				return a.toString().compareToIgnoreCase(b.toString());
			}
		}
		ObservableList<Album> tempList = albumList.getItems();
		Collections.sort(tempList, new albumSorter());
	}
	
	public void updateList(Album a) {
		
	}
	
	public void setAlert(String alertString) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(alertString);
		alert.showAndWait();
	}
}
