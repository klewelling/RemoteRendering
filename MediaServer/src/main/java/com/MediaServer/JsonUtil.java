package com.MediaServer;

/**
 * Created by alexbiju on 11/8/14.
 */
import com.google.gson.Gson;
import spark.ResponseTransformer;
public class JsonUtil {
    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }
    public static ResponseTransformer json() {
        return JsonUtil::toJson;
    }
}
