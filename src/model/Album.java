package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * 
 * @author Andrew Zhou
 * @author Bang An
 */

public class Album implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String albumName;
	private Date createDate;
	private Date latestDate;
	private int numberOfPhotos;
	private ArrayList<Photo> photoList;
	
	
	public Album(String name) {
		this.albumName = name;
		this.photoList = new ArrayList<Photo>();
		this.numberOfPhotos = 0;
		setCreateDate(new Date());
		setLatestDate(new Date());
	}
	
	public int getNumberOfPhotos() {
		return numberOfPhotos;
	}
	
	public String toString() {
		return this.albumName;
	}

	public ArrayList<Photo> getPhotoList() {
		return photoList;
	}
	
	public void setAlbumName(String name) {
		this.albumName = name;
	}

	public void setPhotoList(ArrayList<Photo> photoList) {
		this.photoList = photoList;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLatestDate() {
		return latestDate;
	}

	public void setLatestDate(Date latestDate) {
		this.latestDate = latestDate;
	}
}