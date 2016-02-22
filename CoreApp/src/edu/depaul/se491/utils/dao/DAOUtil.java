/**
 * utility class for DAO objects and SQLException
 */
package edu.depaul.se491.utils.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * @author Malik
 *
 */
public abstract class DAOUtil {

	/**
	 * return the auto genersated key
	 * @param ps
	 * @return
	 * @throws SQLException
	 */
	public static long getAutGeneratedKey(PreparedStatement ps) throws SQLException {
		long newId = -1;
		ResultSet addressKey = ps.getGeneratedKeys();
		newId = addressKey.next()? addressKey.getLong(1) : -1; 					
		if (newId <= 0)
			throw new SQLException("getGeneratedKeys() returned 0 (SQL NULL) or couldn't get auto generated key (resultSet.next() is false)");
		return newId;
	}
	
	public static boolean validUpdate(int affectedRows) {
		return affectedRows == ZERO_ROW_AFFECTED || affectedRows == ONE_ROW_AFFECTED;
	}
	
	public static boolean validInsert(int affectedRows) {
		return affectedRows == ONE_ROW_AFFECTED;
	}
	
	public static boolean validDelete(int affectedRows) {
		return affectedRows == ONE_ROW_AFFECTED;
	}
	
	public static void close(Connection c) throws SQLException {
		if (c != null)
			c.close();
	}

	public static void close(PreparedStatement ps) throws SQLException {
		if (ps != null)
			ps.close();
	}

	public static void close(Statement s) throws SQLException {
		if (s != null)
			s.close();
	}
	
	public static void close(ResultSet rs) throws SQLException {
		if (rs != null)
			rs.close();
	}
	
	public static void setAutoCommit(Connection c, boolean value) throws SQLException {
		if (c != null)
			c.setAutoCommit(value);
	}
	
	public static final int ONE_ROW_AFFECTED = 1;
	public static final int ZERO_ROW_AFFECTED = 0;
	public static final String GENERIC_BD_ERROR_MSG = "Database Error Occured. Contact admin for details";
}