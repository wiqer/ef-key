package com.wiqer.efkv.model.msg;

//服务端之间通讯的使用的消息
public class KVMsg   {
    private Msg msg;

    private Integer v;
    public KVMsg(Msg msg, Integer v) {
        this.msg = msg;
        this.v = v;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }


}
