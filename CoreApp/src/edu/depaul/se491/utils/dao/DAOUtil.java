package edu.depaul.se491.utils.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * utility class for DAO class
 * 
 * @author Malik
 */
public abstract class DAOUtil {

	/**
	 * return auto generated key
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
	
	/**
	 * return true if 1 row is affected
	 * @param affectedRows
	 * @return
	 */
	public static boolean validUpdate(int affectedRows) {
		return affectedRows == ONE_ROW_AFFECTED;
	}
	
	/**
	 * return true if 1 row is affected
	 * @param affectedRows
	 * @return
	 */
	public static boolean validInsert(int affectedRows) {
		return affectedRows == ONE_ROW_AFFECTED;
	}
	
	/**
	 * return true if 1 row is affected
	 * @param affectedRows
	 * @return
	 */
	public static boolean validDelete(int affectedRows) {
		return affectedRows == ONE_ROW_AFFECTED;
	}
	
	/**
	 * close connection (null safe)
	 * @param c
	 * @throws SQLException
	 */
	public static void close(Connection c) throws SQLException {
		if (c != null)
			c.close();
	}

	/**
	 * close preparedStatement (null safe)
	 * @param ps
	 * @throws SQLException
	 */
	public static void close(PreparedStatement ps) throws SQLException {
		if (ps != null)
			ps.close();
	}

	/**
	 * close Statement (null safe)
	 * @param s
	 * @throws SQLException
	 */
	public static void close(Statement s) throws SQLException {
		if (s != null)
			s.close();
	}
	
	/**
	 * close ResultSet (null safe)
	 * @param rs
	 * @throws SQLException
	 */
	public static void close(ResultSet rs) throws SQLException {
		if (rs != null)
			rs.close();
	}
	
	/**
	 * set autoCommit for a connection (null safe)
	 * @param c
	 * @param value
	 * @throws SQLException
	 */
	public static void setAutoCommit(Connection c, boolean value) throws SQLException {
		if (c != null)
			c.setAutoCommit(value);
	}
	
	private static final int ONE_ROW_AFFECTED = 1;
}