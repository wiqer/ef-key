package com.wiqer.efkv.model.msg;

import java.util.List;
//追加筛选条件时使用
public class MagicMsg extends KVMsg{
    public MagicMsg(Msg msg, Integer v) {
        super(msg, v);
    }

    public List<String> getW() {
        return w;
    }

    public void setW(List<String> w) {
        this.w = w;
    }

    //条件
    public List<String>  w;
}
