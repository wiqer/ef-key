package com.wiqer.efkv.model.vm.context;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;

public class TopicGroup extends MsgGroup{
    //指定消息发布者
    ArrayList<ChannelHandlerContext> producerChannelHandlerContexts;
}
