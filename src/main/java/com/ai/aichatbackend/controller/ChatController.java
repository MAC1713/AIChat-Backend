package com.ai.aichatbackend.controller;


import com.ai.aichatbackend.common.Constants.R;
import com.ai.aichatbackend.common.Global.GlobalParams;
import com.ai.aichatbackend.domain.AllConversation;
import com.ai.aichatbackend.domain.ApiParams;
import com.ai.aichatbackend.domain.HistoryChat;
import com.ai.aichatbackend.service.ChatService;
import com.ai.aichatbackend.service.HistoryChatService;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


/**
 * @author mac
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private HistoryChatService historyChatService;

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
    @PostMapping("/syncHistory")
    public R syncChatHistory(@RequestBody HistoryChat historyChat) {
        return R.ok(historyChatService.saveChatHistory(historyChat));
    }

    @GetMapping("/history/{conversationId}")
    public R getChatHistory(@PathVariable String conversationId) {
        List<Message> history = historyChatService.getChatHistory(conversationId);
        return R.ok(history);
    }

    @PostMapping("/updateNotebook")
    public R updateNotebook(@RequestBody AllConversation allConversation) throws NoApiKeyException, InputRequiredException, IOException, ClassNotFoundException {
        return chatService.updateNotebook(allConversation);
    }

    /**
     * 设置api对话参数
     *
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
