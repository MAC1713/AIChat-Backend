package com.ai.aichatbackend.controller;

import com.ai.aichatbackend.common.Constants.R;
import com.ai.aichatbackend.service.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author mac
 */
@RestController
@RequestMapping("/api/apikey")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @Autowired
    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @GetMapping("/getApiKey")
    public R getApiKey() {
        return R.ok(apiKeyService.getApiKey());
    }

    @PostMapping("/saveApiKey")
    public R saveApiKey(@RequestBody String apiKey) {
        return R.ok(apiKeyService.saveApiKey(apiKey));
    }
}
