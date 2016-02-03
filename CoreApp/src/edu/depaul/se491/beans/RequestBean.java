package edu.depaul.se491.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RequestBean<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private CredentialsBean credentials;
	private T extra;
	

	public RequestBean(){
	}
	
	public RequestBean(CredentialsBean credentials, T extra) {
		this.credentials = credentials;
		this.extra = extra;
	}
	
	public CredentialsBean getCredentials() {
		return credentials;
	}

	public void setCredentials(CredentialsBean credentials) {
		this.credentials = credentials;
	}

	public T getExtra() {
		return extra;
	}

	public void setExtra(T extra) {
		this.extra = extra;
	}
	
	
	
}
