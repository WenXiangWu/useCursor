package com.poker.event;

import org.springframework.context.ApplicationEvent;
import com.poker.model.ChatMessage;

public class ChatMessageEvent extends ApplicationEvent {
    private final ChatMessage chatMessage;
    private final long messageTimestamp;

    public ChatMessageEvent(Object source, ChatMessage chatMessage) {
        super(source);
        this.chatMessage = chatMessage;
        this.messageTimestamp = System.currentTimeMillis();
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public long getMessageTimestamp() {
        return messageTimestamp;
    }
}