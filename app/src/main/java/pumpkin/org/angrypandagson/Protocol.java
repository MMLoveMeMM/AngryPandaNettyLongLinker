package pumpkin.org.angrypandagson;

import android.util.Log;

import com.google.gson.JsonObject;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ProjectName: bugfix_speech_v0.9.5
 * @ClassName: Protocol
 * @Author: 刘志保
 * @CreateDate: 2019/9/10 15:37
 * @Description: 网络协议
 */
public class Protocol {

    private static final String TAG = Protocol.class.getName();
    // Method
    public static final String MDP_MSG_METHOD = "mdp_msg";
    public static final String PARAMS_KEY_COMNON_METHOD = "method";
    public static final String PARAMS_KEY_COMNON_TIMESTAMP = "timestamp";
    public static final String PARAMS_KEY_COMNON_REQ_ID = "req_id";
    public static final String PARAMS_KEY_COMNON_PARAMS = "params";
    public static final String PARAMS_KEY_COMNON_CODE = "code";
    public static final String PARAMS_KEY_COMNON_MSG = "msg";
    public static final String PARAMS_KEY_COMNON_RESULT = "result";
    public static final String PARAMS_KEY_COMNON_FAMILY_ID = "family_id";
    public static final String PARAMS_KEY_COMNON_ROOM_ID = "room_id";
    public static final String PARAMS_KEY_COMNON_USER_ID = "user_id";
    public static final String PARAMS_KEY_COMNON_ATTRIBUTE = "attribute";//attribute
    public static final String PARAMS_KEY_PLAY_CTRL = "play_ctl";
    public static final String RESULT_KEY_LOG_URL = "log_url";

    private static AtomicInteger REQUEST_ID = new AtomicInteger(0);
    //全局唯一 ID (GUID) 对应用实例进行唯一标识
    public static final String uniqueID = UUID.randomUUID().toString();

    public static int getRequestId() {
        return REQUEST_ID.incrementAndGet();
    }

    public static String generateContentJsonStr(int req_id, String reqMethod, String paramsStr) {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(Protocol.PARAMS_KEY_COMNON_METHOD, reqMethod);
            jsonObject.addProperty(Protocol.PARAMS_KEY_COMNON_TIMESTAMP, System.currentTimeMillis());
            jsonObject.addProperty(Protocol.PARAMS_KEY_COMNON_REQ_ID, req_id);
            jsonObject.add(Protocol.PARAMS_KEY_COMNON_PARAMS, GsonUtil.str2JsonObj(paramsStr));

            return GsonUtil.jsonObj2Str(jsonObject);
        } catch (Exception e) {
            Log.e(TAG, "");
        }

        return null;
    }

    public static String addSaferHeader(String data) {
        StringBuffer result = null;
        //数据不需要加密
        result = new StringBuffer("{\"uuid\":\"" + uniqueID + "\", \"encry\":\"false\",");
        result.append("\"content\":").append(data).append("}");
        return result.toString();
    }
}
