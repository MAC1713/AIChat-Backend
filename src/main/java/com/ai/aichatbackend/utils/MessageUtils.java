package com.ai.aichatbackend.utils;

import com.ai.aichatbackend.common.Constants.AIChatConstants;
import com.ai.aichatbackend.common.Global.GlobalParams;
import com.ai.aichatbackend.domain.Note;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.ai.aichatbackend.common.Constants.AIChatConstants.*;

/**
 * @author mac
 */
@Slf4j
@Component
public class MessageUtils {
    @Value("${api-key}")
    private String apiKey;
    private final NotebookUtils notebookUtils;
    private final ApiParamsUtils apiParamsUtils;
    private final AIPromptsUtils aiPromptsUtils;

    @Autowired
    private MessageUtils (NotebookUtils notebookUtils, ApiParamsUtils apiParamsUtils, AIPromptsUtils aiPromptsUtils) {
        this.notebookUtils = notebookUtils;
        this.apiParamsUtils = apiParamsUtils;
        this.aiPromptsUtils = aiPromptsUtils;
    }

    private List<Message> fullConversationHistory;
    private int messageCountSinceLastReminder;

    private static Message createMessage(Role role, String content) {
        return Message.builder().role(role.getValue()).content(content).build();
    }

    public String generateResponse(String userInput, GenerationParam params) throws ApiException, NoApiKeyException, InputRequiredException, IOException, ClassNotFoundException {
        Message userMessage = createMessage(Role.USER, userInput);
        this.fullConversationHistory = GlobalParams.getInstance().getFullConversationHistory();
        this.messageCountSinceLastReminder = GlobalParams.getInstance().getMessageCount();

        List<Message> contextMessages = setSystemMessages();

        contextMessages.add(userMessage);
        fullConversationHistory.add(userMessage);

        params.setMessages(contextMessages);

        try {
            GenerationResult result = callGenerationWithMessages(params);
            String aiResponse = result.getOutput().getChoices().get(0).getMessage().getContent();
            Message aiMessage = result.getOutput().getChoices().get(0).getMessage();
            fullConversationHistory.add(aiMessage);
            GlobalParams.getInstance().setFullConversationHistory(fullConversationHistory);
            return aiResponse;
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            log.error("Error occurred while calling generation API: " + e.getMessage());
            throw e;
        }
    }

    public void sendSpecialMessage(Role role, String collation, Boolean useNotebook) throws NoApiKeyException, InputRequiredException, IOException, ClassNotFoundException {
        try {
            List<Message> messages = new ArrayList<>();
            messages.add(Message.builder().role(role.getValue()).content(collation).build());
            String aiResponse = generateResponse(collation, createGenerationParam(messages));
            if (Boolean.TRUE.equals(useNotebook)) {
                checkAndUpdateNotebook(aiResponse);
            }
        } catch (ApiException | NoApiKeyException | InputRequiredException | IOException | ClassNotFoundException ex) {
            log.error("Error occurred while sending special message: " + ex.getMessage());
            throw ex;
        }
    }

    public void checkAndUpdateNotebook(String aiResponse) {
        log.info("aiResponse = " + aiResponse);
        List<Note> newNotes = notebookUtils.extractNotesFromResponse(aiResponse);
        for (Note note : newNotes) {
            notebookUtils.addNote(note.getContent(), note.getTag(), note.getImportance());
        }
    }

    /**
     * 设置System级对话消息
     *
     * @return final message
     * @throws IOException            提示词保存问题
     * @throws ClassNotFoundException 提示词加载问题
     */
    private List<Message> setSystemMessages() throws IOException, ClassNotFoundException {
        List<Message> contextMessages = new ArrayList<>();
        setHistoryConversation(contextMessages);


        String firstSystemMessage = aiPromptsUtils.loadPrompts().getInitialSystemPrompt() + "\n" + aiPromptsUtils.loadPrompts().getSimplifiedSystemPrompt() + "\n" + AIChatConstants.HOW_TO_USE_NOTEBOOK;

        //初始化System消息
        if (messageCountSinceLastReminder == 0) {
            contextMessages.add(createMessage(Role.SYSTEM, firstSystemMessage));
            fullConversationHistory.add(createMessage(Role.SYSTEM, firstSystemMessage));
        }

        //每5次对话提醒一次身份
        if (messageCountSinceLastReminder >= REMINDER_INTERVAL && messageCountSinceLastReminder % REMINDER_INTERVAL == 0) {
            contextMessages.add(createMessage(Role.SYSTEM, aiPromptsUtils.loadPrompts().getSimplifiedSystemPrompt()));
            fullConversationHistory.add(createMessage(Role.SYSTEM, aiPromptsUtils.loadPrompts().getSimplifiedSystemPrompt()));
        }

        //每2次对话提示一次notebook指令
        if (messageCountSinceLastReminder >= REMIND_USE_NOTEBOOK && messageCountSinceLastReminder % REMIND_USE_NOTEBOOK == 0) {
            contextMessages.add(createMessage(Role.SYSTEM, AIChatConstants.HOW_TO_USE_NOTEBOOK));
            fullConversationHistory.add(createMessage(Role.SYSTEM, AIChatConstants.HOW_TO_USE_NOTEBOOK));
        }

        //每次对话提示一次notebook
        if (!notebookUtils.getNotes().isEmpty()) {
            contextMessages.add(createMessage(Role.SYSTEM, "Here's the current content of your notebook:\n" + notebookUtils.getFormattedNotes()));
        }

        messageCountSinceLastReminder++;

        GlobalParams.getInstance().setMessageCount(messageCountSinceLastReminder);
        return contextMessages;
    }

    /**
     * 导入历史消息
     *
     * @param contextMessages 最终message
     */
    private void setHistoryConversation(List<Message> contextMessages) {
        int startIndex = Math.max(0, fullConversationHistory.size() - MAX_CONTEXT_MESSAGES);
        for (int i = startIndex; i < fullConversationHistory.size(); i++) {
            Message message = fullConversationHistory.get(i);
            contextMessages.add(message);
        }
    }

    /**
     * 创建api消息
     *
     * @param messages 所有消息
     * @return api返回消息
     */
    public GenerationParam createGenerationParam(List<Message> messages) throws IOException, ClassNotFoundException {
        return GenerationParam.builder()
                .model(AIChatConstants.QWEN_MODEL)
                .messages(messages)
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .topP(apiParamsUtils.loadApiParams().getTopP())
                .topK(apiParamsUtils.loadApiParams().getTopK())
                .repetitionPenalty(apiParamsUtils.loadApiParams().getRepetitionPenalty())
                .temperature(apiParamsUtils.loadApiParams().getTemperature())
                .apiKey(apiKey)
                .build();
    }

    /**
     * 发送消息，获取ai回复
     * @param param 封装好的api消息
     * @return ai回复
     * @throws ApiException api异常
     * @throws NoApiKeyException 没有api key异常
     * @throws InputRequiredException 输入异常
     */
    private static GenerationResult callGenerationWithMessages(GenerationParam param) throws ApiException, NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        return gen.call(param);
    }
}
