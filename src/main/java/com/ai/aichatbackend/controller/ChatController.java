package com.ai.aichatbackend.controller;


import com.ai.aichatbackend.common.Constants.R;
import com.ai.aichatbackend.common.Global.GlobalParams;
import com.ai.aichatbackend.domain.AllConversation;
import com.ai.aichatbackend.domain.ApiParams;
import com.ai.aichatbackend.service.ChatService;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


/**
 * @author mac
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    /**
     * 与ai对话
     *
     * @param allConversation 所有信息
     * @return ai回应
     */
    @PostMapping("/send")
    public R sendMessage(@RequestBody AllConversation allConversation) throws NoApiKeyException, InputRequiredException, IOException, ClassNotFoundException {
        AllConversation response = new AllConversation();
        response.setAiMessage(chatService.sendMessage(allConversation));
        response.setMessageCount(GlobalParams.getInstance().getMessageCount());
        response.setFullConversationHistory(GlobalParams.getInstance().getFullConversationHistory());
        return R.ok(response);
    }

    @PostMapping("/updateNotebook")
    public R updateNotebook(@RequestBody AllConversation allConversation) throws NoApiKeyException, InputRequiredException, IOException, ClassNotFoundException {
        return chatService.updateNotebook(allConversation);
    }

    /**
     * 设置api对话参数
     * @param apiParams api参数
     * @return api参数
     * @throws IOException 创建文件失败
     */
    @PostMapping("/setApiParams")
    public R setApiParams(@RequestBody ApiParams apiParams) throws IOException {
        chatService.setApiParams(apiParams);
        return R.ok(apiParams);
    }
}
