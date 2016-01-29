/**
 * Menu Model
 * 
 * Class to manipulate Menu (create menu items, update menu items, etc)
 */
package edu.depaul.se491.models;


import java.util.List;

import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.MenuItemBean;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.daos.ProductionDAOFactory;
import edu.depaul.se491.enums.AccountRole;
import edu.depaul.se491.exceptions.DBException;
import edu.depaul.se491.validators.MenuItemValidator;

/**
 * @author Malik
 *
 */
public class MenuModel extends BaseModel{
	
	public MenuModel(CredentialsBean credentials) {
		super(ProductionDAOFactory.getInstance(), credentials);
	}
	
	public MenuModel(DAOFactory daoFactory, CredentialsBean credentials) {
		super(daoFactory, credentials);
	}

	/**
	 * create a new menu item and return the newly created menu item (has menu item id)
	 * @param bean
	 * @return newly created menu item or null
	 * @throws DBException
	 */
	public MenuItemBean create(MenuItemBean bean) throws DBException {
		// get the logged in user and check they have permission (role)
		AccountRole[] allowedRoles = new AccountRole[] {AccountRole.MANAGER};
		boolean isValid = hasPermission(allowedRoles);
		
		// validate bean parameter
		boolean isNewItem = true;
		isValid = isValid? isValidBean(bean, isNewItem) : false; 
		
		MenuItemBean createdMenuItem = null;
		if (isValid) {
			// add it
			createdMenuItem = getDAOFactory().getMenuItemDAO().add(bean);
			if (createdMenuItem == null)
				addErrorMessage("New menu item could not be added (check menu item details)");
		}
		
		return createdMenuItem;
	}
	

	/**
	 * update an existing menu item
	 * @param bean
	 * @return
	 * @throws DBException on DB Errors
	 */
	public Boolean update(MenuItemBean bean) throws DBException {
		// get the logged in user and check they have permission (role)
		AccountRole[] allowedRoles = new AccountRole[] {AccountRole.MANAGER};
		boolean isValid = hasPermission(allowedRoles);
		
		// validate bean parameter
		boolean isNewItem = false;
		isValid = isValid? isValidBean(bean, isNewItem) : false; 
		
		Boolean updated = null;
		if (isValid) {
			// update it
			updated = getDAOFactory().getMenuItemDAO().update(bean);
		}
		
		return updated;
	}
	

	/**
	 * Return a menu item with the given id or null
	 * @param id
	 * @return
	 * @throws DBException
	 */
	public MenuItemBean read(Long id) throws DBException {
		// get the logged in user and check they have permission (role)
		AccountRole[] allowedRoles = new AccountRole[] {AccountRole.MANAGER, AccountRole.EMPLOYEE};
		boolean isValid = hasPermission(allowedRoles);

		// validate id
		isValid = isValid? isValidMenuItemId(id) : false; 

		MenuItemBean menuItem = null;
		if (isValid) {
			//look up the menu item
			menuItem = getDAOFactory().getMenuItemDAO().get(id);
			if (menuItem == null)
				addErrorMessage(String.format("No menu item found (id = %d)", id));
		}
		return menuItem;
	}
	
	/**
	 * Return all menu items
	 * @return
	 * @throws DBException
	 */
	public List<MenuItemBean> readAll() throws DBException {
		// get the logged in user and check they have permission (role)
		AccountRole[] allowedRoles = new AccountRole[] {AccountRole.MANAGER, AccountRole.EMPLOYEE, AccountRole.CUSTOMER_APP};
		boolean isValid = hasPermission(allowedRoles);
		
		List<MenuItemBean> menuItemList = null;
		if (isValid) {
			menuItemList = getDAOFactory().getMenuItemDAO().getAll();
		}
		return menuItemList;
	}	

	/**
	 * delete an existing menu item
	 * THIS FUNCTIONALITY IS NOT SUPPORTED FOR NOW SO THIS ALWAYS RETURNS FALSE
	 * @param id
	 * @return
	 * @throws ValidationException
	 * @throws DBException
	 */
	public Boolean delete(Long id) throws DBException {
		// get the logged in user and check they have permission (role)
		AccountRole[] allowedRoles = new AccountRole[] {AccountRole.MANAGER};
		boolean isValid = hasPermission(allowedRoles);
		
		// validate id
		isValid = isValid? isValidMenuItemId(id) : false; 
		
		Boolean deleted = null;
		if (isValid) {
			// delete the menu item (always returns false. not supported)
			deleted = getDAOFactory().getMenuItemDAO().delete(id);
		}
		return deleted;
	}
	
	
	private boolean isValidMenuItemId(Long id) {
		MenuItemValidator validator = new MenuItemValidator();
		
		boolean isNewItem = false;
		boolean isValid = validator.validateId(id, isNewItem); 
		
		if (!isValid)
			addErrorMessage(validator.getValidationMessages());
		
		return isValid;
	}
	
	private boolean isValidBean(MenuItemBean bean, boolean isNewItem) {
		MenuItemValidator validator = new MenuItemValidator();
		boolean isValid = validator.validate(bean, isNewItem);
		
		if (!isValid)
			addErrorMessage(validator.getValidationMessages());
		return isValid;
	}
}
