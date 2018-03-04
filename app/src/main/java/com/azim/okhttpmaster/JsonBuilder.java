package com.azim.okhttpmaster;

import org.json.JSONObject;

/**
 * Created by Azim on 18-02-2018.
 * Returns json objects as string
 */

public class JsonBuilder {

    public static String getSimpleJSON() {
        JSONObject jObj = new JSONObject();
        try {
            jObj.put("title", "foo");
            jObj.put("body", "bar");
            jObj.put("userId", 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return jObj.toString();
    }
}
