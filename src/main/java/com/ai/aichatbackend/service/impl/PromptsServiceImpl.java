package com.ai.aichatbackend.service.impl;

import com.ai.aichatbackend.domain.Prompts;
import com.ai.aichatbackend.mapper.PromptsMapper;
import com.ai.aichatbackend.service.PromptsService;
import com.ai.aichatbackend.utils.TokenCalculator;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mac
 */
@Service
public class PromptsServiceImpl implements PromptsService {

    private final PromptsMapper promptsMapper;
    private final TokenCalculator tokenCalculator;

    @Autowired
    public PromptsServiceImpl(PromptsMapper promptsMapper, TokenCalculator tokenCalculator) {
        this.promptsMapper = promptsMapper;
        this.tokenCalculator = tokenCalculator;
    }

    /**
     * 获取所有prompts
     *
     * @return prompts列表
     */
    @Override
    public List<Prompts> getAllPrompts() {
        return promptsMapper.selectList();
    }

    /**
     * 获取prompts
     *
     * @param id prompts id
     * @return prompts
     */
    @Override
    public Prompts getPromptsById(String id) throws NoApiKeyException, InputRequiredException {
        Prompts existingPrompts = promptsMapper.selectById(id);
        if (existingPrompts != null) {
            if (existingPrompts.getTokens() == null) {
                existingPrompts.setTokens(tokenCalculator.tokenizer(existingPrompts.getPromptData()));
            }
            return existingPrompts;
        }
        return null;
    }

    /**
     * 根据类型获取prompts
     *
     * @param prompts prompts
     * @return prompts
     */
    @Override
    public Prompts getPromptsByType(Prompts prompts) throws NoApiKeyException, InputRequiredException {
        Prompts existingPrompts = promptsMapper.selectByType(prompts.getPromptType());
        if (existingPrompts != null) {
            if (existingPrompts.getTokens() == null) {
                existingPrompts.setTokens(tokenCalculator.tokenizer(existingPrompts.getPromptData()));
            }
            return existingPrompts;
        }
        return null;
    }

    /**
     * 保存prompts
     *
     * @param prompts prompts
     * @return 成功条数
     */
    @Override
    public Integer savePrompts(Prompts prompts) throws NoApiKeyException, InputRequiredException {
        prompts.setTokens(tokenCalculator.tokenizer(prompts.getPromptData()));
        Prompts existingPrompts = promptsMapper.selectByType(prompts.getPromptType());
        if (existingPrompts != null) {
            return promptsMapper.updateById(prompts);
        } else {
            return promptsMapper.insert(prompts);
        }
    }
}
