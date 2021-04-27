package com.wiqer.efkv.model.vm.context;

import com.wiqer.efkv.model.msg.DataMsg;
import com.wiqer.efkv.model.msg.Msg;
import com.wiqer.efkv.model.tool.FlushUtil;
import com.wiqer.efkv.model.util.container.MQListMap;
import com.wiqer.efkv.model.util.container.Windows;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MsgGroup {
    //指定消息订阅者
    ArrayList<ChannelHandlerContext> consumerChannelHandlerContexts;
    Windows<DataMsg> msgWindows;
    private AtomicInteger roundRobin = new AtomicInteger(0);
    //轮训选取一个管道发送一个消息，减轻竞争
    public void  RobinPush(){
        //选中的管道
        ChannelHandlerContext chc;
                DataMsg dataMsg ;

        int ctcsIndex=roundRobin.addAndGet(1);
        chc=consumerChannelHandlerContexts.get(ctcsIndex%consumerChannelHandlerContexts.size());
        dataMsg=msgWindows.getNextValue(chc);
        //消费者掉线直接移除对方
        if(chc!=null&&chc.isRemoved()){
            consumerChannelHandlerContexts.remove(ctcsIndex);
        }//发送订阅的消息
        else if(dataMsg!=null&&chc!=null&&!chc.isRemoved()){
            FlushUtil.flush(chc,dataMsg);
        }
        //此处
    }
   public void addConsumer(ChannelHandlerContext chc){
       consumerChannelHandlerContexts.add(chc);
    }
}
