package com.ai.aichatbackend.service.impl;

import com.ai.aichatbackend.mapper.ApiKeyDao;
import com.ai.aichatbackend.service.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mac
 */
@Service
public class ApiKeyServiceImpl implements ApiKeyService {

    @Autowired
    private ApiKeyDao apiKeyDao;

    /**
     * 获取apiKey
     *
     * @return apiKey
     */
    @Override
    public String getApiKey() {
        return apiKeyDao.selectAll();
    }

    /**
     * 保存apiKey
     *
     * @param apiKey apiKey
     * @return 成功条数
     */
    @Override
    public Integer saveApiKey(String apiKey) {
        if (apiKeyDao.selectAll() != null) {
            apiKeyDao.deleteApiKey();
        }
        return apiKeyDao.saveApiKey(apiKey);
    }


}
