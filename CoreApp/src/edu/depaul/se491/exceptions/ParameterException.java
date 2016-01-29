package edu.depaul.se491.exceptions;

import java.util.List;

public class ParameterException extends RuntimeException {
	private List<String> messages;
	
	public ParameterException(List<String> messages) {
		super();
		this.messages = messages;
	}
		
	public ParameterException(List<String> messages, Exception exp) {
		super(exp);
		this.messages = messages;
	}
	
	
	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		for(String msg: messages) {
			sb.append(msg);
			sb.append(NEW_LINE);
		}
		return sb.toString();
	}
	
	private static final String NEW_LINE = "\n";
}
