package com.ai.aichatbackend.service.impl;

import com.ai.aichatbackend.common.Global.GlobalParams;
import com.ai.aichatbackend.domain.AllConversation;
import com.ai.aichatbackend.domain.ApiParams;
import com.ai.aichatbackend.service.ChatService;
import com.ai.aichatbackend.utils.ApiParamsUtils;
import com.ai.aichatbackend.utils.MessageUtils;
import com.ai.aichatbackend.utils.NotebookAssistant;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.ai.aichatbackend.common.Constants.AIChatConstants.TIME_TO_COLLATION;

/**
 * @author mac
 */
@Service
public class ChatServiceImpl implements ChatService {
    private final MessageUtils messageUtils;
    private final ApiParamsUtils apiParamsUtils;
    private final NotebookAssistant notebookAssistant;

    public ChatServiceImpl(MessageUtils messageUtils, ApiParamsUtils apiParamsUtils, NotebookAssistant notebookAssistant) {
        this.messageUtils = messageUtils;
        this.apiParamsUtils = apiParamsUtils;
        this.notebookAssistant = notebookAssistant;
    }

    @Override
    public String sendMessage(AllConversation allConversation) throws NoApiKeyException, InputRequiredException, IOException, ClassNotFoundException {
        initialParams(allConversation);

        //获取用户输入
        String userInput = allConversation.getUserMessage();
        //获取当前总对话量
        int messageCount = allConversation.getMessageCount();

        //如果当前对话量是（TIME_TO_COLLATION：5）的倍数，发送整理提示信息
        if (messageCount % TIME_TO_COLLATION == 0 && messageCount > 0) {
            notebookAssistant.collection();
        }
        //如果用户信息不为空
        if (StringUtils.isNotBlank(userInput)) {
            try {
                //1. 发送用户信息
                String aiResponse = messageUtils.sendMessage(Role.USER, userInput, true);
                //3. 助手检测记事本内容
                notebookAssistant.sendToCheck(userInput, aiResponse);
                return ("Emma: " + aiResponse + "\n\n");
            } catch (ApiException | NoApiKeyException | InputRequiredException ex) {
                return ("Error: " + ex.getMessage() + "\n\n");
            }
        }
        return "Emma don't know what you want and keep silent. Please try again.";
    }

    /**
     * 设置对话参数
     *
     * @param apiParams 对话参数
     */
    @Override
    public void setApiParams(ApiParams apiParams) throws IOException {
        apiParamsUtils.saveApiParams(apiParams);
    }

    private void initialParams(AllConversation allConversation) {
        GlobalParams.getInstance().setMessage(allConversation.getUserMessage());
        GlobalParams.getInstance().setMessageCount(allConversation.getMessageCount());
        GlobalParams.getInstance().setFullConversationHistory(allConversation.getFullConversationHistory());
    }

}