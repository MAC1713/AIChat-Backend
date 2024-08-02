package com.ai.aichatbackend.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author mac
 */
@Data
public class AIPrompts implements Serializable {

    @Serial
    private static final long serialVersionUID = 4731601646097146173L;
    private String initialSystemPrompt;
    private String simplifiedSystemPrompt;

    public AIPrompts(String initialSystemPrompt, String simplifiedSystemPrompt) {
        this.initialSystemPrompt = initialSystemPrompt;
        this.simplifiedSystemPrompt = simplifiedSystemPrompt;
    }
}