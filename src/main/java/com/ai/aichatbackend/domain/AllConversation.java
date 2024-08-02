package com.ai.aichatbackend.domain;

import com.alibaba.dashscope.common.Message;
import lombok.Data;

import java.util.List;

/**
 * @author mac
 */
@Data
public class AllConversation {
    public String userMessage;
    public String aiMessage;
    public int messageCount;
    public List<Message> fullConversationHistory;

}
