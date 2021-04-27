package com.wiqer.efkv.server.disruptor;


import com.lmax.disruptor.EventFactory;
import com.wiqer.efkv.model.msg.Msg;
import com.wiqer.efkv.model.wapper.MsgWapper;

public class MsgEventFactory implements EventFactory<MsgWapper>
{

    @Override
    public MsgWapper newInstance() {
        return new MsgWapper();
    }

}

