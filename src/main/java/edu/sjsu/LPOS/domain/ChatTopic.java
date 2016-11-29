package edu.sjsu.LPOS.domain;

import java.util.List;
import java.util.UUID;

import edu.sjsu.LPOS.beans.ChatMessage;

public class ChatTopic {

	private Long chatsId;
	private List<ChatMessage> chats;
	private List<Long> participating_userIds;
	
}
