package edu.sjsu.LPOS.beans;

public enum Message {
	OK("OK"),
	UNAUTHORIZED("You do not have enough privilege to access this resource."), 
	NO_TOKEN("You have no token included in the header."), 
	INVALID_TOKEN("Your token is invalid."),
	NOT_FOUND("The resource you requested is not found.");
	
	private String messageText;
	
	private Message(String messageText) {
		this.messageText = messageText;
	}
	
	public String getMessageText(){
		return messageText;
	}
	
}
