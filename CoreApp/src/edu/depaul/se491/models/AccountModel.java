/**
 * Account Model
 * 
 * Class to manipulate Accounts (create, update, etc)
 */
package edu.depaul.se491.models;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import edu.depaul.se491.beans.AccountBean;
import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.daos.ProductionDAOFactory;
import edu.depaul.se491.daos.mysql.AccountDAO;
import edu.depaul.se491.enums.AccountRole;
import edu.depaul.se491.exceptions.DBException;
import edu.depaul.se491.validators.AccountValidator;
import edu.depaul.se491.validators.AddressValidator;
import edu.depaul.se491.validators.CredentialValidator;
import edu.depaul.se491.validators.UserValidator;

/**
 * @author Malik
 *
 */
public class AccountModel extends BaseModel {
	
	public AccountModel(CredentialsBean credentials) {
		super(ProductionDAOFactory.getInstance(), credentials);
	}
	
	public AccountModel(DAOFactory daoFactory, CredentialsBean credentials) {
		super(daoFactory, credentials);
	}
	
	/**
	 * create a new account and return it or null
	 * @param bean
	 * @return
	 * @throws DBException
	 */
	public AccountBean create(AccountBean bean) {
		AccountRole[] allowedRoles = new AccountRole[] {ADMIN, MANAGER};
		boolean isValid = hasPermission(allowedRoles);

		isValid = isValid? isValidBean(bean, true) : false;
		
		AccountBean createdAccount = null;
		if (isValid) {
			AccountRole loggedInAs = getLoggedinAccount().getRole();
			isValid = canCreateOtherAccount(loggedInAs,  bean.getRole()); 
			
			if (isValid) {
				try {
					AccountDAO dao = getDAOFactory().getAccountDAO();
					
					AccountBean accountWithSameUsername = dao.get(bean.getCredentials().getUsername());
					if (accountWithSameUsername != null) {
						setResponseStatus(Status.BAD_REQUEST);
						setResponseMessage("Account username is already taken (try a different username)");
					} else {
						createdAccount = getDAOFactory().getAccountDAO().add(bean);						
					}
				} catch (DBException e) {
					setResponseAndMeessageForDBError();
				}
			}
		}
		return createdAccount;
	}

	public Boolean update(AccountBean bean) {
		AccountRole[] allowedRoles = new AccountRole[] {ADMIN, MANAGER, EMPLOYEE, VENDOR};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidBean(bean, false) : false;
		
		AccountBean oldAccount = null;
		if (isValid) {
			isValid = false;
			try {
				oldAccount = getDAOFactory().getAccountDAO().get(bean.getCredentials().getUsername());

				// updated user & address must have same id as the save ones
				isValid  = bean.getUser().getId() == oldAccount.getUser().getId();
				isValid &= bean.getUser().getAddress().getId() == oldAccount.getUser().getAddress().getId();
	
				if (!isValid) {
					setResponseStatus(Status.BAD_REQUEST);
					setResponseMessage("Invalid user id or address id");
				}
			} catch (DBException e) {
				setResponseAndMeessageForDBError();
			}
						
		}
		
		if (isValid) {
			AccountBean loggedInAccount = getLoggedinAccount();
			
			String oldAccountUsername = oldAccount.getCredentials().getUsername();
			String loggedInUsername = loggedInAccount.getCredentials().getUsername();
			
			boolean isSelf = loggedInUsername.equals(oldAccountUsername);
			
			if (isSelf) {
				// can't change self role
				isValid = loggedInAccount.getRole() == oldAccount.getRole();
				if (!isValid) {
					setResponseStatus(Status.UNAUTHORIZED);
					setResponseMessage("Access Denied (cannot change own account role)");
				}
			} else {
				// admin and manager can update other accounts
				AccountRole loggedInAs = loggedInAccount.getRole();
				isValid = canUpdateOtherAccount(loggedInAs, oldAccount.getRole());
			}
		}
		
		Boolean updated = null;
		if (isValid) {
			try {
				updated = getDAOFactory().getAccountDAO().update(bean);
			} catch (DBException e) {
				setResponseAndMeessageForDBError();
			}
		}
		
		return updated;
	}
	
	
	public AccountBean read(final String username) {
		AccountRole[] allowedRoles = new AccountRole[] {ADMIN, MANAGER, EMPLOYEE, VENDOR};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidUsername(username) : false;
		
		AccountBean account = null;
		if (isValid) {
			AccountBean loggedInAccount = getLoggedinAccount();
			AccountRole loggedInAs = loggedInAccount.getRole();

			String loggedInUsername = loggedInAccount.getCredentials().getUsername();
			
			boolean isSelf = loggedInUsername.equals(username);
			
			if (isSelf) {
				account = loggedInAccount;
			} else if (loggedInAs == ADMIN || loggedInAs == MANAGER) {
				try {
					account = getDAOFactory().getAccountDAO().get(username);
					isValid = (account != null);
					
					if (!isValid) {
						setResponseStatus(Status.NOT_FOUND);
						setResponseMessage("Account Not Found");
					} else {
						boolean canRead = canReadOtherAccount(loggedInAs, account.getRole());
						if (!canRead) {
							account = null;
						}
					}
				} catch (DBException e) {
					setResponseAndMeessageForDBError();
				}
			} else {
				setResponseStatus(Status.UNAUTHORIZED);
				setResponseMessage("Access Denied (unauthorized)");
			}
		}
		return account;
	}
	

