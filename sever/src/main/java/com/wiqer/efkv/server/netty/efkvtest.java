package com.wiqer.efkv.server.netty;

import io.netty.util.NettyRuntime;
import io.netty.util.internal.SystemPropertyUtil;

public class efkvtest {
    public static void main(String[] args) {
        //无参数启动NioEventLoopGroup分配的线程数
        System.out.println( Math.max(1, SystemPropertyUtil.getInt(
                "io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2)));
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        System.out.println( Math.max(1, SystemPropertyUtil.getInt(
                "io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2)));
    }
}
