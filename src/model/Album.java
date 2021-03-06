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
		setCreateDate(null);
		setLatestDate(null);
	}
	
	public int getNumberOfPhotos() {
		return numberOfPhotos;
	}
	
	public String toString() {
		return this.albumName;
	}
	
	public boolean equals(Object o) {
		if(o==null || (!(o instanceof Album))) {
			return false;
		}
		Album t = (Album)o;
		return this.toString().equalsIgnoreCase(t.toString());
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
	
	public void setCreateDate(long createDate) {
		this.createDate = new Date(createDate);
	}

	public Date getLatestDate() {
		return latestDate;
	}

	public void setLatestDate(Date latestDate) {
		this.latestDate = latestDate;
	}
	
	public void setLatestDate(long latestDate) {
		this.latestDate = new Date(latestDate);
	}
	
	public void addPhoto(Photo p) {
		this.photoList.add(p);
	}
}