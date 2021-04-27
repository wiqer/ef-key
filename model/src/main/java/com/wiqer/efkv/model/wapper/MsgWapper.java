package com.wiqer.efkv.model.wapper;

import com.wiqer.efkv.model.msg.Msg;
import com.wiqer.efkv.model.tool.FastJsonUtils;
import com.wiqer.efkv.model.vm.RunTimeStroe;
import io.netty.channel.ChannelHandlerContext;

public class MsgWapper {

    public Msg msg;
    public ChannelHandlerContext ctx;
    public  RunTimeStroe map;
    public MsgWapper(){

    }
    public MsgWapper(Msg msg, ChannelHandlerContext ctx, RunTimeStroe map) {
        this.msg = msg;
        this.ctx = ctx;
        this.map = map;
    }
    public MsgWapper(String jsonMsg, ChannelHandlerContext ctx, RunTimeStroe map) {

        this.msg =FastJsonUtils.toBean(jsonMsg, Msg.class);
        this.ctx = ctx;
        this.map = map;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }
    public void setMsg(String jsonMsg) {
        this.msg = FastJsonUtils.toBean(jsonMsg, Msg.class);
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public RunTimeStroe getMap() {
        return map;
    }

    public void setMap(RunTimeStroe map) {
        this.map = map;
    }

}
