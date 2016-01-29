/**
 * Loader for Recipe bean
 */
package edu.depaul.se491.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.depaul.se491.beans.RecipeBean;

/**
 * @author Malik
 *
 */
public class RecipeBeanLoader implements BeanLoader<RecipeBean> {

	@Override
	public List<RecipeBean> loadList(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RecipeBean loadSingle(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadParameters(PreparedStatement ps, RecipeBean bean, int paramIndex) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
