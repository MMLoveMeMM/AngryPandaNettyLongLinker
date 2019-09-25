package pumpkin.org.angrypandagson;

import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import pumpkin.org.angrypandagson.net.base.BaseLongLinker;

/**
 * @ProjectName: AngryPandaGSon
 * @ClassName: LongLinkerTestB
 * @Author: 刘志保
 * @CreateDate: 2019/9/12 9:43
 * @Description: java类作用描述
 */
public class LongLinkerTestB extends BaseLongLinker {

    private HandlerThread mLinkThread = new HandlerThread("LongLinker");
    private LinkHandler mLinkHandler;
    public LongLinkerTestB(){
        super();
        mIpAddress = "192.168.10.25";
        mPort = 10086;
        init();
    }

    private void init(){
        mLinkThread.start();
        mLinkHandler = new LinkHandler(mLinkThread.getLooper());
    }

    public void startLink(){
        mLinkHandler.sendEmptyMessage(1);
    }

    @Override
    protected void sendReq(String method, String nodeId, boolean mdpMsg, Object params, int timeout) {

    }

    @Override
    protected void sendJson(int reqId, boolean mdpMsg, String contentJson, int timeout) {

    }

    private class LinkHandler extends android.os.Handler{
        public LinkHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int state = msg.what;
            switch (state){
                case 1:{
                    connect();
                }
                break;
                default:
                    break;
            }
        }
    }
}
