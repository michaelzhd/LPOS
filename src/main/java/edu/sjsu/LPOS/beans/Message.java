package edu.sjsu.LPOS.beans;

public enum Message {
	OK("OK"),
	INTERNAL_SERVER_ERROR("Some server error captured, "
			+ "probably because you entered invalid input, check and try again."
			+ "If it still happens, please contact us."),
	UNAUTHORIZED("You do not have enough privilege to access this resource."), 
	NO_TOKEN("You have no token included in the header."), 
	INVALID_TOKEN("Your token is invalid."),
	NOT_FOUND("The resource you requested is not found."),
	UNKNOWN_ERROR("Some unknown server captured. please contact us.");
	
	private String messageText;
	
	private Message(String messageText) {
		this.messageText = messageText;
	}
	
	public String getMessageText(){
		return messageText;
	}
	
}
