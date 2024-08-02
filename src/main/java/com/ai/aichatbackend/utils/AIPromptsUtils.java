package com.ai.aichatbackend.utils;

import com.ai.aichatbackend.common.Constants.AIChatConstants;
import com.ai.aichatbackend.domain.AIPrompts;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * @author mac
 */
@Component
public class AIPromptsUtils {
    /**
     * 将提示词保存到文件中
     * @param prompts AIPrompts对象，包含初始系统提示词和简化系统提示词
     * @throws IOException 如果发生I/O错误
     */
    public void savePrompts(AIPrompts prompts) throws IOException {
        File file = new File(AIChatConstants.PROMPTS_FILE);
        file.getParentFile().mkdirs();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(prompts);
        }
    }

    /**
     * 从文件中读取提示词
     * @return AIPrompts对象，包含初始系统提示词和简化系统提示词
     * @throws IOException 如果发生I/O错误
     * @throws ClassNotFoundException 如果类未找到
     */
    public AIPrompts loadPrompts() throws IOException, ClassNotFoundException {
        File file = new File(AIChatConstants.PROMPTS_FILE);
        if (!file.exists() || file.length() == 0) {
            return new AIPrompts(AIChatConstants.INITIAL_SYSTEM_PROMPT, AIChatConstants.SIMPLIFIED_SYSTEM_PROMPT);
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (AIPrompts) ois.readObject();
        }
    }
}
