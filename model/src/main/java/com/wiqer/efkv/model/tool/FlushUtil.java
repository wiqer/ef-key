package com.wiqer.efkv.model.tool;

import com.wiqer.efkv.model.rule.id.EFConstant;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

public class FlushUtil {
    /**
     * 往channel里输出消息
     */
    public static void flush(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        try {
            //同步发送
            channelHandlerContext.channel().writeAndFlush(byteBuf).sync();
        } catch (InterruptedException e) {

        }
        //1，不是放内存吞吐量会提升，8个工作线程，吞吐量40万，一般性能会提升4倍，代价是内存好用一个线程一个G
        //会走以下方式释放 AbstractChannelHandlerContext->ReferenceCountUtil.release(msg);
        //2，开启后吞吐量会下降一半但是内存占用极低，cpu占用率也会拉低，GC频率也会拉低
        //3,SimpleChannelInboundHandler中的read会释放，ChannelInboundHandlerAdapter的read不会释放
        //体现在ChannelInboundHandlerAdapter->channelRead-> ReferenceCountUtil.release(msg);
        //4，不立即释放的代价是大量消息积压在对外内存，会导致申请内存越来越大。
        //5,只要客户端不会频繁发送大量消息，问题不会爆露
        //6，建议开启自动释放，除非服务器是EFKV专用服务器
        //7，开启content的release基于4.1.42.Final，吞吐量8线程11w，但是不会出现内存溢出问题
        //8,关闭content的release可以考虑4版内的高版本netty
        finally {
            byteBuf.release();
        }
    }
    public static void flush(ChannelHandlerContext channelHandlerContext, Object obj) {
        ByteBuf byteBuf = Unpooled.copiedBuffer(FastJsonUtils.convertObjectToJSON(obj)+ EFConstant.DELIMITER, CharsetUtil.UTF_8);
        try {
            //同步发送
            channelHandlerContext.channel().writeAndFlush(byteBuf).sync();
        } catch (InterruptedException e) {

        }
        //1，不是放内存吞吐量会提升，8个工作线程，吞吐量40万，一般性能会提升4倍，代价是内存好用一个线程一个G
        //会走以下方式释放 AbstractChannelHandlerContext->ReferenceCountUtil.release(msg);
        //2，开启后吞吐量会下降一半但是内存占用极低，cpu占用率也会拉低，GC频率也会拉低
        //3,SimpleChannelInboundHandler中的read会释放，ChannelInboundHandlerAdapter的read不会释放
        //体现在ChannelInboundHandlerAdapter->channelRead-> ReferenceCountUtil.release(msg);
        //4，不立即释放的代价是大量消息积压在对外内存，会导致申请内存越来越大。
        //5,只要客户端不会频繁发送大量消息，问题不会爆露
        //6，建议开启自动释放，除非服务器是EFKV专用服务器
        //7，开启content的release基于4.1.42.Final，吞吐量8线程11w，但是不会出现内存溢出问题
        //8,关闭content的release可以考虑4版内的高版本netty
        finally {
            byteBuf.release();
        }
    }


}
