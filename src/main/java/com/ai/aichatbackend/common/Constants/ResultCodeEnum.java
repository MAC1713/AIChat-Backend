package com.ai.aichatbackend.common.Constants;

import com.alibaba.fastjson2.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mac
 */

public enum ResultCodeEnum {
    /**
     * 枚举各种返回值
     */
    RES_CODE_SUCCESS("200", "接口调用成功"),
    RES_CODE_ERROR("998", "系统异常，请稍后再试！"),
    RES_CODE_PARAMS_ERROR("997", "%s参数不能为空！"),
    RES_CODE_NO_TOKEN("400", "无效访问,token丢失"),
    RES_CODE_NO_LOGIN("401", "未登录"),
    RES_CODE_LOGIN_FAIL("401.1", "登录失败"),
    RES_CODE_NO_POWER("401.5", "此用户没有权限调用该接口"),

    RES_CODE_WRONG_ACCOUNT("401.6", "账号或密码不正确"),
    RES_CODE_LOCK_ACCOUNT("401.7", "账号已被锁定,请联系管理员"),
    RES_CODE_NO_ACCOUNT("401.8", "账号或密码不正确"),
    RES_CODE_PASS_ERROR("B0002", "原始密码错误"),
    RES_CODE_EXPIRE("401.9", "登录过期"),
    RES_OAUTH_IS_NULL("401.11", "用户信息获取失败"),
    RES_CODE_GET_MENU_FAIL("401.10", "获取菜单失败"),
    RES_CODE_NOT_COMPLETE("403.18", "禁止访问，数据不完整"),
    RES_CODE_NO_PARAMETER("403.3", "禁止访问 ,要求必填的输入参数值为空"),
    RES_CODE_403("403.3", "用户无权限,非法用户!"),
    RES_CODE_FORBIDDEN("404.1", "无法找到 Web 站点,此服务停用"),
    RES_CODE_PAGE("500-16", "分页异常"),
    RES_CODE_SAVE("500-17", "信息保存错误，请联系管理员"),

    RES_CODE_TEL("500-18", "手机号已存在，请重新录入"),

    RES_CODE_COUNT("50019", "请求过于频繁，休息一下吧"),
    RES_CODE_NO_KNOW("500-99", "未知异常"),
    RES_CODE_NO_RESULT("200.4", "未查询到结果"),
    RES_CODE_PART_SUCCESS("200.5", "未查询到结果"),
    RES_CODE_WHRONG_CHECK("200.6", "未通过校验"),

    RES_CODE_APPLICATION("999", "应用未开通或已停用,请联系管理人员");


    private String code;
    private String msg;

    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getMsgByCode(String code) {
        ResultCodeEnum[] values = ResultCodeEnum.values();
        for (ResultCodeEnum resultCodeEnum : values) {
            if (resultCodeEnum.code.equals(code)) {
                return resultCodeEnum.msg;
            }
        }
        return code;
    }

    public static String getResultJsonByCode(String code) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", code);
        map.put("msg", getMsgByCode(code));
        return JSON.toJSONString(map);
    }

    public Map<String, Object> getResultMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }

    public String getResultJson() {
        return JSON.toJSONString(getResultMap());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
