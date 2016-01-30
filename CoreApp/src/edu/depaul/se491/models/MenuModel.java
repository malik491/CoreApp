/**
 * Menu Model
 * 
 * Class to manipulate Menu (create menu items, update menu items, etc)
 */
package edu.depaul.se491.models;


import java.util.List;

import javax.ws.rs.core.Response.Status;

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
public class MenuModel extends BaseModel {
	
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
	public MenuItemBean create(MenuItemBean bean) {
		AccountRole[] allowedRoles = new AccountRole[] {MANAGER};
		boolean isValid = hasPermission(allowedRoles);
		
		boolean isNewItem = true;
		isValid = isValid? isValidBean(bean, isNewItem) : false; 
		
		MenuItemBean createdMenuItem = null;
		if (isValid) {
			try {
				createdMenuItem = getDAOFactory().getMenuItemDAO().add(bean);
				if (createdMenuItem == null) {
					setResponseAndMeessageForDBError();
				}
			} catch (DBException e) {
				setResponseAndMeessageForDBError();
			}
		}
		
		return createdMenuItem;
	}
	

	/**
	 * update an existing menu item
	 * @param bean
	 * @return
	 * @throws DBException on DB Errors
	 */
	public Boolean update(MenuItemBean bean) {
		AccountRole[] allowedRoles = new AccountRole[] {MANAGER};
		boolean isValid = hasPermission(allowedRoles);
		
		boolean isNewItem = false;
		isValid = isValid? isValidBean(bean, isNewItem) : false; 
		
		Boolean updated = null;
		if (isValid) {
			try {
				updated = getDAOFactory().getMenuItemDAO().update(bean);
			} catch (DBException e) {
				setResponseAndMeessageForDBError();
			}
		}
		return updated;
	}
	

	/**
	 * Return a menu item with the given id or null
	 * @param id
	 * @return
	 * @throws DBException
	 */
	public MenuItemBean read(Long id) {
		AccountRole[] allowedRoles = new AccountRole[] {MANAGER, EMPLOYEE};
		boolean isValid = hasPermission(allowedRoles);

		isValid = isValid? isValidMenuItemId(id) : false; 

		MenuItemBean menuItem = null;
		if (isValid) {
			try {
				menuItem = getDAOFactory().getMenuItemDAO().get(id);
				if (menuItem == null) {
					setResponseStatus(Status.NOT_FOUND);
					setResponseMessage("No Menu Item Found");
				}			
			} catch (DBException e) {
				setResponseAndMeessageForDBError();
			}
		}
		return menuItem;
	}
	
	/**
	 * Return all menu items
	 * @return
	 * @throws DBException
	 */
	public List<MenuItemBean> readAll() {
		AccountRole[] allowedRoles = new AccountRole[] {MANAGER, EMPLOYEE, CUSTOMER_APP};
		boolean isValid = hasPermission(allowedRoles);
		
		List<MenuItemBean> menuItemList = null;
		if (isValid) {
			try {
				menuItemList = getDAOFactory().getMenuItemDAO().getAll();
			} catch (DBException e) {
				setResponseAndMeessageForDBError();
			}
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
	public Boolean delete(Long id) {
		AccountRole[] allowedRoles = new AccountRole[] {MANAGER};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidMenuItemId(id) : false; 
		
		Boolean deleted = null;
		if (isValid) {
			// delete the menu item (always returns false. not supported)
			
			try {
				deleted = getDAOFactory().getMenuItemDAO().delete(id);
			} catch (DBException e) {
				setResponseAndMeessageForDBError();
			}
		}
		return deleted;
	}
	
	
	private boolean isValidMenuItemId(Long id) {
		MenuItemValidator validator = new MenuItemValidator();
		
		boolean isNewItem = false;
		boolean isValid = validator.validateId(id, isNewItem); 
		
		if (!isValid) {
			setResponseStatus(Status.BAD_REQUEST);
			setResponseMessage("Invalid menu item id");
		}
		return isValid;
	}
	
	private boolean isValidBean(MenuItemBean bean, boolean isNewItem) {
		MenuItemValidator validator = new MenuItemValidator();
		boolean isValid = validator.validate(bean, isNewItem);
		
		if (!isValid) {
			setResponseStatus(Status.BAD_REQUEST);
			setResponseMessage("Invalid menu item data");
		}
		return isValid;
	}
	
}
