package pumpkin.org.angrypandagson.net.core;


import android.text.TextUtils;
import android.util.Log;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import static pumpkin.org.angrypandagson.net.core.LongLinkState.SEND_FAIL;
import static pumpkin.org.angrypandagson.net.core.LongLinkState.SEND_OK;

/**
 * @ProjectName: bugfix_speech_v0.9.5
 * @ClassName: LongLinkCreator
 * @Author: 刘志保
 * @CreateDate: 2019/9/9 14:56
 * @Description: java类作用描述
 */
public class LongLinkCreator {

    private static final String TAG = LongLinkCreator.class.getName();

    private Channel mChannel;
    private String mAddress;
    private int mPort;
    private LongLinkCore mLongLinkCore;

    public LongLinkCreator(){
        mLongLinkCore = new LongLinkCore();
    }

    public LongLinkCreator(String address, int port) {
        mAddress = address;
        mPort = port;
        mLongLinkCore = new LongLinkCore();
    }

    public boolean createLongLink(String address, int port,ISession session,boolean filter){
        mAddress = address;
        mPort = port;
        return createLongLink(session,filter);
    }
    /**
     * 创建一个长连接,并且获取对应的Channel
     * Channel是发送数据的载体
     *
     * @return
     */
    public boolean createLongLink(final ISession session,boolean filter) {
        /*new Thread(new Runnable() {
            @Override
            public void run() {*/
                if(!LongLinkCore.isInit()){
                    LongLinkCore.init();
                }
                if (LongLinkCore.isInit()) {
                    // 这个连接是个阻塞的过程
                    mChannel = mLongLinkCore.connect(mAddress, mPort);
                    if (mChannel != null) {
                        if (session != null) {
                            if (filter) {
                                mLongLinkCore.addSessionListener(mAddress + ":" + mPort, session);
                            }else{
                                mLongLinkCore.addSessionListener(session);
                            }
                        }
                    }

                }
           // }
        //}).start();

        return true;
    }

    public boolean isConnect(){
        if(mChannel!=null) {
            return mChannel.isActive();
        }
        return false;
    }

    public void sendPackage(String datas, final ICallBackResult callBackResult) {

        if (TextUtils.isEmpty(datas) || !datas.endsWith("\n")) {
            return;
        }

        if (mChannel == null || !mChannel.isActive()) {
            return;
        }

        mChannel.writeAndFlush(datas).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    Log.d(TAG, "send msg success !!!");
                    // 消息可能发送出去了，现在等回复
                    if (callBackResult != null) {
                        callBackResult.callBackResult(SEND_OK);
                    }
                } else {
                    Log.e(TAG, "send msg fail !!!" + channelFuture.cause() + "/n");
                    if (callBackResult != null) {
                        callBackResult.callBackResult(SEND_FAIL);
                    }
                }
            }
        });
    }

    public void disconnect() {

        if (mChannel != null) {
            mChannel.close();
            mChannel = null;
        }

    }
}
