package edu.depaul.se491.daos.mysql;

import edu.depaul.se491.daos.ConnectionFactory;
import edu.depaul.se491.daos.DAOFactory;

public class EmailDAO {
	private ConnectionFactory connFactory;
	
	public EmailDAO(DAOFactory daoFactory, ConnectionFactory connFactory) {
		this.connFactory = connFactory;
	}
	
}
