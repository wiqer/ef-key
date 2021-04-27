package com.wiqer.efkv.server.disruptor;


import com.lmax.disruptor.EventHandler;
import com.wiqer.efkv.model.msg.Msg;
import com.wiqer.efkv.model.wapper.MsgWapper;

public class MsgEventHander implements EventHandler<MsgWapper> {
    @Override
    public void onEvent(MsgWapper msgWapper, long sequence, boolean endOfBatch) throws InterruptedException {
        //String threandName = Thread.currentThread().getName();
        //String resultT = "consumer:consume one ->thread name : {0} , event :{1},endOfBatch:{2}";
        //String result = MessageFormat.format(resultT,threandName,book.getBookName(),endOfBatch);
        //System.out.println(result);
        //模拟业务处理时间
       // Thread.sleep(5000L);

       // book.setBookName(book.getBookName()+"SYN");
    }
}
