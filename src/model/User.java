package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 
 * @author Andrew Zhou
 * @author Bang An
 */

public class User implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String username;
	
	
	
	public User(String username) {
		this.setUsername(username);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String toString() {
		return this.username;
	}
	/*
	public static void main() throws IOException {
		User admin = new User("admin");
		User stock = new User("stock");
		
		File f = new File("users.txt");
		FileOutputStream fos = new FileOutputStream(f);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(admin);
		oos.writeObject(stock);
		oos.close();
	}
	*/
}