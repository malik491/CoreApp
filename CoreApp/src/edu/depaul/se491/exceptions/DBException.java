/**
 * 
 */
package edu.depaul.se491.exceptions;

/**
 * @author Malik
 *
 */
public class DBException extends RuntimeException {
	private String message;
	
	public DBException(String message) {
		this.message = message;
	}
}
