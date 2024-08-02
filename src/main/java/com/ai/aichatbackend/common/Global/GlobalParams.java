package com.ai.aichatbackend.common.Global;

import com.alibaba.dashscope.common.Message;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局共享变量
 *
 * @author MAC1713
 * @email 1172820376@qq.com
 * @date 2024-07-26 03:11:04
 */
@Data
@Component
public class GlobalParams {
    private static GlobalParams instance;
    private String message;

    private Integer messageCount = 0;

    private List<Message> fullConversationHistory = new ArrayList<>();

    private GlobalParams() {
        // 私有构造函数防止外部实例化
    }

    public static synchronized GlobalParams getInstance() {
        if (instance == null) {
            instance = new GlobalParams();
        }
        return instance;
    }
}