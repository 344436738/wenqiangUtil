package com.leimingtech.core.util;

import com.leimingtech.base.entity.vo.ZtreeEntity;
import com.leimingtech.common.utils.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * Created by liuzhen on 2017/4/13.
 */
public class ZTreeUtils {

    public static JSONArray getSelectedJsonArray(List<ZtreeEntity> ztreeList, List<String> seletedNode) {
        JSONArray jsonArray = new JSONArray();
        for (ZtreeEntity t : ztreeList) {
            JSONObject json = new JSONObject();
            json.put("id", t.getId());
            json.put("name", t.getName());
            json.put("open", true);

            if (StringUtils.isNotBlank(t.getParentId())) {
                json.put("pId", t.getParentId());
            }

            if (seletedNode.contains(t.getId())) {
                json.put("checked", true);
            } else {
                json.put("checked", false);
            }
            jsonArray.add(json);
        }
        return jsonArray;
    }
}
