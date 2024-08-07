package com.ai.aichatbackend.service.impl;

import com.ai.aichatbackend.common.Constants.R;
import com.ai.aichatbackend.domain.Notebook;
import com.ai.aichatbackend.service.NotebookService;
import com.ai.aichatbackend.utils.NotebookUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mac
 */
@Service
public class NotebookServiceImpl implements NotebookService {

    private final NotebookUtils notebookUtils;

    public NotebookServiceImpl(NotebookUtils notebookUtils) {
        this.notebookUtils = notebookUtils;
    }

    /**
     * 获取笔记本列表
     *
     * @return 笔记本列表
     */
    @Override
    public List<Notebook> getNotebook() {
        return notebookUtils.getNotes();
    }

    /**
     * 保存笔记本列表
     *
     * @param notebooks 笔记本列表
     * @return 保存结果
     */
    @Override
    public R saveNotebook(List<Notebook> notebooks) {
        return notebookUtils.saveNotes(notebooks);
    }

    /**
     * 清理笔记本
     *
     * @param notebook 笔记本参数
     * @return 清理结果
     */
    @Override
    public R cleanNotebook(Notebook notebook) {
        return notebookUtils.cleanupNotes(notebook);
    }
}
