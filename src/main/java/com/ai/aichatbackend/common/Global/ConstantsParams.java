package com.ai.aichatbackend.common.Global;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author mac
 */
@Data
@Component
public class ConstantsParams {
    private static ConstantsParams instance;

    private int chatSystemPromptsToken = 0;
    private int chatSimpleSystemPromptsToken = 0;
    private int employeeCollectionToken = 0;
    private int assistantNotebookToken = 0;
    private int assistantCollation = 0;
    private int systemToken = 0;
    private int notebookToken = 0;
    private int diaryToken = 0;

    private ConstantsParams() {
        // 私有构造函数防止外部实例化
    }

    public static synchronized ConstantsParams getInstance() {
        if (instance == null) {
            instance = new ConstantsParams();
        }
        return instance;
    }
}

