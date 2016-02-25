package edu.depaul.se491.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/***
 * Credentials Bean
 * 
 * @author Malik
 */
@XmlRootElement
public class CredentialsBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	
	/**
	 * construct CredentialsBean
	 */
	public CredentialsBean() {
	}
	
	/**
	 * construct CredentialsBean
	 * @param username
	 * @param password
	 */
	public CredentialsBean(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	/**
	 * return username
	 * @return
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * set username
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * return password
	 * @return
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * set password
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
