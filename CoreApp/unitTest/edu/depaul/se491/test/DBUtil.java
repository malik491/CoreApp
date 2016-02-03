/**
 * Utility class for executing SQL statements
 */
package edu.depaul.se491.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import edu.depaul.se491.daos.ConnectionFactory;

/**
 * @author Malik
 *
 */
public class DBUtil {
	
	public static void executeSQLFile(ConnectionFactory connFactory, String filePath) throws FileNotFoundException, SQLException, IOException {
		executeSQL(connFactory ,SQLFileCache.getInstance().getQueries((filePath)));
	}

	public static void executeSQL(ConnectionFactory connFactory, List<String> queries) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		
		conn = connFactory.getConnection();
		
		for (String sql : queries) {
			stmt = conn.createStatement();
			try {
				stmt.execute(sql);
			} 
			catch (SQLException e) {
				throw new SQLException(e.getMessage() + " from executing: " + sql, e.getSQLState(), e.getErrorCode());
			}
			finally {
				if (stmt != null)
					stmt.close();
			}
		}
		if (conn != null)
			conn.close();
	}
}
