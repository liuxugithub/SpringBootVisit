package godday.xin.sixin.utils;

import godday.xin.sixin.Handler.ChatHandler;
import godday.xin.sixin.Handler.WSSInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

@Component
public class WssServer {
    private EventLoopGroup maingroup;
    private EventLoopGroup slavegroup;
    private ServerBootstrap serverBootstrap;
    private ChannelFuture channelFuture;

    private static class SingletionWsserver{
        static WssServer instanct = new WssServer();
    }
    public static WssServer getInstance(){
        return SingletionWsserver.instanct;
    }
    public WssServer(){
        maingroup=new NioEventLoopGroup();
        slavegroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(maingroup, slavegroup).channel(NioServerSocketChannel.class)
                .childHandler(new WSSInitializer());
    }
    public void start() throws InterruptedException {
        this.channelFuture=serverBootstrap.bind(8080);
        System.out.println("netty websocket 启动成功！！！");
    }
}