	public Boolean delete(String username) {
		AccountRole[] allowedRoles = new AccountRole[] {ADMIN, MANAGER};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidUsername(username) : false;

		AccountRole loggedInAs = null;
		if (isValid) {
			AccountBean loggedInAccount = getLoggedinAccount();	
			loggedInAs = loggedInAccount.getRole();
			
			String loggedInUsername = loggedInAccount.getCredentials().getUsername();		
			boolean isSelf = loggedInUsername.equals(username);
			
			isValid = (!isSelf);
			if (!isValid) {
				setResponseStatus(Status.UNAUTHORIZED);
				setResponseMessage("Cannot delete own account");
			}
		}

		Boolean deleted = null;
		if (isValid) {
			// lookup the to-be-deleted account
			try {
				AccountBean oldAccount = getDAOFactory().getAccountDAO().get(username);
				isValid = (oldAccount != null);

				if (!isValid) {
					setResponseStatus(Status.NOT_FOUND);
					setResponseMessage("Account Not Found");
				} else {
					boolean canDelete = canDeleteOtherAccount(loggedInAs,  oldAccount.getRole());
					if (canDelete)
						deleted = getDAOFactory().getAccountDAO().delete(username);		
				}				
			} catch (DBException e) {
				setResponseAndMeessageForDBError();
			}
		}
		
		return deleted;
	}
	
	public List<AccountBean> readAll() {
		AccountRole[] allowedRoles = new AccountRole[] {ADMIN, MANAGER};
		boolean isValid = hasPermission(allowedRoles);
		
		List<AccountBean> accounts =  null;
		if (isValid) {
			accounts = new ArrayList<>();
			
			AccountRole loggedInAs = getLoggedinAccount().getRole();	
			AccountRole[] viewableRoles = null;

			if (loggedInAs == ADMIN) {
				// all accounts but admin accounts
				viewableRoles = new AccountRole[] {MANAGER, EMPLOYEE, VENDOR, CUSTOMER_APP};
			} else if (loggedInAs == MANAGER) {
				// employee or vendors accounts only
				viewableRoles = new AccountRole[] {EMPLOYEE, VENDOR};
			} else {
				viewableRoles = new AccountRole[0];
			}
			
			try {
				for (AccountRole role: viewableRoles) {
					accounts.addAll(getDAOFactory().getAccountDAO().getAllByRole(role));
				}
			} catch (DBException e) {
				setResponseAndMeessageForDBError();
			}
		}
		
		return accounts;
	}
	
	
	private boolean isValidUsername(String username){
		CredentialValidator credentialValidator = new CredentialValidator();
		boolean isValid = credentialValidator.isValidUsername(username);
		
		if (!isValid) {
			setResponseStatus(Status.BAD_REQUEST);
			setResponseMessage("Invalid username (check username)");
		}
		
		return isValid;
	}
	
	private boolean isValidBean(AccountBean bean, boolean isNewAccount) {
		String message = null;
		
		// validate account bean
		boolean isValid = new AccountValidator().validate(bean);
		if (!isValid)
			message = "Invalid account data";
		
		// validate credentials bean for the account
		if (isValid) {
			isValid = new CredentialValidator().validate(bean.getCredentials());
			if (!isValid)
				message = "Invalid credentials data";
		}
		
		// validate user bean
		if (isValid) {
			boolean isNewUser = isNewAccount;
			isValid = new UserValidator().validate(bean.getUser(), isNewUser);
			if (!isValid)
				message = "Invalid user data";
		}
		
		// validate address bean
		if (isValid) {
			boolean isNewAddress = isNewAccount;
			isValid = new AddressValidator().validate(bean.getUser().getAddress(), isNewAddress);
			if (!isValid)
				message = "Invalid address data";
				
		}
		
		if (!isValid) {
			setResponseStatus(Status.BAD_REQUEST);
			setResponseMessage(message);
		}
		
		return isValid;
	}
	
