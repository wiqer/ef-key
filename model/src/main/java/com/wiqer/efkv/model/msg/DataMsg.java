package com.wiqer.efkv.model.msg;

import com.wiqer.efkv.model.util.container.MQListMap;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

//存储的消息
public class DataMsg<T> {
    public  DataMsg(){

    }
    public  DataMsg(T data){
        this.data=(T)data;
    }
    public long createTime;//createTime
    public T data;//data
    public String id;//id

    public String creat(String k,String v){
        if(data instanceof ConcurrentSkipListMap){
            return ""+ ((MQListMap<String>) data).creat(v);
        }else if (data instanceof HashSet){
            return ""+ ((HashSet<String>) data).add(v);
        }else if (data instanceof ConcurrentHashMap){
            return ((ConcurrentHashMap<String,String>) data).put(k,v);
        }else {
            return (String)data;
        }
    }
    public String get(String b){
        if(data instanceof MQListMap){
            return  ((MQListMap<String>) data).get(Integer.getInteger(b));
        }else if (data instanceof HashSet){
            return ""+ ((HashSet<String>) data).contains(b);
        }else if (data instanceof ConcurrentHashMap){
            return ((ConcurrentHashMap<String,String>) data).get(b);
        }else {
            return (String)data;
        }
    }
    public String put(String k,String v){
        if(data instanceof MQListMap){
            return ""+ ((MQListMap<String>) data).put(Integer.getInteger(k),v);
        }else if (data instanceof ConcurrentHashMap){
            return ((ConcurrentHashMap<String,String>) data).put(k,v);
        }else {
            return (String)data;
        }
    }
    public String remove(String b){
        if(data instanceof MQListMap){
            return ""+ ((MQListMap<String>) data).remove(Integer.getInteger(b));
        }else if (data instanceof HashSet){
            return ""+ ((HashSet<String>) data).remove(b);
        }else if (data instanceof ConcurrentHashMap){
            return ((ConcurrentHashMap<String,String>) data).remove(b);
        }else {
            return (String)data;
        }
    }

}
