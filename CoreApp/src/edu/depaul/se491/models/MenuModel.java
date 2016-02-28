package edu.depaul.se491.models;


import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import edu.depaul.se491.beans.CredentialsBean;
import edu.depaul.se491.beans.MenuItemBean;
import edu.depaul.se491.daos.DAOFactory;
import edu.depaul.se491.enums.AccountRole;
import edu.depaul.se491.validators.MenuItemValidator;

/**
 * Menu Model
 * class to manipulate Menu (create menu items, update menu items, etc)
 * 
 * @author Malik
 */
public class MenuModel extends BaseModel {
	
	/**
	 * construct MenuModel
	 * @param daoFactory DAO factory
	 * @param credentials of the current model user
	 */
	public MenuModel(DAOFactory daoFactory, CredentialsBean credentials) {
		super(daoFactory, credentials);
	}

	/**
	 * create new menuItem
	 * @param bean
	 * @return newly created menuItem or null
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
			} catch (SQLException e) {
				setResponseAndMeessageForDBError(e);
			}
		}
		
		return createdMenuItem;
	}
	

	/**
	 * update a menu item
	 * @param bean
	 * @return
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
			} catch (SQLException e) {
				setResponseAndMeessageForDBError(e);
			}
		}
		return updated;
	}
	

	/**
	 * return a menu item with the given id
	 * @param id
	 * @return
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
			} catch (SQLException e) {
				setResponseAndMeessageForDBError(e);
			}
		}
		return menuItem;
	}
	
	/**
	 * Return all visible menu items or empty list
	 * @return
	 */
	public List<MenuItemBean> readAllVisible() {
		AccountRole[] allowedRoles = new AccountRole[] {MANAGER, EMPLOYEE, CUSTOMER_APP};
		if (hasPermission(allowedRoles))
			return readAll(false);
		else
			return null;
	}
	
	/**
	 * Return all hidden menu items or empty list
	 * @return
	 */
	public List<MenuItemBean> readAllHidden() {
		AccountRole[] allowedRoles = new AccountRole[] {MANAGER, EMPLOYEE, CUSTOMER_APP};
		if (hasPermission(allowedRoles))
			return readAll(true);
		else
			return null;
	}

	/**
	 * delete a menu item
	 * NOTE: this is not supported yet (DB design) so it always return false
	 * @param id
	 * @return false, always
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
			} catch (SQLException e) {
				setResponseAndMeessageForDBError(e);
			}
		}
		return deleted;
	}
	
	/**
	 * Hide/un-Hide a menu item
	 * @param id
	 * @param hide hide a menu Item?
	 * @return false, always
	 */
	public Boolean updateIsHidden(Long id, boolean hide) {
		AccountRole[] allowedRoles = new AccountRole[] {MANAGER};
		boolean isValid = hasPermission(allowedRoles);
		
		isValid = isValid? isValidMenuItemId(id) : false; 
		Boolean updated = null;
		if (isValid) {
			try {
				updated = getDAOFactory().getMenuItemDAO().updateIsHidden(id, hide);
			} catch (SQLException e) {
				setResponseAndMeessageForDBError(e);
			}
		}
		return updated;
	}
	
	private List<MenuItemBean> readAll(boolean isHidden) {
		List<MenuItemBean> menuItemList = null;
		try {
			menuItemList = getDAOFactory().getMenuItemDAO().getAll(isHidden);
		} catch (SQLException e) {
			setResponseAndMeessageForDBError(e);
		}
		return menuItemList;
		
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
