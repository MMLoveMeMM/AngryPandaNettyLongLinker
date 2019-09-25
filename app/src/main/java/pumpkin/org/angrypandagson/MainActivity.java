package pumpkin.org.angrypandagson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.google.gson.JsonObject;

import pumpkin.org.angrypandagson.net.core.ICallBackResult;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private static final String DEFAULT_USER_NAME = "11111111111";
    private static final String DEFAULT_USER_PWD = "hd_iot_speech";

    private LongLinkerTest mLongLinkerTest;
    private LongLinkerTestB mLongLinkerTestB;

    private Button mConnBtn;
    private Button mSendBtn;
    private WebView webView;
    private Button mConnbBtn;
    private Button mSendbBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLongLinkerTest = new LongLinkerTest();
        mLongLinkerTestB = new LongLinkerTestB();

        Json1();

        String json = FileUtils.readAssetFile(getApplicationContext(),"dm_get_device_info.json");
        Log.e(TAG,json);
        int id = GsonUtil.getInt("req_id",json);
        Log.e(TAG,"+++++++++++ req_id : "+id);

        webView=(WebView)findViewById(R.id.web);
        webView.loadUrl("http://www.jikexueyuan.com");

        mConnBtn = (Button)findViewById(R.id.connect);
        mConnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLongLinkerTest.startLink();
            }
        });

        mConnBtn = (Button)findViewById(R.id.connect);
        mConnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLongLinkerTest.startLink();
            }
        });

        mConnbBtn = (Button)findViewById(R.id.connectb);
        mConnbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLongLinkerTestB.startLink();
            }
        });

        mSendBtn = (Button)findViewById(R.id.send);
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLongLinkerTest.sendPackage("hello this is liuzhibao id : " + Protocol.getRequestId()+"\n", new ICallBackResult() {
                    @Override
                    public void callBackResult(int state) {
                        Log.e(TAG,"state : "+state);
                    }
                });
            }
        });

        mSendbBtn = (Button)findViewById(R.id.sendb);
        mSendbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLongLinkerTestB.sendPackage("hello this is liuzhibao id : " + Protocol.getRequestId()+"\n", new ICallBackResult() {
                    @Override
                    public void callBackResult(int state) {
                        Log.e(TAG,"state : "+state);
                    }
                });
            }
        });

    }

    public void Json1(){
        JsonObject loginJson = new JsonObject();
        loginJson.addProperty("phone", DEFAULT_USER_NAME);
        loginJson.addProperty("pwd", DEFAULT_USER_PWD);
        loginJson.addProperty("os_type", "Android");

        final int req_id = Protocol.getRequestId();
        String commonJson = Protocol.generateContentJsonStr(req_id, "um_login_pwd", loginJson.toString());
        String finalJson = Protocol.addSaferHeader(commonJson) + "\n";
        Log.e(TAG,"finalJson : "+finalJson);
    }
}
