package model;

import java.io.Serializable;

/**
 * 
 * @author Andrew Zhou
 * @author Bang An
 */

public class Tag implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tagName;
	private String tagValue;
	
	public Tag(String tagName, String tagValue) {
		this.tagName = tagName;
		this.tagValue = tagValue;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTagValue() {
		return tagValue;
	}

	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}
	
	public String toString() {
		return tagName + "=" + tagValue;
	}
}