package com.wiqer.efkv.model.msg;

import cn.hutool.core.date.SystemClock;
import com.wiqer.efkv.model.rule.id.EFNETID;
import com.wiqer.efkv.model.type.MsgType;
import com.wiqer.efkv.model.type.OperationType;
import com.wiqer.efkv.model.util.container.MQListMap;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

//真正传输的消息
//data:长数据自行处理，短数据交给fastgson处理
public class Msg {
    /**
     * 创建的时间
     */
    public long ct= SystemClock.now();//createTime
    public String k;//key
    public MsgType t;//Type
    public OperationType o;//OperationT
    public Object d;//data
//    public String db;//database
    public String s;//scope作用域
    public String i= EFNETID.randomID().toStringMore();//id

    @Override
    public String toString() {
        return "Msg{" +
                "ct=" + ct +
                ", k='" + k + '\'' +
                ", t=" + t +
                ", o=" + o +
                ", d=" + d +
                ", s='" + s + '\'' +
                ", i='" + i + '\'' +
                '}';
    }


    public long getCt() {
        return ct;
    }

    public void setCt(long ct) {
        this.ct = ct;
    }


    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public MsgType getT() {
        return t;
    }

    public void setT(MsgType t) {
        this.t = t;
    }

    public OperationType getO() {
        return o;
    }

    public void setO(OperationType o) {
        this.o = o;
    }

    public Object getD() {
        return d;
    }

    public void setD(Object d) {
        this.d = d;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }
//    STRING((byte) 1),
//    LIST((byte) 2),
//    MAP((byte) 3),
//    SET((byte) 4), //命中率
    public DataMsg creatDataMsg(){
        DataMsg dataMsg;
        if(t==MsgType.LIST){
            dataMsg= new DataMsg<MQListMap<String>>((MQListMap)d);
        }else if (t==MsgType.SET){
            dataMsg= new DataMsg<HashSet>((HashSet<String>)d);
        }else if (t==MsgType.MAP){
            dataMsg= new DataMsg<ConcurrentHashMap<String,String>>((ConcurrentHashMap)d);
        }else if (t==MsgType.STRING){
            dataMsg= new DataMsg<String>((String)d);
        }else {
            dataMsg= new DataMsg(d);
        }
        dataMsg.id=i;
        dataMsg.createTime=ct;
        return  dataMsg;
    }

}
