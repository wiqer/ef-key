package com.wiqer.efkv.server.starters;

import com.wiqer.efkv.model.tool.AsyncPool;
import com.wiqer.efkv.server.netty.tcp.multiple.EFKVMultipleTcpServer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
@Component
public class NodesServerStarter {
    @Resource
    EFKVMultipleTcpServer efkvMultipleTcpServer;
    @PostConstruct
    public void start() {
        AsyncPool.asyncDo(()->{
            try{
                efkvMultipleTcpServer.startMultipleTcpServer();
            }catch (Exception e){
                e.printStackTrace();
            }
        });

    }
}
