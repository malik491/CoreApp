package edu.depaul.se491.validators;

import edu.depaul.se491.beans.MenuItemBean;
import edu.depaul.se491.utils.ParamLengths;
import edu.depaul.se491.utils.ParamValues;

/**
 * MenuItemBean Validator
 * 
 * @author Malik
 */
public class MenuItemValidator extends BeanValidator {

	/**
	 * 	validate MenuItemBean
	 * @param bean
	 * @param isNewMenuItem
	 * @return
	 */
	public boolean validate(MenuItemBean bean, boolean isNewMenuItem) {
		boolean isValid = isValidObject(bean);

		if(isValid){
			isValid  =  validateId(bean.getId(), isNewMenuItem);
			isValid &= isValidName(bean);
			isValid &= isValidDescription(bean);
			isValid &= isValidPrice(bean);
			isValid &= isValidCategoryItem(bean);
		}
		
		return isValid;
	}
	
	/**
	 * validate menu item id
	 * @param menuItemId
	 * @param isNewMenuItem
	 * @return
	 */
	public boolean validateId(Long menuItemId, boolean isNewMenuItem) {
		boolean isValid = isValidObject(menuItemId);
		
		if (isValid)
			isValid = isValidId(menuItemId, isNewMenuItem);
		
		return isValid;
	}

	private boolean isValidName(MenuItemBean bean) {
		return isValidString(bean.getName(), ParamLengths.MenuItem.MIN_NAME, ParamLengths.MenuItem.MAX_NAME);
	}
	
	private boolean isValidDescription(MenuItemBean bean) {
		return isValidString(bean.getDescription(), ParamLengths.MenuItem.MIN_DESC, ParamLengths.MenuItem.MAX_DESC);
	}
		
	private boolean isValidPrice(MenuItemBean bean) {
		return isValidValue(bean.getPrice(), ParamValues.MenuItem.MIN_PRICE, ParamValues.MenuItem.MAX_PRICE);
	}	
	
	private boolean isValidCategoryItem(MenuItemBean bean) {
		return isValidObject(bean.getItemCategory());
	}
}
