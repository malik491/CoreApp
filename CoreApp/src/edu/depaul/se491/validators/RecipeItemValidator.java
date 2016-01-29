/**
 * Validator class for RecipeItem bean
 */
package edu.depaul.se491.validators;

import edu.depaul.se491.beans.RecipeItemBean;

/**
 * @author Malik
 *
 */
public class RecipeItemValidator extends BeanValidator {

	public boolean validateCreate(RecipeItemBean bean, boolean isNewItem) {
		addMessage("invalid: no implementation yet");
		return false;
	}
		
	public boolean validateId(Long recipeItemId, boolean isNewItem) {
		addMessage("invalid: no implementation yet");
		return false;
	}
}
