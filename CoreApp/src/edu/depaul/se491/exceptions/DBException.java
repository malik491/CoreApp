/**
 * 
 */
package edu.depaul.se491.exceptions;

/**
 * @author Malik
 *
 */
public class DBException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String message;
	
	public DBException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}
