package com.ai.aichatbackend.service;

import com.ai.aichatbackend.common.Constants.R;
import com.ai.aichatbackend.domain.AllConversation;
import com.ai.aichatbackend.domain.ApiParams;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;

import java.io.IOException;

/**
 * @author mac
 */
public interface ChatService {

    /**
     * 发送消息获取回答
     *
     * @param allConversation 所有消息
     * @return ai回答
     * @throws NoApiKeyException      缺少apiKey
     * @throws InputRequiredException 输入缺少参数
     * @throws IOException            io异常
     * @throws ClassNotFoundException 类找不到
     */
    String sendMessage(AllConversation allConversation) throws NoApiKeyException, InputRequiredException, IOException, ClassNotFoundException;

    /**
     * 并行更新笔记本
     * @param allConversation 所有消息
     * @return 更新结果
     * @throws NoApiKeyException 缺少apiKey
     * @throws InputRequiredException 输入缺少参数
     * @throws IOException io异常
     * @throws ClassNotFoundException 类找不到
     */
    R updateNotebook(AllConversation allConversation) throws NoApiKeyException, InputRequiredException, IOException, ClassNotFoundException;

    /**
     * 设置对话参数
     * @param apiParams 对话参数
     * @throws IOException io异常
     */
    void setApiParams(ApiParams apiParams) throws IOException;
}