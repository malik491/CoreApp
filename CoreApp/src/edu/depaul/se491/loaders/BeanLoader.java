/**
 * Interface for bean loaders
 */
package edu.depaul.se491.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * This interface specifies what should be contained in a loader.
 * 
 * The generic type <T> specifies the type of bean that the loader is responsible for extacting from
 * a result set.
 * 
 * @param <T> A type for the bean that will be loaded with this class.
 */
public interface BeanLoader<T> {
	/**
	 * Loads a list of the bean of type T from a result set.  Typically makes iterated calls
	 * to loadSingle.
	 * @param rs The java.sql.ResultSet we are extracting.
	 * @return A java.util.List<T> where T is the type for this loader.
	 * @throws SQLException
	 */
	public List<T> loadList(ResultSet rs) throws SQLException;

	/**
	 * Contains the instructions for mapping the rows in this java.sql.ResultSet into
	 * beans of type <T>.
	 * @param rs The java.sql.ResultSet to be loaded.
	 * @return A Bean of type T containing the loaded information or null.
	 * @throws SQLException
	 */
	public T loadSingle(ResultSet rs) throws SQLException;

	/**
	 * Used for an insert or update, this method contains the instructions for mapping the fields within
	 * a bean of type T into a prepared statement which modifies the appropriate table.
	 * @param ps The prepared statement to be loaded.
	 * @param bean The bean containing the data to be placed.
	 * @throws SQLException
	 */
	public void loadParameters(PreparedStatement ps, T bean, int paramIndex) throws SQLException;
}
