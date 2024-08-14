package com.ai.aichatbackend.controller;

import com.ai.aichatbackend.common.Constants.R;
import com.ai.aichatbackend.domain.Prompts;
import com.ai.aichatbackend.service.PromptsService;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * &#064;Description  : 提示词控制器
 * @author mac
 */
@RestController
@RequestMapping("/api/prompts")
public class PromptsController {
    private final PromptsService promptsService;

    @Autowired
    public PromptsController(PromptsService promptsService) {
        this.promptsService = promptsService;
    }

    @GetMapping("/getAllPrompts")
    public R getAllPrompts(){
        return R.ok(promptsService.getAllPrompts());
    }

    @GetMapping("/getPromptsById/{id}")
    public R getPromptsById(@PathVariable String id) throws NoApiKeyException, InputRequiredException {
        return R.ok(promptsService.getPromptsById(id));
    }

    @PostMapping("/getPromptsByType")
    public R getPromptsByType(@RequestBody Prompts prompts) throws NoApiKeyException, InputRequiredException {
        return R.ok(promptsService.getPromptsByType(prompts));
    }

    @PostMapping("/savePrompts")
    public R savePrompts(@RequestBody Prompts prompts) throws NoApiKeyException, InputRequiredException {
        return R.ok(promptsService.savePrompts(prompts));
    }
}
