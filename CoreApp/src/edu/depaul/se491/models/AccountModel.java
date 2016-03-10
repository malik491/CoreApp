package edu.depaul.se491.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import edu.depaul.se491.beans.AccountBean;
import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.daos.mysql.AccountDAO;
import edu.depaul.se491.enums.AccountRole;
import edu.depaul.se491.validators.AccountValidator;
import edu.depaul.se491.validators.AddressValidator;
import edu.depaul.se491.validators.CredentialsValidator;
import edu.depaul.se491.validators.UserValidator;

/**
 * Account Model
 * 
 * @author Malik
 */
public class AccountModel extends BaseModel {
	
	/**
	 * construct AccountModel
	 * @param daoFactory DAO factory
	 * @param credentials of the current model user
	 */
	public AccountModel(DAOFactory daoFactory, CredentialsBean credentials) {
		super(daoFactory, credentials);
	}
	
	/**
	 * create new account 
	 * @param bean
	 * @return newly created account or null
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
				} catch (SQLException e) {
					setResponseAndMeessageForDBError(e);
				}
			}
		}
		return createdAccount;
	}

	/**
	 * update account
	 * @param bean
	 * @return
	 */
	public Boolean update(AccountBean bean) {
		AccountRole[] allowedRoles = new AccountRole[] {ADMIN, MANAGER, EMPLOYEE};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidBean(bean, false) : false;
		
		AccountBean oldAccount = null;
		if (isValid) {
			isValid = false;
			
			oldAccount = read(bean.getCredentials().getUsername());
			isValid = (oldAccount != null);
				
			if (isValid) {
				// updated user & address must have same id as the save ones
				isValid  = bean.getUser().getId() == oldAccount.getUser().getId();
				isValid &= bean.getUser().getAddress().getId() == oldAccount.getUser().getAddress().getId();
				
				if (!isValid) {
					setResponseStatus(Status.BAD_REQUEST);
					setResponseMessage("Invalid account data (user id or address id)");
				}
			}
		}
		
		if (isValid) {
			AccountBean loggedInAccount = getLoggedinAccount();
			
			String oldAccountUsername = oldAccount.getCredentials().getUsername();
			String loggedInUsername = loggedInAccount.getCredentials().getUsername();
			
			boolean isSelf = loggedInUsername.equalsIgnoreCase(oldAccountUsername);
			
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
			} catch (SQLException e) {
				setResponseAndMeessageForDBError(e);
			}
		}
		
		return updated;
	}
	
	/**
	 * return account with the given username or null
	 * @param username
	 * @return
	 */
	public AccountBean read(final String username) {
		AccountRole[] allowedRoles = new AccountRole[] {ADMIN, MANAGER, EMPLOYEE};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidUsername(username) : false;
		
		AccountBean account = null;
		if (isValid) {
			AccountBean loggedInAccount = getLoggedinAccount();
			AccountRole loggedInAs = loggedInAccount.getRole();

			String loggedInUsername = loggedInAccount.getCredentials().getUsername();
			
			boolean isSelf = loggedInUsername.equalsIgnoreCase(username);
			
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
				} catch (SQLException e) {
					setResponseAndMeessageForDBError(e);
				}
			} else {
				setResponseStatus(Status.UNAUTHORIZED);
				setResponseMessage("Access Denied (unauthorized)");
			}
		}
		return account;
	}
	
	/**
	 * delete account with the given username
	 * @param username
	 * @return
	 */
	public Boolean delete(String username) {
		AccountRole[] allowedRoles = new AccountRole[] {ADMIN, MANAGER};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidUsername(username) : false;

		AccountRole loggedInAs = null;
		if (isValid) {
			AccountBean loggedInAccount = getLoggedinAccount();	
			loggedInAs = loggedInAccount.getRole();
			
			String loggedInUsername = loggedInAccount.getCredentials().getUsername();		
			boolean isSelf = loggedInUsername.equalsIgnoreCase(username);
			
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
			} catch (SQLException e) {
				setResponseAndMeessageForDBError(e);
			}
		}
		
		return deleted;
	}
	
	/**
	 * return all accounts
	 * @return
	 */
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
				viewableRoles = new AccountRole[] {MANAGER, EMPLOYEE, CUSTOMER_APP};
			} else if (loggedInAs == MANAGER) {
				// employee accounts only
				viewableRoles = new AccountRole[] {EMPLOYEE};
			}
			
			try {
				for (AccountRole role: viewableRoles) {
					accounts.addAll(getDAOFactory().getAccountDAO().getAllByRole(role));
				}
			} catch (SQLException e) {
				setResponseAndMeessageForDBError(e);
			}
		}
		
		return accounts;
	}
	
	private boolean isValidUsername(String username){
		CredentialsValidator credentialValidator = new CredentialsValidator();
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
			isValid = new CredentialsValidator().validate(bean.getCredentials());
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
	 * return true/false solely based on account role
	 * Logged in as:
	 * 		Admin	: can create admin, manager, or customer_app account
	 *  	Manager	: can create employee accounts
	 *  	all other roles: not allowed to create accounts 
	 * @param loggedInAs
	 * @param toBeUpdatedHasRole
	 * @return
	 */
	private boolean canCreateOtherAccount(AccountRole loggedInAs, AccountRole toBeCreatedHasRole) {
		String message = "";
		
		boolean isValid = false;
		
		if (loggedInAs == ADMIN) {
		
			// admin can create manager, or customer_app account 
			isValid = (toBeCreatedHasRole == MANAGER || toBeCreatedHasRole == EMPLOYEE || toBeCreatedHasRole == CUSTOMER_APP);
			if (!isValid)
				message = String.format("Access Denied (Admin cannot create accounts with '%s' role)", toBeCreatedHasRole);  
		
		} else if (loggedInAs == MANAGER) {
			// manager can create employee accounts
		
			isValid = (toBeCreatedHasRole == EMPLOYEE);
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
	 *  	Manager	: can read employee accounts
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
		
			isValid = (toBeReadHasRole == EMPLOYEE);
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
	 *  	Manager	: can delete employee accounts
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
			// manager can delete employee accounts
		
			isValid = (toBeDeleteHasRole == EMPLOYEE);
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
	 *  	Manager	: can delete employee accounts
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
			
			isValid = (toBeUpdatedHasRole == EMPLOYEE);
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
