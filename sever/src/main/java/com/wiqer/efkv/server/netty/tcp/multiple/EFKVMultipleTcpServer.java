package com.wiqer.efkv.server.netty.tcp.multiple;


import com.wiqer.efkv.model.rule.id.EFConstant;
import com.wiqer.efkv.model.vm.RunTimeStroe;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
/*
Java中该注解的说明：@PostConstruct该注解被用来修饰一个非静态的void（）方法。
被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次。
PostConstruct在构造函数之后执行，init（）方法之前执行。

 */
/**
 * 该server用于给各个微服务实例连接用。
 * MultipleTcpServer保证io线程优先，提供更大的io量
 * 不加日志，有必要才加，否则会堵塞io线程
 *      //值以下得参考
 * 1，https://blog.csdn.net/zguoshuaiiii/article/details/78533752
 * @author wuweifeng wrote on 2019-11-05.
 */
@Component
public class EFKVMultipleTcpServer {

    //ConcurrentHashMap性能不差，方便换型
    private RunTimeStroe[] MultipleStroe;
    @Value("${server.nodeSum}")
    private int nMaps;
    @Value("${server.startPort}")
    private int startPort;
    @Value("${server.nodeWorkerThreadSum}")
    private int nodeWorkerThreadSum;

    public EFKVMultipleTcpServer(int startPort,int nMaps){
       // this.nMaps= tableSizeFor(nMaps) ;
        this.nMaps=nMaps;
        this.startPort=startPort;
        MultipleStroe= new RunTimeStroe[this.nMaps];
        for (int i=0; i<MultipleStroe.length;i++) {

            MultipleStroe[i]= new RunTimeStroe();
        }
    }
    @SuppressWarnings("unused")
    public EFKVMultipleTcpServer() {
        // this.nMaps= tableSizeFor(nMaps) ;
//        if(this.nMaps==0|| this.startPort==0)
//                throw new Exception("startPort或nMaps未初始化");

    }

    public void startMultipleTcpServer() throws Exception {
        MultipleStroe= new RunTimeStroe[this.nMaps];
        for (int i=0; i<MultipleStroe.length;i++) {

            MultipleStroe[i]= new RunTimeStroe();
        }
        EventExecutorGroup multipleGroup = new DefaultEventExecutorGroup(this.nMaps);//业务线程池

        for (int i=0; i<this.nMaps;i++) {
            int nIndex=i;
            multipleGroup.execute(() -> {
                try {
                    if(this.nodeWorkerThreadSum==0){
                        this.startNettyServer(startPort+nIndex, MultipleStroe[nIndex],nIndex);
                    }else {
                        this.startNettyServer(startPort+nIndex, MultipleStroe[nIndex],this.nodeWorkerThreadSum);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
    public static void main(String[] args) throws Exception {
       new EFKVMultipleTcpServer(36668,8).startMultipleTcpServer();
    }
    public void startNettyServer(int port,RunTimeStroe multipleStroe,int nMapindex) throws Exception {


        //boss单线程
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
       // EventLoopGroup workerGroup = new NioEventLoopGroup(1);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, bossGroup)
                    .channel(NioServerSocketChannel.class)
                    //这是一种限流 措施，防止数据写入太大，导致消息堆积，内存不足的情况。
                    .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,new WriteBufferWaterMark(8*1024*1024,16*1024*1024))
                    //.handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //保持长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //出来网络io事件，如记录日志、对消息编解码等
                    .childHandler(new ChildChannelHandler(multipleStroe,nMapindex));
            //绑定端口，同步等待成功
            ChannelFuture future = bootstrap.bind(port).sync();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                bossGroup.shutdownGracefully (1000, 3000, TimeUnit.MILLISECONDS);
               // workerGroup.shutdownGracefully (1000, 3000, TimeUnit.MILLISECONDS);
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
           // workerGroup.shutdownGracefully();
        }
    }
    public void startNettyServer(int port,RunTimeStroe multipleStroe,int nMapindex,int NioEventLoopGroupSum) throws Exception {


        //boss单线程
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
         EventLoopGroup workerGroup = new NioEventLoopGroup(NioEventLoopGroupSum);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //这是一种限流 措施，防止数据写入太大，导致消息堆积，内存不足的情况。
                    .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,new WriteBufferWaterMark(8*1024*1024,16*1024*1024))
                    //.handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //保持长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //出来网络io事件，如记录日志、对消息编解码等
                    .childHandler(new ChildChannelHandler(multipleStroe,nMapindex));
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
        public ChildChannelHandler( ){

        }
        private RunTimeStroe map;
        int nMapindex;
        public ChildChannelHandler(int nMapindex){
            this.nMapindex= nMapindex;

//            }
        }
        public ChildChannelHandler(RunTimeStroe multipleStroe,int nMapindex){
           this(nMapindex);
           this.map=multipleStroe;
        }
//        EFKVTcpServerHandler[] serverHandlers ;//= new EFKVTcpServerHandler();
        ByteBuf delimiter = Unpooled.copiedBuffer(EFConstant.DELIMITER.getBytes());

        StringDecoder decoder=new StringDecoder();

        @Override
        protected void initChannel(Channel ch) {
            //向管道中添加拦截器
            ch.pipeline()
                    .addLast(new DelimiterBasedFrameDecoder(EFConstant.MAX_LENGTH, delimiter))// tcpFrameDecoder)//
                    .addLast(decoder)//new StringDecoder())//
                    //.addLast(new StringEncoder())//encoder)//
                    .addLast(new EFKVMultipleTcpServerHandler(map));//serverHandlers[(int)(Thread.currentThread().getId())&(nThreads-1)]);//
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
