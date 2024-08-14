package com.ai.aichatbackend.service;

import com.ai.aichatbackend.common.Constants.R;
import com.ai.aichatbackend.domain.HistoryChat;
import com.alibaba.dashscope.common.Message;
import java.util.List;

/**
 * @author mac
 */
public interface HistoryChatService {

    /**
     * 保存聊天记录
     *
     * @param historyChat 聊天记录
     * @return 聊天记录
     */
    R saveChatHistory(HistoryChat historyChat);

    /**
     * 获取聊天记录
     * @param conversationId 对话id
     * @return 聊天记录
     */
    List<Message> getChatHistory(String conversationId);
}