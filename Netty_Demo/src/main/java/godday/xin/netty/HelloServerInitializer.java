package godday.xin.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import sun.net.www.protocol.ftp.Handler;

import java.nio.channels.Pipe;

/*
初始化器，channer 注册后，会执行里面相应的初始化方法
 */
public class HelloServerInitializer  extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel socketChannel) throws Exception {



        ChannelPipeline pip=socketChannel.pipeline();

        //通过管道，添加handler
        //httpservercodec 是由netty自己提供的助手类，可以理解为拦截器
        //当请求到服务器，我们需要做编解码，响应到客户端做编码
        pip.addLast("HttpServerCodec",new HttpServerCodec());

        //添加自定义到助手类，返回 hello,netty
        pip.addLast("CustomerHandler",new CustomerHandler());

    }
}
