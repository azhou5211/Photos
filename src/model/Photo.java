package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * 
 * @author Andrew Zhou
 * @author Bang An
 */

public class Photo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Tag> tags;
	private String caption;
	private String thumbnail;
	private String filePath;
	private Date date;
	
	public Photo() {
		this.setTags(new ArrayList<Tag>());
		this.setCaption("");
		setThumbnail(null);
		setDate(new Date());
		this.setFilePath("");
	}

	public ArrayList<Tag> getTags() {
		return tags;
	}

	public void setTags(ArrayList<Tag> tags) {
		this.tags = tags;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setDate(long date) {
		this.date = new Date(date);
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}