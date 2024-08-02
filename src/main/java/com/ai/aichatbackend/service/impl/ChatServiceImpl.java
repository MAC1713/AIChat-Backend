package com.ai.aichatbackend.service.impl;

import com.ai.aichatbackend.common.Global.GlobalParams;
import com.ai.aichatbackend.domain.AllConversation;
import com.ai.aichatbackend.domain.ApiParams;
import com.ai.aichatbackend.service.ChatService;
import com.ai.aichatbackend.utils.ApiParamsUtils;
import com.ai.aichatbackend.utils.MessageUtils;
import com.ai.aichatbackend.utils.NotebookUtils;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.ai.aichatbackend.common.Constants.AIChatConstants.*;

/**
 * @author mac
 */
@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private MessageUtils messageUtils;
    @Autowired
    private NotebookUtils notebookUtils;

    @Autowired
    private ApiParamsUtils apiParamsUtils;

    @Override
    public String sendMessage(AllConversation allConversation) throws NoApiKeyException, InputRequiredException, IOException, ClassNotFoundException {
        initialParams(allConversation);
        String userInput = allConversation.getUserMessage();
        //将用户输入保存到全局当前用户消息中
        GlobalParams.getInstance().setMessage(userInput);

        userInput = GlobalParams.getInstance().getMessage();

        //获取当前总对话量
        int messageCount = GlobalParams.getInstance().getMessageCount();
        //如果当前对话量为0，代表初始化，发送使用记事本的提示信息
        if (messageCount == 0) {
            messageUtils.sendSpecialMessage(Role.USER, REPEAT_NOTEBOOK, false);
        }
        //如果当前对话量是（TIME_TO_COLLATION：5）的倍数，发送整理提示信息
        if (messageCount % TIME_TO_COLLATION == 0 && messageCount > 0) {
            messageUtils.sendSpecialMessage(Role.USER, COLLATION, true);
        }
        //如果用户信息不为空
        if (StringUtils.isNotBlank(userInput)) {
            try {
                List<Message> messages = new ArrayList<>();
                messages.add(Message.builder().role(Role.USER.getValue()).content(userInput).build());
                String aiResponse = messageUtils.generateResponse(userInput, messageUtils.createGenerationParam(messages));
                messageUtils.checkAndUpdateNotebook(aiResponse);
                aiResponse = notebookUtils.removeNoteTags(aiResponse);
                return ("Emma: " + aiResponse + "\n\n");
            } catch (ApiException | NoApiKeyException | InputRequiredException ex) {
                return ("Error: " + ex.getMessage() + "\n\n");
            }
        }
        return "Emma don't know what you mean and keep silent. Please try again.";
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