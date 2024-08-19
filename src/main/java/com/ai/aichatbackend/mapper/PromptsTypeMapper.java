package com.ai.aichatbackend.mapper;

import com.ai.aichatbackend.domain.PromptsType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * (PromptsType)表数据库访问层
 *
 * @author sunhr
 * @since 2024-08-19 09:42:49
 */
@Mapper
public interface PromptsTypeMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    PromptsType queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param promptsType 查询条件
     * @return 对象列表
     */
    List<PromptsType> getPromptsTypeList(PromptsType promptsType);

    /**
     * 新增数据
     *
     * @param promptsType 实例对象
     * @return 影响行数
     */
    int save(PromptsType promptsType);

    /**
     * 修改数据
     *
     * @param promptsType 实例对象
     * @return 影响行数
     */
    int edit(PromptsType promptsType);
    
    /**
     * 通过主键删除数据(Real)
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteByIdReal(String id);
}

