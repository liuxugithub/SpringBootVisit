package godday.xin.netty.Websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class WSServer {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup maingroup=new NioEventLoopGroup();
        EventLoopGroup slavegroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(maingroup, slavegroup).channel(NioServerSocketChannel.class)
                    .childHandler(new WSSInitializer());
            ChannelFuture channelFuture=serverBootstrap.bind(8089).sync();
            channelFuture.channel().closeFuture().sync();
        }
      finally {
            maingroup.shutdownGracefully();
            slavegroup.shutdownGracefully();
        }
    }
}
