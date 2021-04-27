package com.wiqer.efkv.server.disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.wiqer.efkv.model.vm.RunTimeStroe;
import com.wiqer.efkv.model.wapper.MsgWapper;
import io.netty.channel.ChannelHandlerContext;

public class MsgEventProducer {
    private final RingBuffer<MsgWapper> ringBuffer;

    public MsgEventProducer(RingBuffer<MsgWapper> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslatorOneArg<MsgWapper,String> TRANSLATOR =
            new EventTranslatorOneArg<MsgWapper,String>()
            {
                @Override
                public void translateTo(MsgWapper book, long l, String s) {
//                    book.setBookId(l);
//                    book.setBookName(s);
                }
            };

    public void loadMsgWapper(MsgWapper booksource) {
        long sequence = ringBuffer.next();
        try {
            MsgWapper book = ringBuffer.get(sequence);
//            book.setBookId( booksource.getBookId());
//            book.setBookName(booksource.getBookName());
//            book.setBookType(booksource.getBookType());
        } finally {
            //最终的生产实际上靠的是这行代码
            ringBuffer.publish(sequence);
        }

    }
    public void loadMsgWapper(String jsonMsg, ChannelHandlerContext ctx, RunTimeStroe map) {
        long sequence = ringBuffer.next();
        try {
            MsgWapper book = ringBuffer.get(sequence);
//            book.setBookId( booksource.getBookId());
//            book.setBookName(booksource.getBookName());
//            book.setBookType(booksource.getBookType());
        } finally {
            //最终的生产实际上靠的是这行代码
            ringBuffer.publish(sequence);
        }

    }

    public void product(String name)
    {
        ringBuffer.publishEvent(TRANSLATOR, name);
    }

}
