package controller;

/**
 * 
 * @author Andrew Zhou, Bang An
 *
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class loginController {

	@FXML private TextField username;
	
	public void signIn(ActionEvent e) {
		String test = username.getText();
		System.out.println(test);
	}
}
