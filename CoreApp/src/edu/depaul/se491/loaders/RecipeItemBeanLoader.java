/**
 * Loader for RecipeItem bean
 */
package edu.depaul.se491.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.depaul.se491.beans.RecipeItemBean;

/**
 * @author Malik
 *
 */
public class RecipeItemBeanLoader implements BeanLoader<RecipeItemBean> {

	@Override
	public List<RecipeItemBean> loadList(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RecipeItemBean loadSingle(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadParameters(PreparedStatement ps, RecipeItemBean bean, int paramIndex) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
