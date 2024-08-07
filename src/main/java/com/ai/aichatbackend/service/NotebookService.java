package com.ai.aichatbackend.service;

import com.ai.aichatbackend.common.Constants.R;
import com.ai.aichatbackend.domain.Notebook;

import java.util.List;

/**
 * @author mac
 */
public interface NotebookService {
    /**
     * 获取笔记本列表
     *
     * @return 笔记本列表
     */
    List<Notebook> getNotebook();

    /**
     * 保存笔记本列表
     *
     * @param notebooks 笔记本列表
     * @return 保存结果
     */
    R saveNotebook(List<Notebook> notebooks);

    /**
     * 清理笔记本
     *
     * @param notebook 笔记本参数
     * @return 清理结果
     */
    R cleanNotebook(Notebook notebook);
}
