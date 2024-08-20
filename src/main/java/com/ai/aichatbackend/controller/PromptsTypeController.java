package com.ai.aichatbackend.controller;

import com.ai.aichatbackend.common.Constants.R;
import com.ai.aichatbackend.domain.PromptsAll;
import com.ai.aichatbackend.domain.PromptsType;
import com.ai.aichatbackend.service.PromptsService;
import com.ai.aichatbackend.service.PromptsTypeService;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * (PromptsType)表控制层
 *
 * @author sunhr
 * @since 2024-08-19 09:27:13
 */
@RestController
@RequestMapping("/api/promptsType/")
public class PromptsTypeController {

    private final PromptsTypeService promptsTypeService;

    private final PromptsService promptsService;

    @Autowired
    public PromptsTypeController(PromptsTypeService promptsTypeService, PromptsService promptsService)  {
        this.promptsTypeService = promptsTypeService;
        this.promptsService = promptsService;
    }

    /**
     * 全部查询
     *
     * @return 查询结果
     */
    @PostMapping("/getPromptsTypeList")
    public R getPromptsTypeList(@RequestBody PromptsType promptsType) throws NoApiKeyException, InputRequiredException {
        List<PromptsType> promptsTypeList = promptsTypeService.getPromptsTypeList(promptsType);
        List<PromptsAll> list = new ArrayList<>();

        for (PromptsType type : promptsTypeList) {
            PromptsAll promptsAll = new PromptsAll();

            promptsAll.setId(type.getId());
            promptsAll.setPromptType(type.getPromptsType());
            promptsAll.setDescription(type.getDescription());
            promptsAll.setUpdateTime(type.getUpdateTime());

            promptsAll.setPromptData(promptsService.getPromptsById(type.getPromptsType()).getPromptData());
            promptsAll.setTokens(promptsService.getPromptsById(type.getPromptsType()).getTokens());

            list.add(promptsAll);
        }

        return R.ok(list);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/queryById/{id}")
    public R queryById(@PathVariable String id) {
        return R.ok(promptsTypeService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param promptsType 实体
     * @return 新增结果
     */
    @PostMapping("/save")
    public R save(@RequestBody PromptsType promptsType) {
        return R.ok(this.promptsTypeService.save(promptsType));
    }

    /**
     * 编辑数据
     *
     * @param promptsType 实体
     * @return 编辑结果
     */
    @PostMapping("/edit")
    public R edit(@RequestBody PromptsType promptsType) {
        return R.ok(this.promptsTypeService.edit(promptsType));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @GetMapping("/deleteById/{id}")
    public R deleteById(@PathVariable String id) {
        return R.ok(this.promptsTypeService.deleteById(id));
    }

}

