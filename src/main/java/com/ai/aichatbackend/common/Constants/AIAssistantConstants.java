package com.ai.aichatbackend.common.Constants;

import com.ai.aichatbackend.aop.AutoFetchPrompt;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author mac
 */
@Data
public class AIAssistantConstants implements Serializable {
    @Serial
    private static final long serialVersionUID = -913043819765235806L;

    @AutoFetchPrompt(promptIdField = "USER_NAME")
    public static String USER_NAME;
    @AutoFetchPrompt(promptIdField = "AI_NAME")
    public static String AI_NAME;

    @AutoFetchPrompt(promptIdField = "HOW_TO_USE_NOTEBOOK")
    public static String HOW_TO_USE_NOTEBOOK;

    @AutoFetchPrompt(promptIdField = "HOW_TO_USE_NOTEBOOK", type = "int")
    public static int HOW_TO_USE_NOTEBOOK_TOKEN;

    @AutoFetchPrompt(promptIdField = "REPEAT_NOTEBOOK")
    public static String REPEAT_NOTEBOOK;

    @AutoFetchPrompt(promptIdField = "NECESSARY_TO_NOTE")
    public static String NECESSARY_TO_NOTE;
    @AutoFetchPrompt(promptIdField = "COLLATION")
    public static String COLLATION;
    @AutoFetchPrompt(promptIdField = "COLLATION", type = "int")
    public static int COLLATION_TOKEN;
}
