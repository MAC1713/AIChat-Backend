package com.ai.aichatbackend.controller;

import com.ai.aichatbackend.common.Constants.R;
import com.ai.aichatbackend.domain.PromptsType;
import com.ai.aichatbackend.service.PromptsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    public PromptsTypeController(PromptsTypeService promptsTypeService) {
        this.promptsTypeService = promptsTypeService;
    }

    /**
     * 全部查询
     *
     * @return 查询结果
     */
    @PostMapping("/getPromptsTypeList")
    public R getPromptsTypeList(@RequestBody PromptsType promptsType) {
        return R.ok(promptsTypeService.getPromptsTypeList(promptsType));
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

