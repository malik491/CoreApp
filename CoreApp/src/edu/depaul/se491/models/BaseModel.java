/**
 * 
 */
package edu.depaul.se491.models;

import javax.ws.rs.core.Response.Status;

import edu.depaul.se491.beans.AccountBean;
import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.enums.AccountRole;
import edu.depaul.se491.exceptions.DBException;

/**
 * @author Malik
 *
 */
public abstract class BaseModel {
	private DAOFactory daoFactory;
	private String message;
	private Status responseStatus;
	
	private CredentialsBean credentials;
	private AccountBean loggedinAccount;
	private boolean lookupAccount;
	
	protected BaseModel(DAOFactory daoFactory, CredentialsBean credentials) {
		this.daoFactory = daoFactory;
		this.message = "";
		this.credentials = credentials;
		this.lookupAccount = true;
	}
	
	public String getResponseMessage() {
		return message;
	}
	
	public Status getResponseStatus() {
		return responseStatus;
	}
	
	protected void setResponseMessage(String message) {
		this.message = message;
	}
	
	protected void setResponseStatus(Status responseStatus) {
		this.responseStatus = responseStatus;
	}

	protected void setResponseAndMeessageForDBError() {
		this.responseStatus = Status.INTERNAL_SERVER_ERROR;
		this.message = GENERIC_SERVER_ERR_MSG;
	}
	
	protected DAOFactory getDAOFactory() {
		return this.daoFactory;
	}
	
	protected AccountBean getLoggedinAccount() {
		if (lookupAccount) {
			try {
				AccountBean existingAccount = daoFactory.getAccountDAO().get(credentials.getUsername());
				boolean accountFound = (existingAccount != null);
				if (accountFound) {				
					String existingPassword = existingAccount.getCredentials().getPassword();
					if (credentials.getPassword().equals(existingPassword)) {
						loggedinAccount = existingAccount;
					} else {
						responseStatus = Status.NOT_FOUND;
						message = "Incorrect Password (check password)";
					}
				} else {
					responseStatus = Status.NOT_FOUND;
					message = "Account Not Found (check username)";
				}	
			} catch (DBException e) {
				setResponseAndMeessageForDBError();
			}
			lookupAccount = false;
		}
		
		return loggedinAccount;
	}
	
	protected boolean hasPermission(AccountRole[] allowedRoles) {
		AccountBean loggedInAccount = getLoggedinAccount();
		
		boolean isValid = (loggedInAccount != null);
		if (isValid) {
			// must have permission (role)
			AccountRole loggedInRole = loggedInAccount.getRole();
			
			isValid = false;
			for (AccountRole role: allowedRoles) {
				isValid |= (loggedInRole == role);
			}	
			if (!isValid) {
				responseStatus = Status.UNAUTHORIZED;
				message = "Access Denied (unauthorized)";
			}
		}
		
		return isValid;
	}

	protected static final AccountRole ADMIN = AccountRole.ADMIN;
	protected static final AccountRole MANAGER = AccountRole.MANAGER;
	protected static final AccountRole EMPLOYEE = AccountRole.EMPLOYEE;
	protected static final AccountRole VENDOR = AccountRole.VENDOR;
	protected static final AccountRole CUSTOMER_APP = AccountRole.CUSTOMER_APP;

	private static final String GENERIC_SERVER_ERR_MSG = "Internal Server Error. Contact the site admin for details";
	
}
