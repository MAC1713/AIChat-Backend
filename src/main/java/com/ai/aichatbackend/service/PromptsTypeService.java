package com.ai.aichatbackend.service;

import com.ai.aichatbackend.domain.PromptsType;

import java.util.List;


/**
 * (PromptsType)表服务接口
 *
 * @author sunhr
 * @since 2024-08-19 09:25:46
 */
public interface PromptsTypeService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    PromptsType queryById(String id);

    /**
     * 分页查询
     *
     * @param promptsType 筛选条件
     * @return 查询结果
     */
    List<PromptsType> getPromptsTypeList(PromptsType promptsType);

    /**
     * 新增数据
     *
     * @param promptsType 实例对象
     * @return 实例对象
     */
    PromptsType save(PromptsType promptsType);

    /**
     * 修改数据
     *
     * @param promptsType 实例对象
     * @return 实例对象
     */
    PromptsType edit(PromptsType promptsType);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

}
