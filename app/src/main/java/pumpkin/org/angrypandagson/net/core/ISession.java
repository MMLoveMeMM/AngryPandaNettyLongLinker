package pumpkin.org.angrypandagson.net.core;

import io.netty.channel.ChannelHandlerContext;

/**
 * @ProjectName: bugfix_speech_v0.9.5
 * @ClassName: ISession
 * @Author: 刘志保
 * @CreateDate: 2019/9/9 15:24
 * @Description: java类作用描述
 */
public interface ISession /*extends ChannelInboundHandler*/ {
    void channelRead(ChannelHandlerContext channelHandlerContext, String datas);
    void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable);
}
