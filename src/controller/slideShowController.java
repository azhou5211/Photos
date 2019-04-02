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
import javafx.geometry.Pos;
import javafx.scene.Node;
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

public class slideShowController {

	@FXML
	private Pagination pagination;

	public void startup(Album album) {
		pagination.setPageCount(album.getPhotoList().size());
		pagination.setPageFactory((Integer pageIndex) -> getImage(pageIndex, album.getPhotoList()));
	}

	public VBox getImage(int index, ArrayList<Photo> photoList) {
		VBox b = new VBox();
		b.setAlignment(Pos.CENTER);
		Photo p = photoList.get(index);
		ImageView imageView = new ImageView();
		File file = new File(p.getFilePath());
		try {
			@SuppressWarnings("unused")
			BufferedImage bufferedImage = ImageIO.read(file);
			Image image = new Image(file.toURI().toString());
			imageView.setImage(image);
			imageView.setPreserveRatio(true);
			if (image.getWidth() > image.getHeight()) {
				imageView.setFitWidth(718);
			} else {
				imageView.setFitHeight(409);
			}
			imageView.setSmooth(true);
			imageView.setCache(true);
		} catch (IOException ex) {

		}
		b.getChildren().add(imageView);
		return b;
	}
}
