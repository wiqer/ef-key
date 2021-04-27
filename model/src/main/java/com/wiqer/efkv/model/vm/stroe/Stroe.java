package com.wiqer.efkv.model.vm.stroe;

import com.wiqer.efkv.model.msg.DataMsg;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface Stroe {
    public Object get(String key, String type, String scope  );

    public Object put(String key,Object val,String type,String scope  );

    public Object remove(String key,String type,String scope );

    public Object creat(String key,Object val,String type,String scope );

    //最后实现
    public Object delele(String type,String scope );


}
