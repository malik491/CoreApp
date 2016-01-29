/**
 * Validator class for InventoryItem
 */
package edu.depaul.se491.validators;

import edu.depaul.se491.beans.InventoryItemBean;

/**
 * @author Malik
 *
 */
public class InventoryItemValidator extends BeanValidator {

	public boolean validate(InventoryItemBean bean, boolean isNewItem) {
		// to do
		addMessage("invalid: no implementation yet");
		return false;
	}
	
	public boolean validateId(long inventoryItemId, boolean isNewItem) {
		// to do
		addMessage("invalid: no implementation yet");
		return false;
	}
}
