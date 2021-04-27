package com.wiqer.efkv.model.util.container;

import cn.hutool.core.date.SystemClock;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.atomic.LongAdder;
/*
* 1，消费者消费的窗口，一个管道对应一个datamsg
* 2，回复ACK时的调用getNextValueAndRemoveUsedNode
* */
public class Windows<V> {
    //数据源
    MQListMap<V> datamap;
    //volatile防止半加载
    //当前消费的数据节点节点
    volatile MQListMap.Node<Integer,V> concurrentUsedNode;
    //消费缓存窗口
    EasyFastSkipListMap<ChannelHandlerContext,WindowsChache<MQListMap.Node<Integer,V>>> Cachemap;
    //消费者消费到下标
    LongAdder UsedNodeAdder=new LongAdder();
    public Windows( MQListMap<V> datamap){
        this.datamap=datamap;
        Cachemap=new EasyFastSkipListMap<ChannelHandlerContext,WindowsChache<MQListMap.Node<Integer,V>>>();
        concurrentUsedNode= datamap.getFirst();
        if(null!=concurrentUsedNode) UsedNodeAdder.add(concurrentUsedNode.key);
    }
    public int getCacheMapSize(){
        return Cachemap.size();
    }
    public MQListMap.Node<Integer,V> removeUsedNode(ChannelHandlerContext channelHandlerContext){
        MQListMap.Node<Integer,V> usedNode;

        usedNode=Cachemap.remove(channelHandlerContext).v;

        if(null!=usedNode){
            if(concurrentUsedNode==usedNode){

            }else {
                datamap.remove(usedNode.key);
            }
        }
        return usedNode;
    }
    public V getNextAndASKChannel(ChannelHandlerContext channelHandlerContext){
        V consumeVal=null;
        MQListMap.Node<Integer,V> usedNode;
        if((usedNode=removeUsedNode(channelHandlerContext))!=null&&usedNode==concurrentUsedNode ){
           Integer usedKeyId= concurrentUsedNode.key;
            consumeVal=getNextValue(channelHandlerContext);
            //移除那个concurrentUsedNode游标对应的数据
            if(consumeVal!=null){
                datamap.remove(usedKeyId);
            }else {
                if(datamap.size()==0){
                    concurrentUsedNode=null;
                }

            }
        }
        return  consumeVal;
    }
    public V getNextValue(ChannelHandlerContext channelHandlerContext){
         MQListMap.Node<Integer,V> nextNode=getNext(channelHandlerContext);
        V kvMsg=null;
         if(nextNode!=null){

             kvMsg=nextNode.getValidValue();
         }
        return kvMsg;
    }
    //先获取 下一节点在移除消费完的节点
      MQListMap.Node<Integer,V>  getNext(ChannelHandlerContext channelHandlerContext){
        MQListMap.Node<Integer,V> consumeNode;
        WindowsChache<MQListMap.Node<Integer,V>> overtimeCache;
        EasyFastSkipListMap.Node<ChannelHandlerContext,WindowsChache<MQListMap.Node<Integer,V>>>  overtimeNode;
        //查看缓存里第一个元素
        if(null!=(overtimeCache=(overtimeNode=Cachemap.findFirst()).getValidValue())){
            if(overtimeCache!=null){
                //二十秒还没消费，可能消费者挂机了，有其他主机代为消费
                if(SystemClock.now()-overtimeCache.ct>20000){
                    //删除key为持有管道的过期数据
                    Cachemap.remove(overtimeNode.key);
                    overtimeCache.ct=SystemClock.now();
                    Cachemap.put(channelHandlerContext,overtimeCache);
                    return overtimeCache.v;
                }
            }

        }
          //查看缓存里自己绑定的元素
        if(null!=(overtimeCache=Cachemap.get(channelHandlerContext))){
            //超过5秒重发
            if(SystemClock.now()-overtimeCache.ct>5000){
                //删除key为持有管道的过期数据
                overtimeCache.ct=SystemClock.now();
                return overtimeCache.v;
            }
            //未超时则返回空
            return null;
        }
        //获取数据库中的元素
        //当前消费过的游标节点为空
        if(null==concurrentUsedNode){
            synchronized (this){
                //当前消费过的节点为空
                if(null==concurrentUsedNode){
                    concurrentUsedNode=consumeNode= datamap.getFirst();
                    if(null!=concurrentUsedNode){
                        UsedNodeAdder.add(concurrentUsedNode.key);
                        //消费过后计数器加1
                        UsedNodeAdder.increment();
                    }
                }else {
                    do{
                        concurrentUsedNode=consumeNode= datamap.getFirst(concurrentUsedNode,UsedNodeAdder.intValue());
                    UsedNodeAdder.increment();
                    }while (consumeNode!=null&&Cachemap.size()<datamap.size());
                }
            }
        }
        //当前消费过的节点不为空
        else {
            synchronized (this){
                do{
                    concurrentUsedNode=consumeNode= datamap.getFirst(concurrentUsedNode,UsedNodeAdder.intValue());
                    UsedNodeAdder.increment();
                }while (consumeNode!=null&&Cachemap.size()<datamap.size());
            }
        }
        if(consumeNode!=null){
            //concurrentUsedNode
            Cachemap.put(channelHandlerContext,new WindowsChache<MQListMap.Node<Integer,V>>(consumeNode));
        }
        return consumeNode;
       // Integer consumeNodeId =consumeNode.key;
//        synchronized (Cachemap){
//            while (){
//                for ( MQListMap.Node<Integer,V> node: Cachemap.values()){
//                    consumeNodeId
//                }
//            }
//
//        }

    }
    class  WindowsChache<V>{
        public V v;
        //缓存创建的时候
        public long ct= SystemClock.now();//createTime
        WindowsChache(V v){
            this.v=v;
        }
    }
}
