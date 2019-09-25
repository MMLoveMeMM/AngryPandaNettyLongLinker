package pumpkin.org.angrypandagson.net;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

import io.netty.channel.ChannelHandlerContext;
import pumpkin.org.angrypandagson.net.core.ICallBackResult;
import pumpkin.org.angrypandagson.net.core.ISession;
import pumpkin.org.angrypandagson.net.core.LongLinkCreator;
import pumpkin.org.angrypandagson.net.timeout.DelayedReqPackage;
import pumpkin.org.angrypandagson.net.timeout.SendPack;

import static pumpkin.org.angrypandagson.net.LongLinkProxy.WorkType.ENABLE_HEARTBEAT_MSG;
import static pumpkin.org.angrypandagson.net.core.LongLinkState.SEND_OK;


/**
 * @ProjectName: bugfix_speech_v0.9.5
 * @ClassName: LongLinkProxy
 * @Author: 刘志保
 * @CreateDate: 2019/9/9 16:44
 * @Description: 创建长连接, 封装收发数据, 心跳包发送,实现重发机制[netty自行实现]
 * 后面所有的长连接都在这个类的基础上新建
 */
public class LongLinkProxy implements ISession {

    private static final String TAG = LongLinkProxy.class.getName();
    private LongLinkCreator mLongLinkCreator;
    private DelayQueue<DelayedReqPackage> mDelayQueues;
    private Map<Integer, SendPack> SendPackMaps = new ConcurrentHashMap<>();
    private WorkHandler mHandler = new WorkHandler();

    @IntDef({ENABLE_HEARTBEAT_MSG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface WorkType {
        int ENABLE_HEARTBEAT_MSG = 0;
    }

    // 单位: ms 默认15s
    private long mHeartBeatInterval = 15 * 1000;
    private String mHeartBeatPackage = "1\n";
    // 默认不发送心跳
    private boolean mEnableHeartBeat = false;

    private ISession mSession;
    private String mIpAddress;
    private int mPort;

    public LongLinkProxy(String address, int port) {
        mLongLinkCreator = new LongLinkCreator();
        mLongLinkCreator.createLongLink(address, port, this,false);
        mDelayQueues = new DelayQueue<>();
    }

    public LongLinkProxy(String address, int port, ISession session) {
        mLongLinkCreator = new LongLinkCreator();
        this.mIpAddress = address;
        this.mPort = port;
        // mLongLinkCreator.createLongLink(address, port, this);
        mSession = session;
        mDelayQueues = new DelayQueue<>();
    }

    public void createLongLink() {
        if (mLongLinkCreator != null) {
            mLongLinkCreator.createLongLink(mIpAddress, mPort, this,false);
        }
    }

    public void createLongLink(String address, int port) {
        this.mIpAddress = address;
        this.mPort = port;
        if (mLongLinkCreator != null) {
            mLongLinkCreator.createLongLink(mIpAddress, mPort, this,false);
        }
    }

    /**
     * 使用默认的心跳包和间隔时间
     */
    public void enableHeartBeat() {
        mEnableHeartBeat = true;
        mHandler.removeMessages(ENABLE_HEARTBEAT_MSG);
        mHandler.sendEmptyMessageDelayed(ENABLE_HEARTBEAT_MSG, mHeartBeatInterval);
    }

    public void disableHeartBeat() {
        mHandler.removeMessages(ENABLE_HEARTBEAT_MSG);
        mEnableHeartBeat = false;
    }

    public void enableHeartBeat(int interval, String datas) {
        mHeartBeatInterval = interval;
        mHeartBeatPackage = datas;
        mEnableHeartBeat = true;
    }

    /**
     * 心跳包发送重新计时
     * 如果有一个命令包发送后,心跳包重新计时顺延15s
     * 当然心跳也可以独立每个15s发送,忽略其他命令包
     */
    private void retryHeartBeat() {
        mHandler.removeMessages(ENABLE_HEARTBEAT_MSG);
        mHandler.sendEmptyMessageDelayed(ENABLE_HEARTBEAT_MSG, mHeartBeatInterval);
    }

    public void sendPackage(String datas) {
        mLongLinkCreator.sendPackage(datas, new ICallBackResult() {
            @Override
            public void callBackResult(int state) {
                // 获取发送是否成功的状态
                if (state == SEND_OK) {
                    retryHeartBeat();

                } else {

                }
            }
        });
    }

    public void sendPackage(String datas, ICallBackResult callBackResult) {
        mLongLinkCreator.sendPackage(datas, callBackResult);
    }

    public void sendPackage(final int req_id, String data, final int timeout) {

        if (mLongLinkCreator != null) {
            SendPack pack = new SendPack(req_id, data, timeout);
            SendPackMaps.put(req_id, pack);
            mLongLinkCreator.sendPackage(data, new ICallBackResult() {
                @Override
                public void callBackResult(int state) {
                    // 获取发送是否成功的状态
                    if (state == SEND_OK) {
                        retryHeartBeat();


                    } else {

                    }
                }
            });
            // 监控数据包收发记录轨迹
            DelayedReqPackage delayedReqPackage = new DelayedReqPackage(req_id, System.currentTimeMillis(), timeout);
            mDelayQueues.add(delayedReqPackage);
        }

    }

    /**
     * 发送心跳包
     */
    public void sendHeartBeat() {
        if (mLongLinkCreator != null) {
            mLongLinkCreator.sendPackage(mHeartBeatPackage, null);
        }
    }

    public void disconnect() {

    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, String datas) {

        Log.e(TAG, "rec : " + datas);
        /*if (TextUtils.isEmpty(datas)
                || datas.length() < 40
                || datas.equalsIgnoreCase("2")) {
            return;
        }*/

        if (mSession != null) {
            mSession.channelRead(channelHandlerContext, datas);
        }

        /*if ("mdp_msg".equalsIgnoreCase(JsonUtils.getResponseMethod(datas))) {

        } else {
            *//**
         * 家居参数信息,继续往上传
         *//*
            long retReqId = JsonUtils.getResponseReqId(datas);
            SendPack sendPack = SendPackMaps.get(retReqId);


        }*/

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) {
        if (mSession != null) {
            mSession.exceptionCaught(channelHandlerContext, throwable);
        }
    }

    public class WorkHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int action = msg.what;
            switch (action) {
                case ENABLE_HEARTBEAT_MSG: {
                    // 循环发送
                    sendHeartBeat();
                    mHandler.removeMessages(ENABLE_HEARTBEAT_MSG);
                    mHandler.sendEmptyMessageDelayed(ENABLE_HEARTBEAT_MSG, mHeartBeatInterval);
                }
                break;
                default:
                    break;
            }
        }
    }


}