	/**
	 * return true/false solely base on account role
	 * Logged in as:
	 * 		Admin	: can create admin, manager, or customer_app account
	 *  	Manager	: can create employee or vendor accounts
	 *  	all other roles: not allowed to created accounts 
	 * @param loggedInAs
	 * @param toBeUpdatedHasRole
	 * @return
	 */
	private boolean canCreateOtherAccount(AccountRole loggedInAs, AccountRole toBeCreatedHasRole) {
		String message = "";
		
		boolean isValid = false;
		
		if (loggedInAs == ADMIN) {
		
			// admin can create admin, manager, or customer_app account 
			isValid = (toBeCreatedHasRole == ADMIN || toBeCreatedHasRole == MANAGER || toBeCreatedHasRole == CUSTOMER_APP);
			if (!isValid)
				message = String.format("Access Denied (Admin cannot create accounts with '%s' role)", toBeCreatedHasRole);  
		
		} else if (loggedInAs == MANAGER) {
			// manager can create employee or vendor accounts
		
			isValid = (toBeCreatedHasRole == EMPLOYEE || toBeCreatedHasRole == VENDOR);
			if (!isValid)
				message = String.format("Access Denied (Manager cannot create accounts with '%s' role)", toBeCreatedHasRole);  
		
		} else {
			message = "Access Denied (unauthorized)";
		}
		
		if (!isValid) {
			setResponseStatus(Status.UNAUTHORIZED);
			setResponseMessage(message);
		}
		
		
		return isValid;
	}
	
	

	/**
	 * return true/false solely base on account role
	 * Logged in as:
	 * 		Admin	: can read all none admin accounts
	 *  	Manager	: can read employee or vendor accounts
	 *  	all other roles:
	 * 				  returns false because role is not enough
	 * 				  to determine read permission.
	 *  
	 * @param loggedInAs the role associated with the current caller
	 * @param toBeReadHasRole the account to be viewed has role
	 * @return
	 */
	private boolean canReadOtherAccount(AccountRole loggedInAs, AccountRole toBeReadHasRole) {
		String message = "";
		
		boolean isValid = false;
		
		if (loggedInAs == ADMIN) {
			// admin can't read other admin's account
		
			isValid = toBeReadHasRole != ADMIN;
			if (!isValid)
				message = String.format("Access Denied (Admin cannot view '%s' accounts", toBeReadHasRole) ;
		
		} else if (loggedInAs == MANAGER) {
		
			isValid = (toBeReadHasRole == EMPLOYEE || toBeReadHasRole == VENDOR);
			if (!isValid)
				message = String.format("Access Denied (Manager cannot view '%s' accounts", toBeReadHasRole) ;
		
		}  else {
			message = "Access Denied (unauthorized)";
		}
		
		if (!isValid) {
			setResponseStatus(Status.UNAUTHORIZED);
			setResponseMessage(message);
		}
		
		return isValid;		
	}
	
	/**
	 * return true/false solely base on account role
	 * Logged in as:
	 * 		Admin	: can delete all none admin accounts
	 *  	Manager	: can delete employee or vendor accounts
	 *  	all other roles: can't delete any account  
	 * @param loggedInAs
	 * @param toBeDeleteHasRole
	 * @return
	 */
	private boolean canDeleteOtherAccount(AccountRole loggedInAs, AccountRole toBeDeleteHasRole) {
		String message = "";
		
		boolean isValid = false;
		
		if (loggedInAs == ADMIN) {
			// admin can't delete other admin account but can delete every thing else
			
			isValid = (toBeDeleteHasRole != ADMIN);
			if (!isValid)
				message = String.format("Access Denied (Admin cannot delete '%s' accounts", toBeDeleteHasRole) ;
		
		} else if (loggedInAs == MANAGER) {
			// manager can delete employee or vendor accounts
		
			isValid = (toBeDeleteHasRole == EMPLOYEE || toBeDeleteHasRole == VENDOR);
			if (!isValid)
				message = String.format("Access Denied (Manager cannot delete '%s' accounts", toBeDeleteHasRole) ;
		
		} else {
			message = "Access Denied (unauthorized)";
		}
		
		if (!isValid) {
			setResponseStatus(Status.UNAUTHORIZED);
			setResponseMessage(message);
		}
		
		return isValid;
	}
	
	/**
	 * return true/false solely base on account role
	 * Logged in as:
	 * 		Admin	: can delete all none admin accounts
	 *  	Manager	: can delete employee or vendor accounts
	 *  	all other roles: can't delete any account
	 * @param loggedInAs
	 * @param toBeUpdatedHasRole
	 * @return
	 */
	private boolean canUpdateOtherAccount(AccountRole loggedInAs, AccountRole toBeUpdatedHasRole) {
		String message = "";
		
		boolean isValid = false;
		
		if (loggedInAs == ADMIN) {
			// admin can't update other admin's account
		
			isValid = (toBeUpdatedHasRole != ADMIN);
			if (!isValid)
				message = String.format("Access Denied (Admin cannot update '%s' accounts", toBeUpdatedHasRole);
			
		} else if (loggedInAs == MANAGER) {
			
			isValid = (toBeUpdatedHasRole == EMPLOYEE || toBeUpdatedHasRole == VENDOR);
			if (!isValid)
				message = String.format("Access Denied (Manager cannot update '%s' accounts", toBeUpdatedHasRole);
		} else {
			message = "Access Denied (unauthorized)";
		}
		
		if (!isValid) {
			setResponseStatus(Status.UNAUTHORIZED);
			setResponseMessage(message);
		}
		
		return isValid;
	}
	
}
