package com.pcis.smartbus.common;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {
    //quick ui的 treeNodes 需要的json数据包装
	
	
    public static JSONObject packTreeNodes(Map<Integer, String> map) {
        JSONObject jsonObject = new JSONObject();
        List<JSONObject> jsonObjects = new ArrayList<>(map.size());
        for (Map.Entry<Integer, String> mapEntry : map.entrySet()) {
            JSONObject object = new JSONObject();
            object.put("id", mapEntry.getKey());
            object.put("parentId", 0);
            object.put("name", mapEntry.getValue());
            jsonObjects.add(object);
        }
        jsonObject.put("treeNodes", jsonObjects);
        return jsonObject;
    }
}
