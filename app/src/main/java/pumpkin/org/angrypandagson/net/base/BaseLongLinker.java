package pumpkin.org.angrypandagson.net.base;

import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Handler;

import io.netty.channel.ChannelHandlerContext;
import pumpkin.org.angrypandagson.net.LongLinkProxy;
import pumpkin.org.angrypandagson.net.core.ICallBackResult;
import pumpkin.org.angrypandagson.net.core.ISession;
import pumpkin.org.angrypandagson.net.timeout.SendPack;

/**
 * @ProjectName: bugfix_speech_v0.9.5
 * @ClassName: BaseLongLinker
 * @Author: 刘志保
 * @CreateDate: 2019/9/10 16:54
 * @Description: java类作用描述
 */
public abstract class BaseLongLinker implements ISession {

    private static final String TAG = BaseLongLinker.class.getName();
    private static final int PORT_INVALID_LIMIT = 1;

    protected Map<Integer, SendPack> mDataReceiver = new ConcurrentHashMap<>();

    protected String mIpAddress;
    protected int mPort;
    protected String mAesKey;
    protected String mAesIv;

    private ISession session;

    private LongLinkProxy mLongLinkProxy;

    public BaseLongLinker() {
    }

    public BaseLongLinker(ISession session) {
        this.session = session;
    }

    public BaseLongLinker(String aesKey, String aesIv) {
        this.mAesKey = aesKey;
        this.mAesIv = aesIv;
    }

    public BaseLongLinker(String aesKey, String aesIv, ISession session) {
        this.mAesKey = aesKey;
        this.mAesIv = aesIv;
        this.session = session;
    }

    public boolean connect(String ipAddress, int port) {
        this.mIpAddress = ipAddress;
        this.mPort = port;
        if (!TextUtils.isEmpty(mIpAddress) && mPort > PORT_INVALID_LIMIT) {
            mLongLinkProxy = new LongLinkProxy(mIpAddress, mPort, this);
            mLongLinkProxy.createLongLink();
            mLongLinkProxy.enableHeartBeat();
            return true;
        } else {
            Log.e(TAG, "");
        }
        return false;
    }

    public boolean connect() {
        if (!TextUtils.isEmpty(mIpAddress) && mPort > PORT_INVALID_LIMIT) {
            mLongLinkProxy = new LongLinkProxy(mIpAddress, mPort, this);
            mLongLinkProxy.createLongLink();
            mLongLinkProxy.enableHeartBeat();
            return true;
        } else {
            Log.e(TAG, "");
        }
        return false;
    }

    public void disconnect() {

    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, String packages) {

        Log.e(TAG,"rec : "+packages);
        /**
         * 需要解密的解密数据
         * 以及去掉头部
         * datas 已经是完全的明文裸数据
         */
        /*String datas = AesDecrypt.decryptResponsePackage(mAesKey, mAesIv, packages);
        int reqId = GsonUtil.getInt("req_id", datas);
        SendPack sendPack = mDataReceiver.get(reqId);
        if (sendPack != null) {
            IDataReceiver callback = sendPack.getReceiver();
            *//**
             * 回传数据的方式,指定req_id回传的就指定的callback回传
             * 如果不是非指定的,就继续往上回传
             * 如果超过时间范围也不会返回
             *//*
            if (callback != null && System.currentTimeMillis() - sendPack.getTimestamp() < sendPack.getTimeout()) {
                callback.onReceiver(datas);
            } else if (session != null) {
                session.channelRead(channelHandlerContext, datas);
            }
            mDataReceiver.remove(reqId);
        } else if (session != null) {
            session.channelRead(channelHandlerContext, datas);
        }*/
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) {
        if (session != null) {
            session.exceptionCaught(channelHandlerContext, throwable);
        }
    }

    /**
     * 子类实现
     *
     * @param method
     * @param nodeId
     * @param mdpMsg
     * @param params
     * @param timeout
     */
    protected abstract void sendReq(String method, String nodeId, boolean mdpMsg, Object params, int timeout);

    protected abstract void sendJson(int reqId, boolean mdpMsg, String contentJson, int timeout);

    protected void sendJson(int reqId, boolean mdpMsg, String contentJson, int timeout, IDataReceiver callReceiver) {
        SendPack sendPack = new SendPack(reqId, contentJson, timeout, System.currentTimeMillis(), callReceiver);
        mDataReceiver.put(reqId, sendPack);
    }

    protected void sendJson(int reqId, boolean mdpMsg, String contentJson, int timeout, ICallBackResult callBackResult,IDataReceiver callReceiver) {
        SendPack sendPack = new SendPack(reqId, contentJson, timeout, System.currentTimeMillis(), callReceiver);
        mDataReceiver.put(reqId, sendPack);
        sendPackage(contentJson,callBackResult);
    }

    public void sendPackage(String datas) {
        if (mLongLinkProxy != null) {
            mLongLinkProxy.sendPackage(datas);
        }
    }

    public void sendPackage(String datas, ICallBackResult callBackResult) {
        if (mLongLinkProxy != null) {
            mLongLinkProxy.sendPackage(datas, callBackResult);
        }
    }

}
