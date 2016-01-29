/**
 * 
 */
package edu.depaul.se491.models;

import java.util.ArrayList;
import java.util.List;

import edu.depaul.se491.beans.AccountBean;
import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.enums.AccountRole;
import edu.depaul.se491.exceptions.DBException;
import edu.depaul.se491.utils.ErrorCodes;
import edu.depaul.se491.validators.CredentialValidator;

/**
 * @author Malik
 *
 */
public abstract class BaseModel {
	private DAOFactory daoFactory;
	private List<String> messages;
	
	private CredentialsBean credentials;
	private AccountBean loggedinAccount;
	private boolean lookupAccount;
	
	
	protected BaseModel(DAOFactory daoFactory, CredentialsBean credentials) {
		this.daoFactory = daoFactory;
		this.messages = new ArrayList<>();
		
		CredentialValidator validator = new CredentialValidator();
		if (validator.validate(credentials)) {
			this.credentials = credentials;
			lookupAccount = true;
		} else {
			messages.addAll(validator.getValidationMessages());
		}
	}
	
	public String getErrorMessage() {
		StringBuilder sb = new StringBuilder();
		for(String msg: messages) {
			sb.append(msg); sb.append(NEW_LINE);
		}
		return sb.toString();
	}
	
	protected DAOFactory getDAOFactory() {
		return this.daoFactory;
	}
	
	protected AccountBean getLoggedinAccount() throws DBException {
		if (lookupAccount) {
			AccountBean existingAccount = daoFactory.getAccountDAO().get(credentials.getUsername());
			
			boolean accountFound = (existingAccount != null);
			if (accountFound) {
				
				String existingPassword = existingAccount.getCredentials().getPassword();

				if (credentials.getPassword().equals(existingPassword)) {
					loggedinAccount = existingAccount;
				} else {
					addErrorMessage(String.format("Wrong account password (password = %s)", credentials.getPassword()));
				}
			} else {
				addErrorMessage(String.format("No account found (username = %s)", credentials.getUsername()));
			}
			
			lookupAccount = false;
		}
		
		return loggedinAccount;
	}
	
	protected void addErrorMessage(String errorMsg) {
		messages.add(errorMsg);
	}
	
	protected void addErrorMessage(List<String> errorMsgList) {
		for (String msg: errorMsgList)
			messages.add(msg);
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
			if (!isValid)
				addErrorMessage(String.format("Access Denied (Error code %d)", ErrorCodes.Access.INSUFFICIENT_ROLE));
		}
		
		return isValid;
	}

	
	
	private static final String NEW_LINE = "\n";
	
}
