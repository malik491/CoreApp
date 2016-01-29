/**
 * Account Model
 * 
 * Class to manipulate Accounts (create, update, etc)
 */
package edu.depaul.se491.models;

import java.util.ArrayList;
import java.util.List;

import edu.depaul.se491.beans.AccountBean;
import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.daos.ProductionDAOFactory;
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
	public AccountBean create(AccountBean bean) throws DBException {
		AccountRole[] allowedRoles = new AccountRole[] {ADMIN, MANAGER};
		boolean isValid = hasPermission(allowedRoles);

		isValid = isValid? isValidBean(bean, true) : false;
		
		AccountBean createdAccount = null;
		if (isValid) {
			AccountRole loggedInAs = getLoggedinAccount().getRole();
			if (canCreateOtherAccount(loggedInAs,  bean.getRole()))
				createdAccount = getDAOFactory().getAccountDAO().add(bean);
		}
		return createdAccount;
	}

	public Boolean update(AccountBean bean) throws DBException {
		AccountRole[] allowedRoles = new AccountRole[] {ADMIN, MANAGER, EMPLOYEE, VENDOR};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidBean(bean, false) : false;
		
		AccountBean oldAccount = null;
		if (isValid) {			
			oldAccount = getDAOFactory().getAccountDAO().get(bean.getCredentials().getUsername());
			
			// updated user & address must have same id as the save ones
			isValid  = bean.getUser().getId() == oldAccount.getUser().getId();
			isValid &= bean.getUser().getAddress().getId() == oldAccount.getUser().getAddress().getId();
		}
		
		if (isValid) {
			AccountBean loggedInAccount = getLoggedinAccount();
			
			String oldAccountUsername = oldAccount.getCredentials().getUsername();
			String loggedInUsername = loggedInAccount.getCredentials().getUsername();
			
			boolean isSelf = loggedInUsername.equals(oldAccountUsername);
			
			if (isSelf) {
				// can change self role
				isValid = loggedInAccount.getRole() == oldAccount.getRole();
			} else {
				// admin and manager can update other accounts
				AccountRole loggedInAs = loggedInAccount.getRole();
				isValid = canUpdateOtherAccount(loggedInAs, oldAccount.getRole());
			}
		}
		
		Boolean updated = null;
		if (isValid)
			updated = getDAOFactory().getAccountDAO().update(bean);
		
		return updated;
	}
	
	public AccountBean read(final String username) throws DBException {
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
			} 
			else if (loggedInAs == ADMIN || loggedInAs == MANAGER) {
				// only admin or manager can lookup other accounts				
				account = getDAOFactory().getAccountDAO().get(username);
				isValid = (account != null);
				
				if (isValid && canReadOtherAccount(loggedInAs, account.getRole()) == false) {
					account = null;
				}
			}
		}
		return account;
	}

	public Boolean delete(String username) throws DBException {
		AccountRole[] allowedRoles = new AccountRole[] {ADMIN, MANAGER};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidUsername(username) : false;

		AccountRole loggedInAs = null;
		if (isValid) {
			AccountBean loggedInAccount = getLoggedinAccount();	
			loggedInAs = loggedInAccount.getRole();
			
			String loggedInUsername = loggedInAccount.getCredentials().getUsername();		
			boolean isSelf = loggedInUsername.equals(username);
			
			// can't delete own account
			isValid = (isSelf == false);
		}

		Boolean deleted = null;
		if (isValid) {
			// lookup the to-be-deleted account
			AccountBean oldAccount = getDAOFactory().getAccountDAO().get(username);
			isValid = (oldAccount != null);

			if (isValid && canDeleteOtherAccount(loggedInAs,  oldAccount.getRole())) {
				deleted = getDAOFactory().getAccountDAO().delete(username);
			}
		}
		
		return deleted;
	}
	
	public List<AccountBean> readAll() throws DBException {
		AccountRole[] allowedRoles = new AccountRole[] {ADMIN, MANAGER};
		boolean isValid = hasPermission(allowedRoles);
		
		List<AccountBean> accounts =  null;
		if (isValid) {
			accounts = new ArrayList<>();
			
			AccountRole loggedInAs = getLoggedinAccount().getRole();	
			
			if (loggedInAs == ADMIN) {
				// all accounts but admin accounts
				List<AccountBean> managers = getDAOFactory().getAccountDAO().getAllByRole(MANAGER);
				List<AccountBean> employee = getDAOFactory().getAccountDAO().getAllByRole(EMPLOYEE);
				List<AccountBean> vendors = getDAOFactory().getAccountDAO().getAllByRole(VENDOR);
				List<AccountBean> customerApps = getDAOFactory().getAccountDAO().getAllByRole(CUSTOMER_APP);
				
				accounts.addAll(managers);
				accounts.addAll(employee);
				accounts.addAll(vendors);
				accounts.addAll(customerApps);
				
			} else if (loggedInAs == MANAGER) {
				// employee or vendors accounts only
				List<AccountBean> employee = getDAOFactory().getAccountDAO().getAllByRole(EMPLOYEE);
				List<AccountBean> vendors = getDAOFactory().getAccountDAO().getAllByRole(VENDOR);

				accounts.addAll(employee);
				accounts.addAll(vendors);
			}
			
		}
		
		return accounts;
	}
	
	
	private boolean isValidUsername(String username){
		CredentialValidator credentialValidator = new CredentialValidator();
		boolean isValid = credentialValidator.isValidUsername(username);
		
		if (!isValid)
			addErrorMessage(credentialValidator.getValidationMessages());
		
		return isValid;
	}
	
	private boolean isValidBean(AccountBean bean, boolean isNewAccount) {
		AccountValidator accountValidator = new AccountValidator();
		
		// validate account bean
		boolean isValid = accountValidator.validate(bean);
		if (!isValid)
			addErrorMessage(accountValidator.getValidationMessages());
		
		// validate credentials bean
		if (isValid) {
			CredentialValidator credentialValidator = new CredentialValidator();
			
			isValid = credentialValidator.validate(bean.getCredentials());
			if (!isValid)
				addErrorMessage(credentialValidator.getValidationMessages());
		}
		
		// validate user bean
		if (isValid) {
			UserValidator userValidator = new UserValidator();
			
			boolean isNewUser = isNewAccount;
			isValid = userValidator.validate(bean.getUser(), isNewUser);
			if (!isValid)
				addErrorMessage(userValidator.getValidationMessages());
		}
		
		// validate address bean
		if (isValid) {
			AddressValidator addressValidator = new AddressValidator();

			boolean isNewAddress = isNewAccount;
			isValid = addressValidator.validate(bean.getUser().getAddress(), isNewAddress);

			if (!isValid)
				addErrorMessage(addressValidator.getValidationMessages());
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
		boolean isValid = false;
		if (loggedInAs == ADMIN) {
			// admin can create admin, manager, or customer_app account 
			isValid = (toBeCreatedHasRole == ADMIN || toBeCreatedHasRole == MANAGER || toBeCreatedHasRole == CUSTOMER_APP);
		} else if (loggedInAs == MANAGER) {
			// manager can create employee or vendor accounts
			isValid = (toBeCreatedHasRole == EMPLOYEE || toBeCreatedHasRole == VENDOR);
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
		boolean isValid = false;
		if (loggedInAs == ADMIN) {
			// admin can't read other admin's account
			isValid = toBeReadHasRole != ADMIN;
		} else if (loggedInAs == MANAGER) {
			isValid = (toBeReadHasRole == EMPLOYEE || toBeReadHasRole == VENDOR);
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
		boolean isValid = false;
		if (loggedInAs == ADMIN) {
			// admin can't delete other admin account but can delete every thing else
			isValid = (toBeDeleteHasRole != ADMIN);
		} else if (loggedInAs == MANAGER) {
			// manager can delete employee or vendor accounts
			isValid = (toBeDeleteHasRole == EMPLOYEE || toBeDeleteHasRole == VENDOR);
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
		boolean isValid = false;
		if (loggedInAs == ADMIN) {
			// admin can't update other admin's account
			isValid = (toBeUpdatedHasRole != ADMIN);
		} else if (loggedInAs == MANAGER) {
			isValid = (toBeUpdatedHasRole == EMPLOYEE || toBeUpdatedHasRole == VENDOR);
		}
		return isValid;
	}
	
	
	private static final AccountRole ADMIN = AccountRole.ADMIN;
	private static final AccountRole MANAGER = AccountRole.MANAGER;
	private static final AccountRole EMPLOYEE = AccountRole.EMPLOYEE;
	private static final AccountRole VENDOR = AccountRole.VENDOR;
	private static final AccountRole CUSTOMER_APP = AccountRole.CUSTOMER_APP;

}
