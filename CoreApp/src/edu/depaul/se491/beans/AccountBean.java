package edu.depaul.se491.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import edu.depaul.se491.enums.AccountRole;

/**
 * Account Bean
 * 
 * @author Malik
 */
@XmlRootElement
public class AccountBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private CredentialsBean credentials;
	private UserBean user;
	private AccountRole role;
	

	/**
	 * construct AccountBean
	 */
	public AccountBean() {
	}
	
	/**
	 * construct AccountBean
	 * @param credentials
	 * @param user
	 * @param role
	 */
	public AccountBean(CredentialsBean credentials, UserBean user, AccountRole role) {
		this.setCredentials(credentials);
		this.user = user;
		this.role = role;
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
	 * return user
	 * @return
	 */
	public UserBean getUser() {
		return user;
	}

	/**
	 * set user
	 * @param user
	 */
	public void setUser(UserBean user) {
		this.user = user;
	}

	/**
	 * return account role
	 * @return
	 */
	public AccountRole getRole() {
		return role;
	}

	/**
	 * set account role
	 * @param role
	 */
	public void setRole(AccountRole role) {
		this.role = role;
	}
	
}
