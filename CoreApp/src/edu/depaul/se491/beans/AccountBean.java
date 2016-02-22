/**
 * Account Bean
 */
package edu.depaul.se491.beans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import edu.depaul.se491.enums.AccountRole;

/**
 * @author Malik
 *
 */
@XmlRootElement
public class AccountBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private CredentialsBean credentials;
	private UserBean user;
	private AccountRole role;
	

	/**
	 * construct an empty Account
	 */
	public AccountBean() {
	}
	
	public AccountBean(CredentialsBean credentials, UserBean user, AccountRole role) {
		this.setCredentials(credentials);
		this.user = user;
		this.role = role;
	}
	

	public CredentialsBean getCredentials() {
		return credentials;
	}
	

	public void setCredentials(CredentialsBean credentials) {
		this.credentials = credentials;
	}
	

	/**
	 * return user associated with this account
	 * @return
	 */
	public UserBean getUser() {
		return user;
	}

	/**
	 * set user associated with this account
	 * @param user
	 */
	public void setUser(UserBean user) {
		this.user = user;
	}

	/**
	 * return role associated with this account
	 * @return
	 */
	public AccountRole getRole() {
		return role;
	}

	/**
	 * set a role for this account
	 * @param role
	 */
	public void setRole(AccountRole role) {
		this.role = role;
	}
	
}
