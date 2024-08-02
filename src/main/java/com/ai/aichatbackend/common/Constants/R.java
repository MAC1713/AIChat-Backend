package com.ai.aichatbackend.common.Constants;

import lombok.SneakyThrows;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mac
 */
public class R extends HashMap<String, Object> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static R ok(Object data) {
        R r = new R();
        r.put("head", successHead());
        r.put("result", data);
        return r;
    }

    public static R okEncrypt(Object data) {
        R r = new R();
        r.put("head", successHead());
        r.put("result", data);
        r.put("encrypt", true);
        return r;
    }

    public static R ok() {
        R r = new R();
        r.put("head", successHead());
        r.put("result", new HashMap<>());
        return r;
    }

    /**
     * 金皓-为了适配前端stable无分页列表
     *
     * @param data
     * @return
     */
    public static R listOk(Object data) {
        R r = new R();
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("data", data);
        r.put("head", successHead());
        r.put("result", pageMap);
        return r;
    }

    /**
     * 金皓-单独通过IPage拦截分页方法设置值，适配前端分页数据渲染
     *
     * @param data
     * @return
     */
    @SneakyThrows
    public static R page(Object data) {
        R r = new R();
        Map<String, Object> pageMap = new HashMap<>();
        Class jClass = data.getClass();
        // 此处通过反射获取固定的方法并执行
        Method getRecords = jClass.getMethod("getRecords", (Class<?>) null);
        Object list = getRecords.invoke(data);
        Method getCurrent = jClass.getMethod("getCurrent", (Class<?>) null);
        long current = (long) getCurrent.invoke(data);
        Method getSize = jClass.getMethod("getSize", (Class<?>) null);
        long size = (long) getSize.invoke(data);
        Method getTotal = jClass.getMethod("getTotal", (Class<?>) null);
        long total = (long) getTotal.invoke(data);
        Method getPages = jClass.getMethod("getPages", (Class<?>) null);
        long pages = (long) getPages.invoke(data);
        pageMap.put("data", list);
        pageMap.put("pageNo", current);
        pageMap.put("pageSize", size);
        pageMap.put("totalCount", total);
        pageMap.put("totalPage", pages);
        r.put("head", successHead());
        r.put("result", pageMap);
        return r;
    }

    private static R successHead(String msg) {
        R r = new R();
        r.put("message", msg);
        r.put("status", ResultCodeEnum.RES_CODE_SUCCESS.getCode());
        return r;
    }

    private static R successHead() {
        R r = new R();
        r.put("message", ResultCodeEnum.RES_CODE_SUCCESS.getMsg());
        r.put("status", ResultCodeEnum.RES_CODE_SUCCESS.getCode());
        return r;
    }

    private static Map<String, Object> failHead(String message, String status) {

        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("status", status);
        return map;
    }

    public static R error(String message, String status, Object data) {
        R r = new R();
        r.put("head", failHead(message, status));
        r.put("result", data);
        return r;
    }

    public static R error(String message, String status) {
        R r = new R();
        r.put("head", failHead(message, status));
        r.put("result", new HashMap<>());
        return r;
    }

    public static R error(String message) {
        R r = new R();
        r.put("head", failHead(message, ResultCodeEnum.RES_CODE_NO_KNOW.getCode()));
        r.put("result", null);
        return r;
    }

    public static R error(ResultCodeEnum resultCodeEnum) {
        R r = new R();
        r.put("head", failHead(resultCodeEnum.getMsg(), resultCodeEnum.getCode()));
        r.put("result", new HashMap<>());
        return r;
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}
