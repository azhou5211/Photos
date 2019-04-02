package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import javax.imageio.ImageIO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.*;

/**
 * 
 * @author Andrew Zhou, Bang An
 *
 */

public class searchResultController {
	
	@FXML private TextField newAlbumName;
	@FXML private TilePane tilepane;
	
	static DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	static User globalUser;
	static ArrayList<Photo> globalPhotoList;
	static Stage parentWindow;
	
	public void startUp(User u, ArrayList<Photo> list, Stage primaryStage) {
		globalUser = u;
		globalPhotoList = list;
		parentWindow = primaryStage;
		
		tilepane.getChildren().clear();
		
		for(Photo p: list) {
			tilepane.getChildren().add(getImage(p));
		}
	}
	
	public VBox getImage(Photo p) {
		ImageView imageView = new ImageView();
		File file = new File(p.getFilePath());
		try {
			@SuppressWarnings("unused")
			BufferedImage bufferedImage = ImageIO.read(file);
			Image image = new Image(file.toURI().toString());
			imageView.setImage(image);
			imageView.setFitWidth(100);
			imageView.setFitHeight(100);
			imageView.setSmooth(true);
			imageView.setCache(true);
		} catch (IOException ex) {
			
		}
		
		VBox b = new VBox();
		b.getChildren().add(imageView);
		TextField t = new TextField();
		t.setEditable(false);
		t.setMaxWidth(100);
		if(p.getCaption().length()==0) {
			t.setText("no caption");
			b.getChildren().add(t);
		} else {
			t.setText(p.getCaption());
			b.getChildren().add(t);
		}
		b.getChildren().add(new Text(dateFormat.format(p.getDate())));
		return b;
	}
	
	public void addAlbum(ActionEvent e) throws IOException {
		ArrayList<Album> albumList = globalUser.getAlbumList();
		String albumName = newAlbumName.getText();
		if(albumName.length()==0) {
			setAlert("Must have Album Name!");
			return;
		}
		if(checkDuplicate(albumName,albumList)) {
			setAlert("Cannot have duplicate Album Name!");
			return;
		}
		Album a = new Album(albumName);
		for(Photo p: globalPhotoList) {
			a.addPhoto(p);
		}
		
		a.setCreateDate(getMinDate(a));
		a.setLatestDate(getMaxDate(a));
		
		globalUser.getAlbumList().add(a);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/album.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		albumController controller = loader.getController();
		controller.setUp(globalUser);
		Scene scene = new Scene(root);
		parentWindow.setScene(scene);
		parentWindow.setTitle("Photos");
		parentWindow.show();
	}
	
	class dateComparator implements Comparator<Photo> {
		@Override
		public int compare(Photo a, Photo b) {
			return a.getDate().compareTo(b.getDate());
		}
	}
	public Date getMaxDate(Album a) {
		Photo d = Collections.max(a.getPhotoList(), new dateComparator());
		return d.getDate();
	}
	
	public Date getMinDate(Album a) {
		Photo d = Collections.min(a.getPhotoList(), new dateComparator());
		return d.getDate();
	}
	
	public boolean checkDuplicate(String name, ArrayList<Album> albumList) {
		for (int i = 0; i < albumList.size(); i++) {
			if(name.equalsIgnoreCase(albumList.get(i).toString())) {
				return true;
			}
		}
		return false;
	}
	
	public void setAlert(String alertString) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(alertString);
		alert.showAndWait();
	}
}
