package com.ai.aichatbackend.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author mac
 */
@Mapper
public interface ApiKeyDao {

    /**
     * 获取apikey
     * @return apikey
     */
    String selectAll();

    /**
     * 保存apikey
     * @param apiKey apikey
     * @return 成功条数
     */
    Integer saveApiKey(String apiKey);

    /**
     * 清空表
     */
    void deleteApiKey();
}
