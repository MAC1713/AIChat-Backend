package com.ai.aichatbackend.service.impl;

import com.ai.aichatbackend.domain.PromptsType;
import com.ai.aichatbackend.mapper.PromptsTypeMapper;
import com.ai.aichatbackend.service.PromptsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (PromptsType)表服务实现类
 *
 * @author sunhr
 * @since 2024-08-19 09:45:39
 */
@Service("promptsTypeService")
public class PromptsTypeServiceImpl implements PromptsTypeService {
    
    private final PromptsTypeMapper promptsTypeMapper;
    
    @Autowired
    public PromptsTypeServiceImpl(PromptsTypeMapper promptsTypeMapper) {
        this.promptsTypeMapper = promptsTypeMapper;
    }

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public PromptsType queryById(String id) {
        return this.promptsTypeMapper.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param promptsType 筛选条件
     * @return 查询结果
     */
    @Override
    public List<PromptsType> getPromptsTypeList(PromptsType promptsType) {
        return this.promptsTypeMapper.getPromptsTypeList(promptsType);
    }

    /**
     * 新增数据
     *
     * @param promptsType 实例对象
     * @return 实例对象
     */
    @Override
    public PromptsType save(PromptsType promptsType) {
        this.promptsTypeMapper.save(promptsType);
        return queryById(promptsType.getId());
    }

    /**
     * 修改数据
     *
     * @param promptsType 实例对象
     * @return 实例对象
     */
    @Override
    public PromptsType edit(PromptsType promptsType) {
        this.promptsTypeMapper.edit(promptsType);
        return this.queryById(promptsType.getId());
    }

    /**
     * 通过主键删除数据
     * Fake:修改delFlag->0
     * Real:直接删除
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.promptsTypeMapper.deleteByIdReal(id) > 0;
    }
}
