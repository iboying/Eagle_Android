package com.buoyantec.eagle_android;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by kang on 16/2/22.
 * 自定义帮助方法类
 */
public class ApplicationHelper {

    public ApplicationHelper() {
    }

    /**
     * 单层json数据转化为HashMap
     * 用于:设备状态详情页
     */
    public HashMap<String, String> jsonToHash(String jsonString) {
        // 定义HashMap
        HashMap<String, String> hashMap = new LinkedHashMap<>();
        // 去除首尾大括号
        jsonString = jsonString.substring(1, jsonString.length()-1);
        // 拆分成数组
        String[] datas = jsonString.split(",");
        String[] items;

        for (String data : datas) {
            items = data.split(":");
            String key = items[0].substring(1, items[0].length()-1);
            if (!(key.equals("id") || key.equals("name"))){
                String value = items[1].substring(1, items[1].length()-1);
                hashMap.put(key, value);
            }
        }

        return hashMap;
    }
}
