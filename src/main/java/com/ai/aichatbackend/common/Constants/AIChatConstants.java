package com.ai.aichatbackend.common.Constants;

import com.ai.aichatbackend.aop.AutoFetchPrompt;

import java.io.Serial;
import java.io.Serializable;

/**
 * 管理关键常量
 *
 * @author MAC1713
 * @email 1172820376@qq.com
 * @date 2024-07-25 04:46:38
 */
public class AIChatConstants implements Serializable {
    @Serial
    private static final long serialVersionUID = -2695603405032830178L;

    public static final String QWEN_MODEL_LONGTEXT = "qwen-max-longcontext";
    // Emma的最大token限制
    public static final int LONGTEXT_MAX_TOKENS = 28000;

    public static final String QWEN_MODEL_SMARTEST = "qwen-max";
    // Smart助手的最大token限制
    public static final int SMARTEST_MAX_TOKENS = 6000;

    public static final int MAX_CONTEXT_MESSAGES = 30;
    public static final int REMINDER_INTERVAL = 10;
    public static final int TIME_TO_COLLATION = 5;
    public static final String PARAMS_FILE = "./src/main/resources/document/api_params.ser";
    public static final String PROMPTS_FILE = "./src/main/resources/document/ai_prompts.ser";
    public static final String NOTEBOOK_FILE = "./src/main/resources/document/ai_notebook.txt";
    public static final String DIARY_FILE = "./src/main/resources/document/ai_diary.txt";
    @AutoFetchPrompt(promptIdField = "INITIAL_SYSTEM_PROMPT")
    public static String INITIAL_SYSTEM_PROMPT;

    @AutoFetchPrompt(promptIdField = "INITIAL_SYSTEM_PROMPT_TOKEN", type = "int")
    public static String INITIAL_SYSTEM_PROMPT_TOKEN;

    @AutoFetchPrompt(promptIdField = "SIMPLIFIED_SYSTEM_PROMPT")
    public static String SIMPLIFIED_SYSTEM_PROMPT;

    @AutoFetchPrompt(promptIdField = "SIMPLIFIED_SYSTEM_PROMPT_TOKEN", type = "int")
    public static String SIMPLIFIED_SYSTEM_PROMPT_TOKEN;
}
