package com.ai.aichatbackend.service;

/**
 * @author mac
 */
public interface ApiKeyService {

    /**
     * 获取apiKey
     * @return apiKey
     */
    String getApiKey();

    /**
     * 保存apiKey
     * @param apiKey apiKey
     * @return 成功条数
     */
    Integer saveApiKey(String apiKey);
}
