package godday.xin.sixin.Handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    //用于记录和管理所有客户端的channel
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String get_str = textWebSocketFrame.text();
        System.out.println("接受到到消息：" + get_str);
        for (Channel channel : channels) {
            channel.writeAndFlush(new TextWebSocketFrame("【服务器收到消息】:" + LocalDateTime.now() + "消息为：" + get_str));
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //当客户端连接服务端之后 获取 客户端当channle ,拉进 channelgroup进行管理
        channels.add(ctx.channel());
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //当触发handlerRemoved ChannelGroup 会自动移除对应当channel
        //channels.remove(ctx.channel());
        System.out.println("客户端断开：channel对应当长id为:" + ctx.channel().id().asLongText());
        System.out.println("客户端断开：channel对应当短id为:" + ctx.channel().id().asShortText());
        super.handlerRemoved(ctx);
    }
}