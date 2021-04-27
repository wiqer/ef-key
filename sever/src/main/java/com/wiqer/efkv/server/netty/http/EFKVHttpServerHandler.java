package com.wiqer.efkv.server.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.nio.CharBuffer;
import java.util.concurrent.ConcurrentHashMap;

//在AbstractChannelHandlerContext的invokeChannelRead方法中打断点调试
@ChannelHandler.Sharable
public class EFKVHttpServerHandler  extends SimpleChannelInboundHandler<HttpObject> {
    int nThreads;
    ConcurrentHashMap[] efKVMaps;
    long threadId;
    public EFKVHttpServerHandler(ConcurrentHashMap[] efKVMaps){
       this.efKVMaps=efKVMaps;
        nThreads= efKVMaps.length;
        threadId=Thread.currentThread().getId();
    }

    //channelRead0 读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //第三种 直接复用mag，实现零拷贝,无法实现


        //第二种 使用内存池分配器创建直接内存缓冲区分配资源
        ByteBuf directByteBufContent = ByteBufUtil.encodeString(ByteBufAllocator.DEFAULT, CharBuffer.wrap("hello"), CharsetUtil.UTF_8);
        //构造一个http的相应，即 httpresponse
        FullHttpResponse directResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, directByteBufContent);
        // response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        directResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, directByteBufContent.readableBytes());
        try {
            //同步发送，直接在管道尾部发送，不走处理器Handler
           // ctx.writeAndFlush(directResponse).sync();
            ctx.writeAndFlush(directResponse).sync();
        } catch (InterruptedException e) {
            System.out.println("flush error " + e.getMessage());
        }
        finally {
            //归还直接内存,不调用释放，会在GC时释放，系统运行不平稳，多机器时运行会出现吞吐量瓶颈
            directByteBufContent.release();
        }
//        //第一种 使用非堆内存分配器创建的直接内存缓冲区
//        ByteBuf content = Unpooled.copiedBuffer("hello", CharsetUtil.UTF_8);
//        //构造一个http的相应，即 httpresponse
//        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
//       // response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
//        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
//        try {
//            //同步发送
//            ctx.channel().writeAndFlush(response).sync();
//        } catch (InterruptedException e) {
//            System.out.println("flush error " + e.getMessage());
//        }finally {
//            //不归还内存会导致内存飙升,相当于 response.release();
//            content.release();
//
//        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //System.out.println("some thing is error , " + cause.getMessage());
    }

//    //解决高并发异常：服务端未给客户端回应消息前客户端提前关闭连接的异常
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
////        super.exceptionCaught(ctx, cause);
////        Channel channel = ctx.channel();
////        if(channel.isActive())ctx.close();
//    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
        super.channelInactive(ctx);
    }
}
