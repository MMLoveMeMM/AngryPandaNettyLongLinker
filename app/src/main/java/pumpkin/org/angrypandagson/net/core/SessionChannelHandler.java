package pumpkin.org.angrypandagson.net.core;

import android.util.Log;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @ProjectName: bugfix_speech_v0.9.5
 * @ClassName: SessionChannelHandler
 * @Author: 刘志保
 * @CreateDate: 2019/9/9 10:43
 * @Description: netty 通道管理,数据监控
 */
@ChannelHandler.Sharable
public class SessionChannelHandler extends SimpleChannelInboundHandler<String> {

    private static final String TAG = SessionChannelHandler.class.getName();
    /**
     * 不过滤不同服务器发过来的数据
     */
    private List<ISession> mListListeners = new ArrayList<>();
    /**
     * 根据不同的服务器IP地址对应回调到上层去
     */
    private Map<String, ISession> mListeners = new ConcurrentHashMap<>();

    public void addSessionListener(ISession session) {
        mListListeners.add(session);
    }

    public void addSessionListener(String address, ISession session) {
        mListeners.put(address, session);
    }

    public void clearSessionListener() {
        if (mListeners != null) {
            mListeners.clear();
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Log.e(TAG, "channelRead0 : " + getChannelKey(channelHandlerContext) + " rec : " + s);

        for(ISession session:mListListeners){
            session.channelRead(channelHandlerContext, s);
        }

        ISession session = getISession(channelHandlerContext);
        if (session != null) {
            session.channelRead(channelHandlerContext, s);
        }
        Log.e(TAG, "receiver : " + s);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Log.e(TAG, "channel active : " + getChannelKey(ctx));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        for(ISession session:mListListeners){
            session.exceptionCaught(ctx, cause);
        }
    }

    private ISession getISession(ChannelHandlerContext ctx) {
        ISession session = mListeners.get(getChannelKey(ctx));
        return session;
    }

    private String getChannelKey(ChannelHandlerContext ctx) {
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String ipAddress = socketAddress.getAddress().getHostAddress();
        int port = socketAddress.getPort();
        String connectionKey = getConnectKey(ipAddress, port);
        Log.d(TAG, "getChannelKey(), " + connectionKey);
        return connectionKey;
    }

    public static String getConnectKey(String ipAddress, int port) {
        return String.format("%s:%d", ipAddress, port);  // TODO
    }
}
