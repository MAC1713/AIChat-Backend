package com.ai.aichatbackend.utils;

import com.ai.aichatbackend.common.Constants.R;
import com.ai.aichatbackend.common.Global.ConstantsParams;
import com.ai.aichatbackend.common.Global.GlobalParams;
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

import static com.ai.aichatbackend.common.Constants.AIAssistantConstants.*;
import static com.ai.aichatbackend.common.Constants.AIChatConstants.*;
import static com.ai.aichatbackend.common.Constants.AIEmployeeConstants.HOW_TO_COLLECTION_NOTEBOOK;

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
    private final DiaryUtils diaryUtils;
    private final TokenCalculator tokenCalculator;

    @Autowired
    private MessageUtils(NotebookUtils notebookUtils, ApiParamsUtils apiParamsUtils, AIPromptsUtils aiPromptsUtils, TokenCalculator tokenCalculator, DiaryUtils diaryUtils) {
        this.notebookUtils = notebookUtils;
        this.apiParamsUtils = apiParamsUtils;
        this.aiPromptsUtils = aiPromptsUtils;
        this.tokenCalculator = tokenCalculator;
        this.diaryUtils = diaryUtils;
    }

    private List<Message> fullConversationHistory;

    private int messageCountSinceLastReminder;

    private static Message createMessage(Role role, String content) {
        return Message.builder().role(role.getValue()).content(content).build();
    }

    public String generateResponse(String inputMessage, GenerationParam params) throws ApiException, NoApiKeyException, InputRequiredException, IOException, ClassNotFoundException {
        Message userMessage = createMessage(Role.USER, inputMessage);
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

    public String sendMessage(Role role, String message) throws NoApiKeyException, InputRequiredException, IOException, ClassNotFoundException {
        try {
            List<Message> messages = new ArrayList<>();
            messages.add(createMessage(role, message));
            return generateResponse(message, createGenerationParam(messages));
        } catch (ApiException | NoApiKeyException | InputRequiredException | IOException | ClassNotFoundException ex) {
            log.error("Error occurred while sending special message: " + ex.getMessage());
            throw ex;
        }
    }

    public String sendEmployeeMessage(String message) throws NoApiKeyException, InputRequiredException, IOException, ClassNotFoundException {
        List<Message> messages = new ArrayList<>();

        // 1. 添加系统消息
        String systemPrompt = aiPromptsUtils.loadPrompts().getInitialSystemPrompt() + "\n" + HOW_TO_USE_NOTEBOOK;
        messages.add(createMessage(Role.SYSTEM, systemPrompt));

        // 2. 添加记事本内容
        String notebookContent = "Here's the all of your notebook:\n" + notebookUtils.getFormattedNotes();
        messages.add(createMessage(Role.SYSTEM, notebookContent));

        // 3. 添加用户消息
        messages.add(createMessage(Role.USER, message));

        GenerationParam params = createGenerationParam(messages);


        try {
            GenerationResult result = callGenerationWithMessages(params);
            String aiResponse = result.getOutput().getChoices().get(0).getMessage().getContent();
            ConstantsParams.getInstance().setDiaryToken(result.getUsage().getOutputTokens());
            return aiResponse;
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            log.error("Error occurred while calling generation API: " + e.getMessage());
            throw e;
        }
    }

    public R sendNoteAssistantMessage(String message) throws NoApiKeyException, InputRequiredException, IOException, ClassNotFoundException {
        List<Message> messages = new ArrayList<>();

        // 1. 添加系统消息
        String systemPrompt = aiPromptsUtils.loadPrompts().getInitialSystemPrompt() + "\n" + HOW_TO_USE_NOTEBOOK;
        messages.add(createMessage(Role.SYSTEM, systemPrompt));
        int systemToken = ConstantsParams.getInstance().getChatSystemPromptsToken() + ConstantsParams.getInstance().getAssistantNotebookToken();
        ConstantsParams.getInstance().setSystemToken(systemToken);

        // 2. 添加记事本内容
        String notebookContent = "Here's the all of your notebook:\n" + notebookUtils.getFormattedNotes();
        messages.add(createMessage(Role.SYSTEM, notebookContent));
        int notebookToken = ConstantsParams.getInstance().getNotebookToken();


        // 3. 计算剩余可用于历史消息的token数
        int remainingTokens = SMARTEST_MAX_TOKENS - systemToken - notebookToken;
        if (remainingTokens < SMARTEST_MAX_TOKENS / 6) {
            //整理记事本->生成日记
            diaryUtils.saveDiary(sendEmployeeMessage(HOW_TO_COLLECTION_NOTEBOOK));
            // 移除最后一条消息
            messages.remove(messages.size() - 1);
            messages.add(createMessage(Role.SYSTEM, diaryUtils.getDiary()));
        }

        int diaryToken = ConstantsParams.getInstance().getDiaryToken();

        if (systemToken + diaryToken > SMARTEST_MAX_TOKENS) {
            return R.error("超出token上限，请清理记事本");
        }

        remainingTokens = SMARTEST_MAX_TOKENS - systemToken - diaryToken;

        // 5. 添加历史消息
        List<Message> historyMessages = GlobalParams.getInstance().getFullConversationHistory();
        StringBuilder historyConversation = new StringBuilder();
        String content;
        for (int i = historyMessages.size() - 1; i >= 0; i--) {
            Message historyMessage = historyMessages.get(i);
            if (historyMessage.getRole().equals(Role.USER.getValue()) || historyMessage.getRole().equals(Role.ASSISTANT.getValue())) {
                if (historyMessage.getRole().equals(Role.USER.getValue())) {
                    content = USER_NAME + ": " + historyMessage.getContent() + "\n";
                } else {
                    content = AI_NAME + ": " + historyMessage.getContent() + "\n";
                }
                int contentTokens = tokenCalculator.tokenizer(content);

                if (remainingTokens - contentTokens >= 0) {
                    historyConversation.insert(0, content);
                    remainingTokens -= contentTokens;
                } else {
                    // 如果剩余空间不足，尝试截断最后一条消息
                    String truncatedContent = truncateToFitTokens(content, remainingTokens);
                    if (!truncatedContent.isEmpty()) {
                        historyConversation.insert(0, truncatedContent);
                    }
                    break;
                }
            }
        }

        if (historyConversation.length() > 0) {
            messages.add(createMessage(Role.SYSTEM, historyConversation.toString()));
        }

        // 6. 添加用户消息
        messages.add(createMessage(Role.USER, message));

        GenerationParam params = createSmartParam(messages);

        try {
            GenerationResult result = callGenerationWithMessages(params);
            String aiResponse = result.getOutput().getChoices().get(0).getMessage().getContent();
            notebookUtils.checkAndUpdateNotebook(aiResponse);
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            log.error("Error occurred while calling generation API: " + e.getMessage());
            throw e;
        }
        return R.ok();
    }

    /**
     * 辅助方法：截断内容以适应剩余的token数
     *
     * @param content         剩余文本
     * @param remainingTokens 剩余token数
     * @return 截断后的文本
     */
    private String truncateToFitTokens(String content, int remainingTokens) throws NoApiKeyException, InputRequiredException {
        StringBuilder truncated = new StringBuilder();
        String[] words = content.split("\\s+");
        int tokenCount = 0;

        for (String word : words) {
            int wordTokens = tokenCalculator.tokenizer(word + " ");
            if (tokenCount + wordTokens <= remainingTokens) {
                truncated.append(word).append(" ");
                tokenCount += wordTokens;
            } else {
                break;
            }
        }

        return truncated.toString().trim();
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

        String firstSystemMessage = aiPromptsUtils.loadPrompts().getInitialSystemPrompt() + "\n" + aiPromptsUtils.loadPrompts().getSimplifiedSystemPrompt();
        contextMessages.add(createMessage(Role.SYSTEM, firstSystemMessage));

        setHistoryConversation(contextMessages);

        //每5次对话提醒一次身份
        if (messageCountSinceLastReminder >= REMINDER_INTERVAL && messageCountSinceLastReminder % REMINDER_INTERVAL == 0) {
            contextMessages.add(createMessage(Role.SYSTEM, aiPromptsUtils.loadPrompts().getSimplifiedSystemPrompt()));
            fullConversationHistory.add(createMessage(Role.SYSTEM, aiPromptsUtils.loadPrompts().getSimplifiedSystemPrompt()));
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
                .model(QWEN_MODEL_LONGTEXT)
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
     * 创建smart助手消息
     *
     * @param messages 所有消息
     * @return smart助手返回消息
     */
    public GenerationParam createSmartParam(List<Message> messages) {
        return GenerationParam.builder()
                .model(QWEN_MODEL_SMARTEST)
                .messages(messages)
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                /*
                生成过程中核采样方法概率阈值，例如，取值为0.8时，仅保留概率加起来大于等于0.8的最可能token的最小集合作为候选集。
                取值范围为（0,1.0)，取值越大，生成的随机性越高；取值越低，生成的确定性越高。*/
                .topP(0.2)
                /*
                生成时，采样候选集的大小。
                例如，取值为50时，仅将单次生成中得分最高的50个token组成随机采样的候选集。取值越大，生成的随机性越高；取值越小，生成的确定性越高。
                默认值为0，表示不启用top_k策略，此时，仅有top_p策略生效。*/
                .topK(0)
                /*
                用于控制模型生成时的重复度。
                提高repetition_penalty时可以降低模型生成的重复度。1.0表示不做惩罚。*/
                .repetitionPenalty(1.01F)
                /*
                用于控制随机性和多样性的程度。具体来说，temperature值控制了生成文本时对每个候选词的概率分布进行平滑的程度。
                较高的temperature值会降低概率分布的峰值，使得更多的低概率词被选择，生成结果更加多样化；而较低的temperature值则会增强概率分布的峰值，使得高概率词更容易被选择，生成结果更加确定。
                取值范围：[0, 2)，不建议取值为0，无意义。*/
                .temperature(0.3F)
                .apiKey(apiKey)
                .build();
    }

    /**
     * 发送消息，获取ai回复
     *
     * @param param 封装好的api消息
     * @return ai回复
     * @throws ApiException           api异常
     * @throws NoApiKeyException      没有api key异常
     * @throws InputRequiredException 输入异常
     */
    private static GenerationResult callGenerationWithMessages(GenerationParam param) throws ApiException, NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        return gen.call(param);
    }
}
