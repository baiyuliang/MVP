
package com.byl.mvp.api.retrofit;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON转化成对象，集合帮助类
 */
public class JsonUtils {

    /**
     * 将返回结果转化成对象
     *
     * @param s
     * @param t
     * @return
     */
    public static <T> Object toObject(String s, Class<T> t) {
        if (s != null) {
            Gson gson = new Gson();
            return gson.fromJson(s, t);
        }
        return null;
    }

    /**
     * 返回集合对象
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> toListFromJsonArray(String json, Class<T> clazz) throws Exception {
        List<T> lst = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            lst.add(new Gson().fromJson(elem, clazz));
        }
        return lst;
    }

    /***
     * 返回String
     * @param list
     * @return
     */
    public static <T> String toJsonFromList(List<T> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<T>>() {
        }.getType(); // 指定集合对象属性
        String json = gson.toJson(list, type);
        return json;
    }


    /**
     * Object 返回String
     * @param object
     * @return
     */
    public static String toJsonFromObject(Object object){
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json;
    }

}
