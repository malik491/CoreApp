/**
 * Validator class for menuItem beans
 */
package edu.depaul.se491.validators;

import edu.depaul.se491.beans.MenuItemBean;
import edu.depaul.se491.utils.ParamLengths;
import edu.depaul.se491.utils.ParamValues;

/**
 * @author Malik
 *
 */
public class MenuItemValidator extends BeanValidator {

		
	public boolean validate(MenuItemBean bean, boolean isNewMenuItem) {
		boolean isValid = isValidObject(bean, "Invalid MenuItem (Null)");

		if(isValid){
			isValid  =  validateId(bean.getId(), isNewMenuItem);
			isValid &= isValidName(bean);
			isValid &= isValidDescription(bean);
			isValid &= isValidPrice(bean);
			isValid &= isValidCategoryItem(bean);
		}
		
		return isValid;
	}
	
	public boolean validateId(Long menuItemId, boolean isNewMenuItem) {
		boolean isValid = isValidObject(menuItemId, "Invalid menu item id (Null Long object)");
		
		if (isValid) {
			String invalidMsg = String.format("Invalid MenuItem Id %s", isNewMenuItem? " (for new MenuItem)": "");
			isValid = isValidId(menuItemId, isNewMenuItem, invalidMsg);			
		}
		
		return isValid;
	}

	private boolean isValidName(MenuItemBean bean)
	{
		return isValidString(bean.getName(), ParamLengths.MenuItem.MIN_NAME, ParamLengths.MenuItem.MAX_NAME, "Invalid MenuItem Name");
	}
	
	private boolean isValidDescription(MenuItemBean bean)
	{
		return isValidString(bean.getDescription(), ParamLengths.MenuItem.MIN_DESC, ParamLengths.MenuItem.MAX_DESC, "Invalid MenuItem Description");
	}
		
	private boolean isValidPrice(MenuItemBean bean)
	{
		return isValidValue(bean.getPrice(), ParamValues.MenuItem.MIN_PRICE, ParamValues.MenuItem.MAX_PRICE, "Invalid MenuItem Price");
	}	
	
	private boolean isValidCategoryItem(MenuItemBean bean) {
		return isValidObject(bean.getItemCategory(), "Invalid MenuItem category (Null)");
	}
}
