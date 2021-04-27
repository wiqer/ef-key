package com.wiqer.efkv.server.netty.tcp.single;


import com.wiqer.efkv.model.rule.id.EFConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * 该server用于给各个微服务实例连接用。
 *
 * @author wuweifeng wrote on 2019-11-05.
 */
public class EFKVTcpServer {
    public static void main(String[] args) throws Exception {
        new EFKVTcpServer().startNettyServer(36668,1);
    }
    public void startNettyServer(int port,int nThreads) throws Exception {
        nThreads= tableSizeFor(nThreads);
        //boss单线程
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(nThreads);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //.handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //保持长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //出来网络io事件，如记录日志、对消息编解码等
                    .childHandler(new ChildChannelHandler(nThreads));
            //绑定端口，同步等待成功
            ChannelFuture future = bootstrap.bind(port).sync();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                bossGroup.shutdownGracefully (1000, 3000, TimeUnit.MILLISECONDS);
                workerGroup.shutdownGracefully (1000, 3000, TimeUnit.MILLISECONDS);
            }));
            //等待服务器监听端口关闭
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            //do nothing
            System.out.println("netty stop");
        } finally {
            //优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * handler类
     */
    private class ChildChannelHandler extends ChannelInitializer<Channel> {
        int nThreads;
        public ChildChannelHandler(int nThreads){
            this.nThreads= nThreads;
            serverHandlers= new EFKVTcpServerHandler[nThreads];
            for (int i=0; i<serverHandlers.length;i++) {
                serverHandlers[i]= new EFKVTcpServerHandler();
            }
        }
        EFKVTcpServerHandler[] serverHandlers ;//= new EFKVTcpServerHandler();
        ByteBuf delimiter = Unpooled.copiedBuffer(EFConstant.DELIMITER.getBytes());
        DelimiterBasedFrameDecoder frameDecoder=   new DelimiterBasedFrameDecoder(EFConstant.MAX_LENGTH, delimiter);
        StringDecoder decoder=new StringDecoder();
       // StringEncoder encoder =new StringEncoder();
        HashMap<ChannelId,DelimiterBasedFrameDecoder> frameDecoderMap=new HashMap<>();
        @Override
        protected void initChannel(Channel ch) {
            DelimiterBasedFrameDecoder tcpFrameDecoder;
            if(null==(tcpFrameDecoder=frameDecoderMap.get(ch.id()))){
                tcpFrameDecoder= frameDecoderMap.put(ch.id(),  new DelimiterBasedFrameDecoder(EFConstant.MAX_LENGTH, delimiter));
            }
            ch.pipeline()
                   .addLast( tcpFrameDecoder)//new DelimiterBasedFrameDecoder(EFConstant.MAX_LENGTH, delimiter))//
                    .addLast(decoder)//new StringDecoder())//
                    //.addLast(new StringEncoder())//encoder)//
                    .addLast(new EFKVTcpServerHandler());//serverHandlers[(int)(Thread.currentThread().getId())&(nThreads-1)]);//
        }
    }
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= 64) ? 64 : n + 1;
    }

}
