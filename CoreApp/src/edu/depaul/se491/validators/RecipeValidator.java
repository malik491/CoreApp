/**
 * Validator class for Recipe bean
 */
package edu.depaul.se491.validators;

import edu.depaul.se491.beans.RecipeBean;

/**
 * @author Malik
 *
 */
public class RecipeValidator extends BeanValidator {

	
	public boolean validateCreate(RecipeBean bean, boolean isNewRecipe) {
		addMessage("invalid: no implementation yet");
		return false;
	}

	public boolean validateId(Long recipeId,  boolean isNewRecipe) {
		addMessage("invalid: no implementation yet");
		return false;
	}
}
