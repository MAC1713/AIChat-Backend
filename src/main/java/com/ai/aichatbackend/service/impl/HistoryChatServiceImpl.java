package com.ai.aichatbackend.service.impl;

import com.ai.aichatbackend.common.Constants.R;
import com.ai.aichatbackend.domain.HistoryChat;
import com.ai.aichatbackend.mapper.HistoryChatMapper;
import com.ai.aichatbackend.service.HistoryChatService;
import com.alibaba.dashscope.common.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mac
 */
@Service
public class HistoryChatServiceImpl implements HistoryChatService {

    @Autowired
    private HistoryChatMapper historyChatMapper;

    @Override
    public R saveChatHistory(HistoryChat historyChat) {
        HistoryChat existingChat = historyChatMapper.selectById(historyChat.getId());
        if (existingChat != null) {
            historyChatMapper.updateById(historyChat);
        } else {
            historyChatMapper.insert(historyChat);
        }
        return R.ok();
    }

    @Override
    public List<Message> getChatHistory(String conversationId) {
        if (historyChatMapper.selectById(conversationId) == null){
            return new ArrayList<>();
        }
        String historyChat = historyChatMapper.selectById(conversationId).getHistoryChat();
        return convertStringToMessageList(historyChat);
    }

    private List<Message> convertStringToMessageList(String historyChat){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(historyChat, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
