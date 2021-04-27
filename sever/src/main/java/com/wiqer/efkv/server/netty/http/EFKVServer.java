package com.wiqer.efkv.server.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.ConcurrentHashMap;

public class EFKVServer {
    int nThreads;
    ConcurrentHashMap[] efKVMaps;
    public  EFKVServer(int nThreads){
        nThreads= tableSizeFor(nThreads);
        this. nThreads = nThreads;
        efKVMaps=new ConcurrentHashMap[nThreads];
//        for (ConcurrentHashMap<String,String> efKVMap: efKVMaps) {
//            efKVMap=new ConcurrentHashMap<String,String>();
//        }
        for (int i=0; i<efKVMaps.length;i++) {
            efKVMaps[i]=new ConcurrentHashMap<String,String>();
        }
        System.out.println("初始化-》EFKVServer=" + Thread.currentThread().getId());
    }
    public static void main(String[] args) throws Exception {
        new EFKVServer(8).startNettyServer(26668);
    }
    public  void startNettyServer(int port)throws Exception {
        //
        //DefaultEventExecutorGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(nThreads);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new EFKVServerInitializer(this.efKVMaps))
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //保持长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //Boss线程池内存池配置.底层IO处理线程的缓冲区使用堆外直接缓冲区，减少一次IO复制.
                    // 业务消息的编解码使用堆缓冲区,分配效率更高,而且不涉及到内核缓冲区的复制问题.
                    //使用直接内存性能提升不大，瓶颈在io
                    // 配合业务逻辑，在内存池中申请直接内存,使用此方式对吞吐量性能提升不大，但是对内存占用量优化20%
                    // ByteBuf directByteBuf = ByteBufAllocator.DEFAULT.directBuffer(1024);
                    // 归还到内存池
                    //directByteBuf.release();
                    .option(ChannelOption.ALLOCATOR,PooledByteBufAllocator.DEFAULT)
                    //Work线程池内存池配置.
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
            ;




            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            channelFuture.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
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
