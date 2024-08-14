package com.ai.aichatbackend.service;

import com.ai.aichatbackend.domain.Prompts;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;

import java.util.List;

/**
 * @author mac
 */
public interface PromptsService {

    /**
     * 获取所有prompts
     * @return prompts列表
     */
    List<Prompts> getAllPrompts();

    /**
     * 获取prompts
     * @param id prompts id
     * @return prompts
     * @throws NoApiKeyException 没有apikey异常
     * @throws InputRequiredException 输入异常
     */
    Prompts getPromptsById(String id) throws NoApiKeyException, InputRequiredException;

    /**
     * 根据类型获取prompts
     * @param prompts prompts
     * @return prompts
     * @throws NoApiKeyException 没有apikey异常
     * @throws InputRequiredException 输入异常
     */
    Prompts getPromptsByType(Prompts prompts) throws NoApiKeyException, InputRequiredException;

    /**
     * 保存prompts
     * @param prompts prompts
     * @return 成功条数
     * @throws NoApiKeyException 没有apikey异常
     * @throws InputRequiredException 输入异常
     */
    Integer savePrompts(Prompts prompts) throws NoApiKeyException, InputRequiredException;
}
