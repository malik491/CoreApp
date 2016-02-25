package edu.depaul.se491.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Request Bean
 * 
 * @author Malik
 *
 * @param <T>
 */
@XmlRootElement
public class RequestBean<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private CredentialsBean credentials;
	private T extra;
	
	/**
	 * construct RequestBean
	 */
	public RequestBean(){
	}
	
	/**
	 * construct RequestBean
	 * @param credentials
	 * @param extra
	 */
	public RequestBean(CredentialsBean credentials, T extra) {
		this.credentials = credentials;
		this.extra = extra;
	}
	
	/**
	 * return credentials
	 * @return
	 */
	public CredentialsBean getCredentials() {
		return credentials;
	}

	/**
	 * set credentials
	 * @param credentials
	 */
	public void setCredentials(CredentialsBean credentials) {
		this.credentials = credentials;
	}

	/**
	 * return extra object
	 * @return
	 */
	public T getExtra() {
		return extra;
	}

	/**
	 * set extra object
	 * @param extra
	 */
	public void setExtra(T extra) {
		this.extra = extra;
	}
	
	
	
}
