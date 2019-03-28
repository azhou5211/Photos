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
import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
	@FXML private TilePane tilepane;
	@FXML private Pagination pagination;
	@FXML private ListView<Tag> tagListView;
	@FXML private ListView<Album> albumListView;
	@FXML private TextField newTagName;
	@FXML private TextField newTagValue;
	@FXML private TextArea captionText;
	
	static DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	static User globalUser;
	static Album globalAlbum;
	static Photo currentPhoto;
	static VBox previousBox;
	
	public void addPhoto(ActionEvent e) throws InterruptedException {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Choose Photo File");
		File photoFile = chooser.showOpenDialog(new Stage());
		if(photoFile!=null) {
			Date photoDate = new Date(photoFile.lastModified());
			Photo photo = new Photo();
			photo.setDate(photoDate);
			photo.setFilePath(photoFile.getAbsolutePath());
			if(globalAlbum.getPhotoList().contains(photo)) {
				setAlert("Cannot add duplicate photo!");
				return;
			}
			globalAlbum.getPhotoList().add(photo);
			if(globalAlbum.getCreateDate()==null) {
				globalAlbum.setCreateDate(photoDate);
				globalAlbum.setLatestDate(photoDate);
			} else if (photoDate.compareTo(globalAlbum.getCreateDate()) < 0) {
				globalAlbum.setCreateDate(photoDate);
			} else if (photoDate.compareTo(globalAlbum.getLatestDate()) > 0) {
				globalAlbum.setLatestDate(photoDate);
			}
			
			
			tilepane.getChildren().add(getImage(photo));
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
	
	public void deletePhoto(ActionEvent e) {
		// Check tile selection
		if(currentPhoto==null) {
			setAlert("Must select a photo to delete!");
			return;
		}
		globalAlbum.getPhotoList().remove(currentPhoto);
		tilepane.getChildren().remove(previousBox);
		
		if(globalAlbum.getPhotoList().size()==0) {
			globalAlbum.setCreateDate(null);
			globalAlbum.setLatestDate(null);
		} else {
			Date minDate = getMinDate(globalAlbum);
			Date maxDate = getMaxDate(globalAlbum);
			globalAlbum.setCreateDate(minDate);
			globalAlbum.setLatestDate(maxDate);
		}
		
		captionText.setText("");
		tagListView.getItems().clear();
		currentPhoto = null;
		previousBox = null;
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
	
	public void addTag(ActionEvent e) {
		// check if tag name and value is filled
		String tagName = newTagName.getText();
		String tagValue = newTagValue.getText();
		if(tagName.length()==0 || tagValue.length()==0) {
			setAlert("Cannot have empty Tag Name or Tag Value!");
			return;
		}
		// check if a photo is selected in tile selection
		if(currentPhoto==null) {
			setAlert("No photo selected!");
			return;
		}
		// Check for duplicate tags
		Tag newTag = new Tag(tagName,tagValue);
		if(checkDuplicateTags(newTag)) {
			setAlert("Duplicate Tag!");
			return;
		}
		
		newTagName.setText("");
		newTagValue.setText("");
		currentPhoto.getTags().add(newTag);
		tagListView.getItems().add(newTag);
		sortTag();
	}
	
	public void deleteTag(ActionEvent e) {
		// check if a photo is selected in tile selection
		if(currentPhoto==null) {
			setAlert("No photo selected!");
			return;
		}
		
		// Check if tag is selected
		Tag tag = tagListView.getSelectionModel().getSelectedItem();
		if(tag==null) {
			setAlert("No tag selected!");
			return;
		}
		
		currentPhoto.getTags().remove(tag);
		tagListView.getItems().remove(tag);
		sortTag();
	}
	
	public boolean checkDuplicateTags(Tag tag) {
		ArrayList<Tag> tempList = currentPhoto.getTags();
		for (int i = 0; i < tempList.size(); i++) {
			if(tag.toString().equalsIgnoreCase(tempList.get(i).toString())) {
				return true;
			}
		}
		return false;
	}
	
	public void sortTag() {
		class tagComparator implements Comparator<Tag> {
			@Override
			public int compare(Tag a, Tag b) {
				return a.toString().compareToIgnoreCase(b.toString());
			}
		}
		Collections.sort(tagListView.getItems(), new tagComparator());
	}
	
	public void editCaption(ActionEvent e) {
		// check if a photo is selected
		if(currentPhoto==null) {
			setAlert("No photo selected!");
			return;
		}
		String newCaption = captionText.getText();
		currentPhoto.setCaption(newCaption);
		TextField t = (TextField)previousBox.getChildren().get(1);
		if(newCaption.length()==0) {
			t.setText("no caption");
		} else {
			t.setText(newCaption);
		}
	}
	
	public void copyPhoto(ActionEvent e) {
		// check if a photo is selected
		if(currentPhoto==null) {
			setAlert("No photo selected!");
			return;
		}
		// check if album is selected
		Album a = albumListView.getSelectionModel().getSelectedItem();
		if(a==null) {
			setAlert("No album selected!");
			return;
		}
		
		// check if the target album has duplicate
		if(a.getPhotoList().contains(currentPhoto)) {
			setAlert("Target Album already has photo!");
			return;
		}
		
		a.getPhotoList().add(currentPhoto);
		a.setCreateDate(getMinDate(a));
		a.setLatestDate(getMaxDate(a));
	}
	
	public void movePhoto(ActionEvent e) {
		// check if a photo is selected
		if(currentPhoto==null) {
			setAlert("No photo selected!");
			return;
		}
		// check if album is selected
		Album a = albumListView.getSelectionModel().getSelectedItem();
		if(a==null) {
			setAlert("No album selected!");
			return;
		}
		
		// check if the target album has duplicate
		if(a.getPhotoList().contains(currentPhoto)) {
			setAlert("Target Album already has photo!");
			return;
		}
		
		a.getPhotoList().add(currentPhoto);
		a.setCreateDate(getMinDate(a));
		a.setLatestDate(getMaxDate(a));
		
		deletePhoto(new ActionEvent());
		/*
		globalAlbum.getPhotoList().remove(currentPhoto);
		tilepane.getChildren().remove(previousBox);
		
		if(globalAlbum.getPhotoList().size()==0) {
			globalAlbum.setCreateDate(null);
			globalAlbum.setLatestDate(null);
		} else {
			Date minDate = getMinDate(globalAlbum);
			Date maxDate = getMaxDate(globalAlbum);
			globalAlbum.setCreateDate(minDate);
			globalAlbum.setLatestDate(maxDate);
		}
		
		captionText.setText("");
		tagListView.getItems().clear();
		currentPhoto = null;
		previousBox = null;
		*/
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
		globalAlbum = a;
		globalUser = u;
		
		// TODO Need to file in the tilepane and the pagination here!!
		tilepane.getChildren().clear();
		previousBox = null;
		currentPhoto = null;
		
		
		for(Album temp: globalUser.getAlbumList()) {
			if(!globalAlbum.equals(temp)) {
				albumListView.getItems().add(temp);
			}
		}
		class albumComparator implements Comparator<Album> {
			@Override
			public int compare(Album a, Album b) {
				return a.toString().compareToIgnoreCase(b.toString());
			}
		}
		Collections.sort(albumListView.getItems(), new albumComparator());
		
		ArrayList<Photo> photoList = globalAlbum.getPhotoList();
		for(Photo p: photoList) {
			tilepane.getChildren().add(getImage(p));
		}
		
		
	}
	
	public void showDetails(VBox vbox, Photo p) {
		if(previousBox != null) {
			previousBox.setStyle("");
		}
		previousBox = vbox;
		currentPhoto = p;
		vbox.setStyle("-fx-border-color: blue;\n" + "-fx-border-width: 2;\n");
		captionText.setText(p.getCaption());
		tagListView.getItems().clear();
		for(Tag t: p.getTags()) {
			tagListView.getItems().add(t);
		}
		sortTag();
	}
	
	public VBox getImage(Photo p) {
		ImageView imageView = new ImageView();
		File file = new File(p.getFilePath());
		try {
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
		b.setOnMouseClicked(e -> showDetails(b,p));
		return b;
	}	
}
