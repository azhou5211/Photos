package controller;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.activation.MimetypesFileTypeMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

/**
 * 
 * @author Andrew Zhou, Bang An
 *
 */

public class photoController {

	@FXML private MenuBar myMenuBar;
	
	static DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	static User globalUser;
	static Album globalAlbum;
	
	public void addPhoto(ActionEvent e) throws InterruptedException {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Choose Photo File");
		Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL); 
		//File photoFile = chooser.showOpenDialog(new Stage());
		File photoFile = chooser.showOpenDialog(dialog);
		if(photoFile!=null) {
			//System.out.println(photoFile.getAbsolutePath());
			//System.out.println(dateFormat.format(photoFile.lastModified()));
			Date photoDate = new Date(photoFile.lastModified());
			Photo photo = new Photo();
			photo.setDate(photoFile.lastModified());
			//System.out.println(dateFormat.format(photo.getDate()));
			globalAlbum.getPhotoList().add(photo);
			if(globalAlbum.getCreateDate()==null) {
				globalAlbum.setCreateDate(photoFile.lastModified());
				globalAlbum.setLatestDate(photoFile.lastModified());
			} else if (photoDate.compareTo(globalAlbum.getCreateDate()) < 0) {
				globalAlbum.setCreateDate(photoDate);
			} else if (photoDate.compareTo(globalAlbum.getLatestDate()) > 0) {
				globalAlbum.setLatestDate(photoDate);
			}
			/*
			String mimeType= new MimetypesFileTypeMap().getContentType(photoFile);
			if (mimeType.startsWith("image/")) {
				System.out.println("image");
			} else {
				setAlert("File is not an image!");
			}
			*/
			/*
			String mimetype= new MimetypesFileTypeMap().getContentType(photoFile);
			String fileType = mimetype.split("/")[0];
			if(fileType.equals("image")) {
				System.out.println("image");
			} else {
				setAlert("File is not an image!");
			}
			*/
		}
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
	
	public void toAlbum(ActionEvent e) throws IOException, ClassNotFoundException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		loginController controller = loader.getController();
		controller.shutdown();
		FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/view/album.fxml"));
		root = (AnchorPane) loader2.load();
		albumController controller2 = loader2.getController();
		controller2.setUp(globalUser);
		Scene scene = new Scene(root);
		Stage window = (Stage) myMenuBar.getScene().getWindow();
		window.setScene(scene);
		window.setTitle("Photos");
		window.show();
	}
	
	public void setAlert(String alertString) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(alertString);
		alert.showAndWait();
	}

	public void startUp(Album a, User u) {
		// TODO Auto-generated method stub
		globalAlbum = a;
		globalUser = u;
		
	}
	
}
