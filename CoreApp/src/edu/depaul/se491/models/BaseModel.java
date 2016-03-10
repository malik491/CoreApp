package edu.depaul.se491.models;

import java.sql.SQLException;

import javax.ws.rs.core.Response.Status;

import edu.depaul.se491.beans.AccountBean;
import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.enums.AccountRole;
import edu.depaul.se491.validators.CredentialsValidator;

/**
 * Base Model
 * 
 * @author Malik
 */
class BaseModel {
	private DAOFactory daoFactory;
	private String message;
	private Status responseStatus;
	
	private CredentialsBean credentials;
	private AccountBean loggedinAccount;
	private boolean lookupAccount;
	
	BaseModel(DAOFactory daoFactory, CredentialsBean credentials) {
		this.daoFactory = daoFactory;
		this.message = "";
		this.credentials = credentials;
		this.lookupAccount = true;
	}
	
	/**
	 * return response message
	 * @return
	 */
	public String getResponseMessage() {
		return message;
	}
	
	/**
	 * return response status
	 * @return
	 */
	public Status getResponseStatus() {
		return responseStatus;
	}
	
	/**
	 * set response message
	 * @param message
	 */
	public void setResponseMessage(String message) {
		this.message = message;
	}
	
	/**
	 * set response status
	 * @param responseStatus
	 */
	public void setResponseStatus(Status responseStatus) {
		this.responseStatus = responseStatus;
	}

	/**
	 * set response message and status:
	 * status = Response.Status.INTERNAL_SERVER_ERROR
	 * message = a generic error message;
	 */
	public void setResponseAndMeessageForDBError(SQLException e) {
		if (e != null) {
			synchronized(System.err) {
				e.printStackTrace(System.err);
				for (Throwable t : e.getSuppressed()) {
					t.printStackTrace(System.err);
				}
			}
		}
		
		this.responseStatus = Status.INTERNAL_SERVER_ERROR;
		this.message = GENERIC_SERVER_ERR_MSG;
	}
	
	/**
	 * return DAO factory
	 * @return
	 */
	DAOFactory getDAOFactory() {
		return daoFactory;
	}
	
	/**
	 * return account associated with the given credentials (passed to the constructor) 
	 * or null if account is not found or password doesn't match
	 * @return
	 */
	AccountBean getLoggedinAccount() {
		if (lookupAccount && isValidCredentials(credentials)) {
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
			} catch (SQLException e) {
				setResponseAndMeessageForDBError(e);
			}
			lookupAccount = false;
		}
		
		return loggedinAccount;
	}

	/**
	 * return true of the account role for logged in account is in allowedRoles
	 * @param allowedRoles
	 * @return
	 */
	boolean hasPermission(AccountRole[] allowedRoles) {
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
	
	private boolean isValidCredentials(CredentialsBean credentials) {
		boolean isValid = new CredentialsValidator().validate(credentials);
		
		if (!isValid) {
			responseStatus = Status.BAD_REQUEST;
			message = "Invalid Credentials data";
		}
		return isValid;
	}

	public static final AccountRole ADMIN = AccountRole.ADMIN;
	public static final AccountRole MANAGER = AccountRole.MANAGER;
	public static final AccountRole EMPLOYEE = AccountRole.EMPLOYEE;
	public static final AccountRole CUSTOMER_APP = AccountRole.CUSTOMER_APP;

	static final String GENERIC_SERVER_ERR_MSG = "Internal Server Error. Contact the site admin for details";
	
}
