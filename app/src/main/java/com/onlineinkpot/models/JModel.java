package com.onlineinkpot.models;

import org.json.JSONObject;

/**
 * Created by john on 7/28/2017.
 */

public class JModel {
    public static String getString(JSONObject jObject, String title) {
        try {
            return jObject.getString(title);
        } catch (Exception ignored) {
        }
        return null;
    }

    public static int getInt(JSONObject jObject, String title) {
        try {
            return jObject.getInt(title);
        } catch (Exception ignored) {
        }
        return 0;
    }
}
