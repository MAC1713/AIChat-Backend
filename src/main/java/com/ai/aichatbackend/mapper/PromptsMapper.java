package com.ai.aichatbackend.mapper;

import com.ai.aichatbackend.domain.Prompts;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author mac
 */
@Mapper
public interface PromptsMapper extends BaseMapper<Prompts> {

    /**
     * 查询所有提示词
     * @return 提示词列表
     */
    List<Prompts> selectList();

    /**
     * 根据类型查询提示词
     * @param promptType 提示词类型
     * @return 提示词
     */
    Prompts selectByType(String promptType);
}
