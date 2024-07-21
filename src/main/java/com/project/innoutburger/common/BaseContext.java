package com.project.innoutburger.common;

/*
* 基於 ThreadLocal 封裝工具類, 用於保存和獲取當前登錄用戶的 id
* */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();


    /*
    * 設置值
    * @param id
    * */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }


    /*
     * 獲取值
     * @param id
     * */
    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
