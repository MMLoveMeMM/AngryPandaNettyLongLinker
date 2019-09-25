package pumpkin.org.angrypandagson;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.List;
/**
 * @ProjectName: bugfix_speech_v0.9.5
 * @ClassName: GsonUtil
 * @Author: 刘志保
 * @CreateDate: 2019/9/10 19:36
 * @Description: java类作用描述
 */
public class GsonUtil {
    private static final String TAG = "GsonUtil";
    private static Gson sGson = new Gson();
    private static JsonParser sJsonParser = new JsonParser();

    /**
     * String类型的json转为JsonObject类型
     *
     * @param jsonData
     * @return
     */
    public static JsonObject str2JsonObj(String jsonData) {
        try {
            JsonReader reader = new JsonReader(new StringReader(jsonData));
            reader.setLenient(true);
            JsonElement element = sJsonParser.parse(reader);
            if (element == null) {
                return null;
            }
            JsonObject jsonObj = element.getAsJsonObject();
            return jsonObj;
        } catch (JsonIOException | JsonSyntaxException e) {
            Log.e(TAG, "",e);
        }
        return null;
    }

    /**
     * list转json数组
     *
     * @param list
     * @return
     */
    public static JsonArray list2JsonArray(List list) {
        try {
            String contentJson = jsonObj2Str(list);
            JsonReader reader = new JsonReader(new StringReader(contentJson));
            reader.setLenient(true);
            JsonElement element = sJsonParser.parse(reader);
            if (element == null) {
                return null;
            }
            JsonArray contentJo = element.getAsJsonArray();
            return contentJo;
        } catch (JsonIOException | JsonSyntaxException e) {
            Log.e(TAG,"", e);
        }
        return null;
    }

    /**
     * 特定接口，获取响应json content部分中的 result 属性部分，返回结果为JsonObject类型
     *
     * @param contentJson
     * @return
     */
    public static JsonObject getResultJsonObj(String contentJson) {
        try {
            JsonObject resJo = str2JsonObj(contentJson);
            if (resJo == null) {
                return null;
            }
            JsonElement resultEL = resJo.get(Protocol.PARAMS_KEY_COMNON_RESULT);
            if (resultEL == null) {
                return null;
            }
            JsonObject resultObj = resultEL.getAsJsonObject();
            return resultObj;
        } catch (IllegalStateException e) {
            Log.e(TAG,"", e);
        }
        return null;
    }

    /**
     * 特定接口，获取响应json content部分中的 result 属性部分，返回结果为String类型
     *
     * @param contentJson
     * @return
     */
    public static String getResultJsonStr(String contentJson) {
        try {
            JsonObject resultObj = getResultJsonObj(contentJson);
            String resultStr = sGson.toJson(resultObj);

            return resultStr;
        } catch (JsonIOException e) {
            Log.e(TAG, "",e);
        }
        return null;
    }


    /**
     * 获取响应的josn中String类型的属性值
     *
     * @param propertyName
     * @param contentJson
     * @return
     */
    public static String getString(String propertyName, String contentJson) {
        try {
            JsonObject resultJsonObj = GsonUtil.str2JsonObj(contentJson);
            if (resultJsonObj == null) {
                return null;
            }
            JsonElement element = resultJsonObj.get(propertyName);
            if (element == null) {
                return null;
            }
            String propertyValue = element.getAsString();
            return propertyValue;
        } catch (UnsupportedOperationException e) {
            Log.e(TAG,"", e);
        }
        return null;
    }

    /**
     * 获取响应的josn中int类型的属性值
     *
     * @param propertyName
     * @param contentJson
     * @return -1表示没有查到，但有可能查询的该属性值本来就是-1，可能存在bug。
     */
    public static int getInt(String propertyName, String contentJson) {
        try {
            JsonObject resultJsonObj = GsonUtil.str2JsonObj(contentJson);
            if (resultJsonObj == null) {
                return -1;
            }
            JsonElement element = resultJsonObj.get(propertyName);
            if (element == null) {
                return -1;
            }
            int propertyValue = element.getAsInt();
            return propertyValue;
        } catch (UnsupportedOperationException e) {
            Log.e(TAG, "",e);
        }
        return -1;
    }

