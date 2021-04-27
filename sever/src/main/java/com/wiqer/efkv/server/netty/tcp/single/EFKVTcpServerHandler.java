package com.wiqer.efkv.server.netty.tcp.single;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class EFKVTcpServerHandler extends SimpleChannelInboundHandler<String> {
    static volatile int count=0;
    static long startTime =0;    //获取开始时间
    static long endTime = System.currentTimeMillis();    //获取结束时间

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) {
        count++;
        long times=(endTime - startTime);
        if(count%10000==0){
            System.out.println("已经发送条数：" + count+"，总耗时:"+times);
        }
        if(startTime ==0 )startTime =System.currentTimeMillis();    //获取开始时间
        endTime = System.currentTimeMillis();
        ByteBuf content = Unpooled.copiedBuffer("ok\n", CharsetUtil.UTF_8);
        try {
            //同步发送
            //ctx.channel().writeAndFlush("ok\r\n").sync();
            ctx.writeAndFlush(content).sync();
        } catch (InterruptedException e) {
            System.out.println("flush error " + e.getMessage());
        }finally {
            content.release();
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }

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
