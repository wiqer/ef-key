package com.wiqer.efkv.server.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.concurrent.ConcurrentHashMap;

public class EFKVServerInitializer extends ChannelInitializer<SocketChannel> {
        int nThreads;
        ConcurrentHashMap[] efKVMaps;
        public   EFKVServerInitializer( ConcurrentHashMap[] efKVMaps){
                this.nThreads=efKVMaps.length;
                this.efKVMaps=efKVMaps;
                efkvHttpServerHandler= new EFKVHttpServerHandler[nThreads];
                for (int i=0; i<efkvHttpServerHandler.length;i++) {
                        efkvHttpServerHandler[i]= new EFKVHttpServerHandler(this.efKVMaps);
                }

        }
        EFKVHttpServerHandler[]  efkvHttpServerHandler;

@Override
protected void initChannel(SocketChannel ch) throws Exception {
        //无法通过ch.pipeline().channel().eventLoop().parent().children.size = 16获取线程数
        //向管道加入处理器

        //得到管道
        ChannelPipeline pipeline = ch.pipeline();

        //加入一个netty 提供的httpServerCodec codec =>[coder - decoder]
        //HttpServerCodec 说明
        //1. HttpServerCodec 是netty 提供的处理http的 编-解码器
        //因为HttpServerCodec需要绑定http的状态，可能和channel有关，无法单实例，所以内存占用极大，频繁GC
        pipeline.addLast("HttpServerCodec",new HttpServerCodec());
        //2. 增加一个自定义的handler
        pipeline.addLast("EFKVHttpServerHandler", efkvHttpServerHandler[(int)(Thread.currentThread().getId())&(nThreads-1)]);//new EFKVHttpServerHandler(this.efKVMaps));//

        //System.out.println("ok~~~~");

        }
}
