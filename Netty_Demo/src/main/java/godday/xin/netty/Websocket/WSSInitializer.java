package godday.xin.netty.Websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WSSInitializer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline=socketChannel.pipeline();

        channelPipeline.addLast(new HttpServerCodec());

        //对写大数据流对支持
        channelPipeline.addLast(new ChunkedWriteHandler());

        ///对 httpmessage 进行聚合，聚合成Fullhttprequest 或者 FullHttpResponse
        // 机会在所有对netty中的编程中，都会使用到此handler
        channelPipeline.addLast(new HttpObjectAggregator(1024*64));


        //websorcket 服务器处理协议，用于指定给客户端 连接访问到路由 :/ws
        //此handler 会帮你处理很多复杂工作，会帮你处理握手动作
        //对于websorcket来说，都是以frames进行传输到，不同类型的数据类型frames也不同

        channelPipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        channelPipeline.addLast("", new ChatHandler());




    }
}
