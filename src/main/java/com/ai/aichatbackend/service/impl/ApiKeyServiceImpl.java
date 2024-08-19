package com.ai.aichatbackend.service.impl;

import com.ai.aichatbackend.mapper.ApiKeyMapper;
import com.ai.aichatbackend.service.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mac
 */
@Service
public class ApiKeyServiceImpl implements ApiKeyService {

    @Autowired
    private ApiKeyMapper apiKeyMapper;

    /**
     * 获取apiKey
     *
     * @return apiKey
     */
    @Override
    public String getApiKey() {
        return apiKeyMapper.selectAll();
    }

    /**
     * 保存apiKey
     *
     * @param apiKey apiKey
     * @return 成功条数
     */
    @Override
    public Integer saveApiKey(String apiKey) {
        if (apiKeyMapper.selectAll() != null) {
            apiKeyMapper.deleteApiKey();
        }
        return apiKeyMapper.saveApiKey(apiKey);
    }


}
