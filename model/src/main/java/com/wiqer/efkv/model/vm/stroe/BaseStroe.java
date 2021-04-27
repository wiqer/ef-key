package com.wiqer.efkv.model.vm.stroe;

import com.wiqer.efkv.model.msg.DataMsg;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BaseStroe implements Stroe{



//    STRING((byte) 1),
//    LIST((byte) 2),
//    MAP((byte) 3),
//    SET((byte) 4), //命中率
//    SET_COUNT((byte) 5),
//    HOT_MAP_COUNT((byte) 6),
    //作用域->key->数据
//    private ConcurrentHashMap<String,ConcurrentHashMap<String,DataMsg>> STRING;
//    private ConcurrentHashMap<String,ConcurrentHashMap<String, List<DataMsg>>> LIST;
//    private ConcurrentHashMap<String,ConcurrentHashMap<String, ConcurrentHashMap<String, DataMsg>>> MAP;
//    private ConcurrentHashMap<String,ConcurrentHashMap<String, Set<DataMsg>>> SET;
//    private ConcurrentHashMap<String,ConcurrentHashMap<String, Set<DataMsg>>> SET_COUNT;
//    private ConcurrentHashMap<String,ConcurrentHashMap<String, DataMsg>> HOT_MAP_COUNT;
    //数据类型->作用域->key->数据
    private HashMap<String, ConcurrentHashMap<String,ConcurrentHashMap<String, DataMsg>>> stroeLoacl;
    public Object get(String key,String type,String scope ){
        HashMap<String, ConcurrentHashMap<String,ConcurrentHashMap<String, DataMsg>>> stroe=this.stroeLoacl;
        if(null!=stroe){
            ConcurrentHashMap typeNode;
            if(null!=(typeNode=stroe.get(type))){
                ConcurrentHashMap scopeNode;
                if(null!=(scopeNode= (ConcurrentHashMap) typeNode.get(scope))){
                    return  scopeNode.get(key);
                }

            }
        }
        return null;
    }
//    Object get(String key ){
//        return this.get(key,"STRING","default");
//    }
    public Object put(String key,Object val,String type,String scope ){
        HashMap<String, ConcurrentHashMap<String,ConcurrentHashMap<String, DataMsg>>> stroe=this.stroeLoacl;
        ConcurrentHashMap typeNode;
        ConcurrentHashMap<String, DataMsg> scopeNode;
        Object obj;
        if(null==stroe)this.stroeLoacl=new HashMap<>();
        if(null==(typeNode=stroe.get(type))){
            typeNode=new ConcurrentHashMap();
            this.stroeLoacl.put(type,typeNode);
        }
        if(null==(scopeNode= (ConcurrentHashMap<String, DataMsg>) typeNode.get(scope))){
            scopeNode=new ConcurrentHashMap<>();
            this.stroeLoacl.get(type).put(scope,scopeNode);
        }

        if(null==(scopeNode= (ConcurrentHashMap<String, DataMsg>) typeNode.get(scope))){
            scopeNode=new ConcurrentHashMap<>();

            typeNode.put(type,scopeNode);
        }
        return  scopeNode.put(key,(DataMsg)val);
    }

//    Object put(String key ,Object val){
//        return this.put(key,val,"STRING","default");
//    }
    public Object remove(String key,String type,String scope  ){
        HashMap<String, ConcurrentHashMap<String,ConcurrentHashMap<String, DataMsg>>> stroe=this.stroeLoacl;
        if(null!=stroe){
            ConcurrentHashMap typeNode;
            if(null!=(typeNode=stroe.get(type))){
                ConcurrentHashMap scopeNode;
                if(null!=(scopeNode= (ConcurrentHashMap) typeNode.get(scope))){
                    return  scopeNode.remove(key);


                }

            }
        }
        return null;
    }
    public Object insideGet(String key,String type,String scope ){
        HashMap<String, ConcurrentHashMap<String,ConcurrentHashMap<String, DataMsg>>> stroe=this.stroeLoacl;
        if(null!=stroe){
            ConcurrentHashMap typeNode;
            if(null!=(typeNode=stroe.get(type))){
                ConcurrentHashMap scopeNode;
                if(null!=(scopeNode= (ConcurrentHashMap) typeNode.get(scope))){
                    return  scopeNode.get(key);
                }

            }
        }
        return null;
    }
    //    Object get(String key ){
//        return this.get(key,"STRING","default");
//    }
    public Object insidePut(String key,Object val,String type,String scope  ){
        HashMap<String, ConcurrentHashMap<String,ConcurrentHashMap<String, DataMsg>>> stroe=this.stroeLoacl;
        ConcurrentHashMap typeNode;
        ConcurrentHashMap<String, DataMsg> scopeNode;
        Object obj;
        if(null==stroe)this.stroeLoacl=new HashMap<>();
        if(null==(typeNode=stroe.get(type))){
            typeNode=new ConcurrentHashMap();
            this.stroeLoacl.put(type,typeNode);
        }
        if(null==(scopeNode= (ConcurrentHashMap<String, DataMsg>) typeNode.get(scope))){
            scopeNode=new ConcurrentHashMap<>();
            this.stroeLoacl.get(type).put(scope,scopeNode);
        }

        if(null==(scopeNode= (ConcurrentHashMap<String, DataMsg>) typeNode.get(scope))){
            scopeNode=new ConcurrentHashMap<>();

            typeNode.put(type,scopeNode);
        }
            return  scopeNode.put(key,(DataMsg)val);
    }

    //    Object put(String key ,Object val){
//        return this.put(key,val,"STRING","default");
//    }
    public Object insideRemove(String key,String type,String scope ){
        HashMap<String, ConcurrentHashMap<String,ConcurrentHashMap<String, DataMsg>>> stroe=this.stroeLoacl;
        if(null!=stroe){
            ConcurrentHashMap typeNode;
            if(null!=(typeNode=stroe.get(type))){
                ConcurrentHashMap scopeNode;
                if(null!=(scopeNode= (ConcurrentHashMap) typeNode.get(scope))){
                    return  scopeNode.remove(key);
                }

            }
        }
        return null;
    }

//    Object remove(String key ){
//        return this.remove(key,"STRING","default");
//    }
    public Object creat(String key,Object val,String type,String scope ){
        HashMap<String, ConcurrentHashMap<String,ConcurrentHashMap<String, DataMsg>>> stroe=this.stroeLoacl;
        ConcurrentHashMap typeNode;
        ConcurrentHashMap<String, DataMsg> scopeNode;
        Object obj;
        if(null==stroe)this.stroeLoacl=new HashMap<>();
        if(null==(typeNode=stroe.get(type))){
            typeNode=new ConcurrentHashMap();
            this.stroeLoacl.put(type,typeNode);
        }
        if(null==(scopeNode= (ConcurrentHashMap<String, DataMsg>) typeNode.get(scope))){
            scopeNode=new ConcurrentHashMap<>();
            this.stroeLoacl.get(type).put(scope,scopeNode);
        }

        if(null!=(obj=scopeNode.get(key))){
            return obj;
        }else {
            return scopeNode.put(key,(DataMsg)val);
        }

    }

//    Object creat(String key,Object val ){
//        return this.creat(key, val,"STRING","default");
//    }
    public Object delele(String type,String scope ){
        HashMap<String, ConcurrentHashMap<String,ConcurrentHashMap<String, DataMsg>>> stroe=this.stroeLoacl;
        if(null!=stroe){
            ConcurrentHashMap typeNode;
            if(null!=(typeNode=stroe.get(type))){
                ConcurrentHashMap scopeNode;
                if(null!=(scopeNode= (ConcurrentHashMap) typeNode.get(scope))){

                    return  scopeNode.remove(scope);
                }else {
                    return null;
                }

            }
        }
        return null;
    }

//    Object delele(){
//        return this.delele("STRING","default");
//    }

}
