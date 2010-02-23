package be.fedict.eid.blm.mail;

import java.io.Serializable;

public class Attachment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1744155645400679607L;

	private String fileName;
	private String value;

	/**
	 * 
	 * @param fileName the filename
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 
	 * @return the filename
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * 
	 * @param value the value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
}
