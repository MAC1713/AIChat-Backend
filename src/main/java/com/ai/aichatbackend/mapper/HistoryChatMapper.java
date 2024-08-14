package com.ai.aichatbackend.mapper;

import com.ai.aichatbackend.domain.HistoryChat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author mac
 */
@Mapper
public interface HistoryChatMapper extends BaseMapper<HistoryChat> {
    /**
     * 使用会话id存储历史记录
     *
     * @param historyChat 历史记录
     * @return int
     */
    int saveChatHistory(HistoryChat historyChat);

}