    /**
     * 获取响应的josn中Long类型的属性值
     *
     * @param propertyName
     * @param contentJson
     * @return -1表示没有查到，但有可能查询的该属性值本来就是-1，可能存在bug。
     */
    public static long getLong(String propertyName, String contentJson) {
        try {
            JsonObject resultJsonObj = GsonUtil.str2JsonObj(contentJson);
            if (resultJsonObj == null) {
                return -1;
            }
            JsonElement element = resultJsonObj.get(propertyName);
            if (element == null) {
                return -1;
            }
            long propertyValue = element.getAsLong();
            return propertyValue;
        } catch (UnsupportedOperationException e) {
            Log.e(TAG, "",e);
        }
        return -1;
    }

    /**
     * 获取响应的josn中JsonObject类型的属性值
     *
     * @param propertyName
     * @param contentJson
     * @return
     */
    public static JsonObject getJsonObj(String propertyName, String contentJson) {
        try {
            JsonObject resultJsonObj = GsonUtil.str2JsonObj(contentJson);
            if (resultJsonObj == null) {
                return null;
            }
            JsonElement element = resultJsonObj.get(propertyName);
            if (element == null) {
                return null;
            }
            JsonObject propertyValue = element.getAsJsonObject();
            return propertyValue;
        } catch (IllegalStateException e) {
            Log.e(TAG, "",e);
        }
        return null;
    }

    /**
     * 获取相应的josn中JsonArray类型的属性值
     *
     * @param propertyName
     * @param contentJson
     * @return
     */
    public static JsonArray getJsonArray(String propertyName, String contentJson) {
        try {
            JsonObject resultJsonObj = GsonUtil.str2JsonObj(contentJson);
            if (resultJsonObj == null) {
                return null;
            }
            JsonElement element = resultJsonObj.get(propertyName);
            if (element == null) {
                return null;
            }
            JsonArray propertyValue = element.getAsJsonArray();
            return propertyValue;
        } catch (IllegalStateException e) {
            Log.e(TAG,"", e);
        }
        return null;
    }

    /**
     * 封装请求Json内容中的content部分
     *
     * @param req_id
     * @param reqMethod
     * @param reqParams
     * @return
     */
    public static String getContentJsonStr(int req_id, String reqMethod, String reqParams) {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(Protocol.PARAMS_KEY_COMNON_METHOD, reqMethod);
            jsonObject.addProperty(Protocol.PARAMS_KEY_COMNON_TIMESTAMP, System.currentTimeMillis());
            jsonObject.addProperty(Protocol.PARAMS_KEY_COMNON_REQ_ID, req_id);
            jsonObject.add(Protocol.PARAMS_KEY_COMNON_PARAMS, sJsonParser.parse(reqParams));
            String contentStr = sGson.toJson(jsonObject);

            return contentStr;
        } catch (JsonSyntaxException | JsonIOException e) {
            Log.e(TAG, "",e);
        }
        return null;
    }

    /**
     * 生成content的json字符串
     * */
    public static String generateContentJsonStr(int req_id, String reqMethod, JsonObject paramsJsonObj) {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(Protocol.PARAMS_KEY_COMNON_METHOD, reqMethod);
            jsonObject.addProperty(Protocol.PARAMS_KEY_COMNON_TIMESTAMP, System.currentTimeMillis());
            jsonObject.addProperty(Protocol.PARAMS_KEY_COMNON_REQ_ID, req_id);
            jsonObject.add(Protocol.PARAMS_KEY_COMNON_PARAMS, paramsJsonObj);

            String contentStr = sGson.toJson(jsonObject);
            return contentStr;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "",e);
        }

        return null;
    }

    /**
     * json对象转换为json String
     *
     * @param object
     * @return
     */
    public static String jsonObj2Str(Object object) {
        try {
            return sGson.toJson(object);
        } catch (JsonIOException e) {
            Log.e(TAG,"", e);
        }
        return null;
    }

    /**
     * jsonObject转换为bean
     *
     * @param jsonObject
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T getObjectFromJson(JsonObject jsonObject, Class<T> tClass) {
        try {
            return sGson.fromJson(jsonObject, tClass);
        } catch (JsonSyntaxException e) {
            Log.e(TAG,"", e);
        }
        return null;
    }
}
