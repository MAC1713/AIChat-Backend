package com.ai.aichatbackend.utils;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.ai.aichatbackend.common.Constants.AIAssistantConstants.COLLATION;
import static com.ai.aichatbackend.common.Constants.AIAssistantConstants.NECESSARY_TO_NOTE;

/**
 * @author mac
 */
@Component
public class NotebookAssistant {
    private final MessageUtils messageUtils;

    public NotebookAssistant(MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
    }

    /**
     * 助手检测是否记入记事本
     *
     * @param userMessage 用户消息
     * @param aiMessage   AI消息
     * @throws NoApiKeyException      API Key错误
     * @throws InputRequiredException 输入错误
     * @throws IOException            IO错误
     * @throws ClassNotFoundException 类错误
     */
    public void sendToCheck(String userMessage, String aiMessage) throws NoApiKeyException, InputRequiredException, IOException, ClassNotFoundException {
        messageUtils.sendNoteAssistantMessage("\"" + userMessage + "\n" + aiMessage + "\"\n" + NECESSARY_TO_NOTE);
    }

    /**
     * 助手进行总结
     *
     * @throws NoApiKeyException      API Key错误
     * @throws InputRequiredException 输入错误
     * @throws IOException            IO错误
     * @throws ClassNotFoundException 类错误
     */
    public void collection() throws NoApiKeyException, InputRequiredException, IOException, ClassNotFoundException {
        messageUtils.sendNoteAssistantMessage(COLLATION);
    }


}
