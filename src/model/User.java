package model;

import java.io.Serializable;
import java.util.ArrayList;

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
	private ArrayList<Album> albumList;
	
	
	public User(String username) {
		this.setUsername(username);
		this.albumList = new ArrayList<Album>();
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

	public ArrayList<Album> getAlbumList() {
		return albumList;
	}
}