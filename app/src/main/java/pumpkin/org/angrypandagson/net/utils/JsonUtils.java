package pumpkin.org.angrypandagson.net.utils;

import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;

/**
 * @ProjectName: bugfix_speech_v0.9.5
 * @ClassName: JsonParser
 * @Author: 刘志保
 * @CreateDate: 2019/9/9 19:24
 * @Description: java类作用描述
 */
public class JsonUtils {

    public static long getResponseReqId(String data){

        if(TextUtils.isEmpty(data)){
            return -1;
        }

        JsonParser jsonParser = new JsonParser();
        JsonObject rootNode;

        JsonReader reader = new JsonReader(new StringReader(data));
        reader.setLenient(true);
        rootNode = jsonParser.parse(reader).getAsJsonObject();

        JsonElement methodName = rootNode.get("req_id");
        long method = methodName.getAsLong();
        return method;
    }

    public static String getResponseMethod(String data){
        JsonParser jsonParser = new JsonParser();
        JsonObject rootNode;

        JsonReader reader = new JsonReader(new StringReader(data));
        reader.setLenient(true);
        rootNode = jsonParser.parse(reader).getAsJsonObject();

        JsonElement methodName = rootNode.get("method");
        String method = methodName.getAsString();
        if(TextUtils.isEmpty(method)){

            JsonObject jsonObject= (JsonObject) rootNode.get("params");
            methodName = jsonObject.get("method");
            method = methodName.getAsString();
            if(TextUtils.isEmpty(method)){
                return "";
            }
        }
        return method;
    }

}
