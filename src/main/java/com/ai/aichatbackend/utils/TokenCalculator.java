package com.ai.aichatbackend.utils;

import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.tokenizers.Tokenization;
import com.alibaba.dashscope.tokenizers.TokenizationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;


/**
 * @author mac
 */
@Component
public class TokenCalculator {

    @Value("${api-key}")
    private String apiKey;

    public int tokenizer(String message) throws ApiException, NoApiKeyException, InputRequiredException {
        Tokenization tokenizer = new Tokenization();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(message)
                .build();
        GenerationParam param = GenerationParam.builder()
                .model(Tokenization.Models.QWEN_TURBO)
                .messages(Collections.singletonList(userMsg))
                .apiKey(apiKey)
                .build();
        TokenizationResult result = tokenizer.call(param);
        return result.getUsage().getInputTokens();
    }

    public int tokenizer(Message message) throws ApiException, NoApiKeyException, InputRequiredException {
        Tokenization tokenizer = new Tokenization();
        GenerationParam param = GenerationParam.builder()
                .model(Tokenization.Models.QWEN_TURBO)
                .messages(Collections.singletonList(message))
                .apiKey(apiKey)
                .build();
        TokenizationResult result = tokenizer.call(param);
        return result.getUsage().getInputTokens();
    }

    public int tokenizer(List<Message> message) throws ApiException, NoApiKeyException, InputRequiredException {
        Tokenization tokenizer = new Tokenization();
        GenerationParam param = GenerationParam.builder()
                .model(Tokenization.Models.QWEN_TURBO)
                .messages(message)
                .apiKey(apiKey)
                .build();
        TokenizationResult result = tokenizer.call(param);
        return result.getUsage().getInputTokens();
    }
}