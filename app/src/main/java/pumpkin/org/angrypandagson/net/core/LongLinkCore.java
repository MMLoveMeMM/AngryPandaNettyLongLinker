package pumpkin.org.angrypandagson.net.core;

import android.text.TextUtils;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

/**
 * @ProjectName: bugfix_speech_v0.9.5
 * @ClassName: LongLinkCreator
 * @Author: 刘志保
 * @CreateDate: 2019/9/9 10:39
 * @Description: 使用netty创建长连接
 */
public class LongLinkCore {

    private static final String TAG = LongLinkCore.class.getName();
    private static final int READ_TIMEOUT_SECONDS = 60;
    private static final int WRITE_TIMEOUT_SECONDS = 60;
    private static final int BOTH_TIMEOUT_SECONDS = 60;

    private static Bootstrap mBootstrap;
    private static EventLoopGroup mWorkGroup;
    private static SessionChannelHandler mSessionChannelHandler;
    private static Object isInit = null;

    public static void init() {
        if (!isInit()) {
            initBootstrap();
            isInit = new Object();
        }
    }

    public static boolean isInit() {
        if (isInit != null) {
            return true;
        }
        return false;
    }

    private static void initBootstrap() {
        mBootstrap = new Bootstrap();
        mWorkGroup = new NioEventLoopGroup(1);
        mSessionChannelHandler = new SessionChannelHandler();

        mBootstrap
                .group(mWorkGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel)
                            throws Exception {
                        ChannelPipeline p = socketChannel.pipeline();
                        // 配置超时时间
                        p.addLast(
                                // 增加连接超时时间定义
                                new IdleStateHandler(
                                        READ_TIMEOUT_SECONDS, WRITE_TIMEOUT_SECONDS,
                                        BOTH_TIMEOUT_SECONDS));
                        // tcp 编码模式
                        // LineBasedFrameDecoder : 即按照行进行分割，遇到一个换行符，则认为是一个完整的报文
                        /*p.addLast(new StringEncoder(CharsetUtil.UTF_8));
                        p.addLast(new LineBasedFrameDecoder(1024 * 1024 * 10, true, true));
                        p.addLast(new StringDecoder(CharsetUtil.UTF_8));*/
                        // 测试调试解码模式,对方发送过来的数据以$$结尾
                        ByteBuf delimiter = Unpooled.copiedBuffer("&&".getBytes());
                        p.addLast(new DelimiterBasedFrameDecoder(1024 * 1024, delimiter/*Delimiters.lineDelimiter()*/));
                        p.addLast(new StringDecoder());
                        // tcp协议实体
                        p.addLast(mSessionChannelHandler);
                    }
                });
        mBootstrap.option(ChannelOption.SO_RCVBUF, 1024 * 1024 * 10);
        mBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        mBootstrap.option(ChannelOption.TCP_NODELAY, true);
        mBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 15000);
    }

    /**
     * 监听从服务器返回的数据
     * @param hostAddress
     * @param session
     */
    public void addSessionListener(String hostAddress, ISession session) {
        if (mSessionChannelHandler != null) {
            mSessionChannelHandler.addSessionListener(hostAddress, session);
        }
    }

    public void addSessionListener(ISession session) {
        if (mSessionChannelHandler != null) {
            mSessionChannelHandler.addSessionListener(session);
        }
    }

    public Channel connect(String address, int port) {

        Channel channel = null;
        if (TextUtils.isEmpty(address) || TextUtils.isEmpty(port + "")) {
            return null;
        }

        if (mBootstrap != null) {
            // 连接服务端
            ChannelFuture future = mBootstrap.connect(new InetSocketAddress(address, port));
            try {
                channel = future.sync().channel();
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
        return channel;
    }

    public static void release() {
        isInit = null;
        mBootstrap = null;
    }

}
