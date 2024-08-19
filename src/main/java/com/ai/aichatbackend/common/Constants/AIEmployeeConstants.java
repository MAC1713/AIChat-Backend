package com.ai.aichatbackend.common.Constants;

import com.ai.aichatbackend.aop.AutoFetchPrompt;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author mac
 */
@Data
public class AIEmployeeConstants implements Serializable {
    @Serial
    private static final long serialVersionUID = -6598011118697079231L;

    @AutoFetchPrompt(promptIdField = "HOW_TO_COLLECTION_NOTEBOOK")
    public static String HOW_TO_COLLECTION_NOTEBOOK;

    @AutoFetchPrompt(promptIdField = "HOW_TO_COLLECTION_NOTEBOOK_TOKEN", type = "int")
    public static int HOW_TO_COLLECTION_NOTEBOOK_TOKEN;
}
