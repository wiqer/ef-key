 package com.wiqer.efkv.server.netty.tcp.multiple;


 import com.wiqer.efkv.model.msg.Msg;
 import com.wiqer.efkv.model.tool.FastJsonUtils;
 import com.wiqer.efkv.model.tool.FlushUtil;
 import com.wiqer.efkv.model.vm.RunTimeStroe;
 import io.netty.buffer.ByteBuf;
 import io.netty.buffer.Unpooled;
 import io.netty.channel.ChannelHandler;
 import io.netty.channel.ChannelHandlerContext;
 import io.netty.channel.SimpleChannelInboundHandler;
 import io.netty.util.CharsetUtil;
 import org.springframework.util.StringUtils;

 @ChannelHandler.Sharable
public class EFKVMultipleTcpServerHandler extends SimpleChannelInboundHandler<String> {
     private RunTimeStroe map;

     public EFKVMultipleTcpServerHandler(RunTimeStroe map){
        this.map=map;

    }
    static volatile int count=0;
    static long startTime =0;    //获取开始时间
    static long endTime = System.currentTimeMillis();    //获取结束时间

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) {
        if (StringUtils.isEmpty(message)) {
            return;
        }
        count++;
        //System.out.println("消息:"+message+",="+integer);
        long times=(endTime - startTime);
        if(count%10000==0){
            System.out.println("已经发送条数：" + count+"，总耗时:"+times);
        }
        if(startTime ==0 )startTime =System.currentTimeMillis();    //获取开始时间
        endTime = System.currentTimeMillis();
        Msg msg = FastJsonUtils.toBean(message, Msg.class);
        FlushUtil.flush(ctx,map.receiveMsg(msg));

//        ByteBuf content = Unpooled.copiedBuffer("ok\r\n", CharsetUtil.UTF_8);
//        try {
//            //同步发送
//            //ctx.channel().writeAndFlush("ok\r\n").sync();
//            ctx.writeAndFlush(content).sync();
//        } catch (InterruptedException e) {
//            System.out.println("flush error " + e.getMessage());
//        }
//        //1，不是放内存吞吐量会提升，8个工作线程，吞吐量40万，一般性能会提升4倍，代价是内存好用一个线程一个G
//        //会走以下方式释放 AbstractChannelHandlerContext->ReferenceCountUtil.release(msg);
//        //2，开启后吞吐量会下降一半但是内存占用极低，cpu占用率也会拉低，GC频率也会拉低
//        //3,SimpleChannelInboundHandler中的read会释放，ChannelInboundHandlerAdapter的read不会释放
//        //体现在ChannelInboundHandlerAdapter->channelRead-> ReferenceCountUtil.release(msg);
//        //4，不立即释放的代价是大量消息积压在对外内存，会导致申请内存越来越大。
//        //5,只要客户端不会频繁发送大量消息，问题不会爆露
//        //6，建议开启自动释放，除非服务器是EFKV专用服务器
//        //7，开启content的release基于4.1.42.Final，吞吐量8线程11w，但是不会出现内存溢出问题
//        //8,关闭content的release可以考虑4版内的高版本netty
//        finally {
//            content.release();
//        }


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
