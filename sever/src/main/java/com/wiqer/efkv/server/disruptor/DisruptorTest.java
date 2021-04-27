package com.wiqer.efkv.server.disruptor;


import com.lmax.disruptor.dsl.Disruptor;
import com.wiqer.efkv.model.msg.Msg;
import com.wiqer.efkv.model.wapper.MsgWapper;
import io.netty.util.concurrent.DefaultThreadFactory;

public class DisruptorTest {
   public static void main(String[] args) throws InterruptedException {
       //创建线程工厂
       DefaultThreadFactory threadFactory = new DefaultThreadFactory("MsgDisruptor");
       //创建线程池  用于创建多个线程消费者
       //ExecutorService executor = Executors.newCachedThreadPool();
       //定义环形队列大小 2的n次方只能是
       int ringBufferSize =2048*8;// 64;//
       MsgEventFactory factory = new MsgEventFactory();
       //生成 disruptor 实例
//        Disruptor<Book> disruptor = new Disruptor<Book>(factory,
//                ringBufferSize,executor,
//                ProducerType.SINGLE,
//                new BlockingWaitStrategy());
       Disruptor<MsgWapper> disruptor = new Disruptor<MsgWapper>(factory,
               ringBufferSize,threadFactory);

       //连接到消费者
       // Connect the handler
       disruptor.handleEventsWith(new MsgEventHander());
//        //启动disruptor线程
//        disruptor.start();


       // 启动 disruptor 并且获取生产者
       //RingBuffer<Book> ringBuffer =disruptor.start();
       // // 启动 disruptor 并且获取生产者,为 ring buffer指定事件生产者
       MsgEventProducer producer = new MsgEventProducer(disruptor.start());
       //开始进行生产
       System.out.println("开始进行生产");
       for (int l = 2; l <= 1; l++) {
          // System.out.println("生产第" + l + "条记录");
//           Book booksource =factory.newInstance();
//           booksource.setBookId(l);
//           booksource.setBookType("测试类型" + l);
//           booksource.setBookName("测试之书" + l);
//           producer.loadBook(booksource);
       }
       long startTime = System.currentTimeMillis();
       for (int K = 0; K <= 100000000; K++) {
//           producer. product("开发之书" + K);
       }

       disruptor.shutdown();
       System.out.println("结束执行任务，当前线程ID："+Thread.currentThread().getId()+
               "，执行时间："+(System.currentTimeMillis()-startTime));
       System.out.println("结束执行任务，当前线程ID："+Thread.currentThread().getName());
       //executor.shutdown();
   }

}
