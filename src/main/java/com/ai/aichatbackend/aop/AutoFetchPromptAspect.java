package com.ai.aichatbackend.aop;

import com.ai.aichatbackend.common.Constants.AIAssistantConstants;
import com.ai.aichatbackend.domain.Prompts;
import com.ai.aichatbackend.service.PromptsService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author mac
 */
@Slf4j
@Aspect
@Component
public class AutoFetchPromptAspect {

    private final PromptsService promptsService;

    @Autowired
    public AutoFetchPromptAspect(PromptsService promptsService) {
        this.promptsService = promptsService;
    }

    @Before("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void beforeControllerMethod(JoinPoint joinPoint) {
        Class<?> clazz = AIAssistantConstants.class;
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(AutoFetchPrompt.class)) {
                AutoFetchPrompt annotation = field.getAnnotation(AutoFetchPrompt.class);
                String promptId = annotation.promptIdField();
                String type = annotation.type();

                field.setAccessible(true);

                try {
                    Prompts prompt = promptsService.getPromptsById(promptId);
                    if ("int".equals(type)) {
                        if (prompt != null) {
                            Integer token = prompt.getTokens();
                            if (token != null) {
                                field.set(null, token);
                            } else {
                                log.warn("Token is null for promptId: {}", promptId);
                            }
                        } else {
                            log.warn("No prompt found for promptId: {}", promptId);
                        }
                    } else {
                        if (prompt != null) {
                            String promptData = prompt.getPromptData();
                            if (promptData != null) {
                                field.set(null, promptData);
                            } else {
                                log.warn("PromptData is null for promptId: {}", promptId);
                            }
                        } else {
                            log.warn("No prompt found for promptId: {}", promptId);
                        }
                    }
                } catch (IllegalAccessException e) {
                    log.error("Error setting field value for {}", field.getName(), e);
                } catch (Exception e) {
                    log.error("Unexpected error occurred while processing field {}", field.getName(), e);
                }
            }
        }
    }
}
