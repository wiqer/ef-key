package com.wiqer.efkv.model.vm;

import com.wiqer.efkv.model.msg.DataMsg;

import com.wiqer.efkv.model.msg.Msg;
import com.wiqer.efkv.model.vm.stroe.BaseStroe;
import com.wiqer.efkv.model.vm.stroe.Stroe;
import com.wiqer.efkv.model.vm.stroe.meta.MetaStroe;
import com.wiqer.efkv.model.vm.stroe.old.TenuredStroe;
import com.wiqer.efkv.model.vm.stroe.young.YoungStroe;

import java.util.List;
import java.util.Map;

public class RunTimeStroe implements Stroe {
    YoungStroe youngStroe;
    //主从一致
    TenuredStroe tenuredStroe;
    //持久化
    MetaStroe metaStroe;
    //发布订阅


    public RunTimeStroe(){

        youngStroe=new YoungStroe();
    }
//    STRING((byte) 1),
//    LIST((byte) 2),
//    MAP((byte) 3),
//    SET((byte) 4), //命中率
//    SET_COUNT((byte) 5),
//    HOT_MAP_COUNT((byte) 6),
//    EMPTY((byte) 7),
//    PONG((byte) 8),
//    PING((byte) 9); //热key，worker->dashboard

//    CREAT,
//    REMOVE,
//    PUT,
//    GET,
//    DELETE,
//    CP_TO,
//    CP_FROM
    public Object receiveMsg(Msg msg){
        try{
            switch(msg.o){
                case CREAT :
                    return creat(msg.k,msg.creatDataMsg(), msg.t.toString(), msg.s);

                case REMOVE :
                    return remove(msg.k, msg.t.toString(), msg.s);

                //你可以有任意数量的case语句
                case PUT :
                    return put(msg.k,msg.creatDataMsg(), msg.t.toString(), msg.s);

                case GET :
                    return get(msg.k,msg.creatDataMsg().toString(), msg.s);

                case DELETE :{
                    return delele(msg.t.toString(), msg.s);
                }
                case IN_CREAT:{
                    DataMsg dataMsg=  (DataMsg)get(msg.k,msg.creatDataMsg().toString(), msg.s);
                    switch(msg.t){
                        case LIST :{
                            for(String s: (List<String>) msg.d){
                                dataMsg.put(null,s);
                            }
                        }
                        case MAP :{
                            for(Map.Entry<String,String> entry:( (Map<String,String>) msg.d).entrySet()){
                                dataMsg.put(entry.getKey(),entry.getValue());
                            }
                        }
                        case SET :{
                            for(String s: (List<String>) msg.d){
                                dataMsg.put(null,s);
                            }
                        }
                    }

                }
                case IN_PUT :{
                    DataMsg dataMsg=  (DataMsg)get(msg.k,msg.creatDataMsg().toString(), msg.s);
                    switch(msg.t){
                        case LIST :{
                            for(Map.Entry<String,String> entry:( (Map<String,String>) msg.d).entrySet()){
                                dataMsg.put(entry.getKey(),entry.getValue());
                            }
                        }
                        case MAP :{
                            for(Map.Entry<String,String> entry:( (Map<String,String>) msg.d).entrySet()){
                                dataMsg.put(entry.getKey(),entry.getValue());
                            }
                        }
                        case SET :{
                            for(String s: (List<String>) msg.d){
                                dataMsg.put(null,s);
                            }
                        }
                    }
                }
                case IN_GET :{
                    DataMsg dataMsg=  (DataMsg)get(msg.k,msg.creatDataMsg().toString(), msg.s);
                    switch(msg.t){
                        case LIST :{
                            for(String s: (List<String>) msg.d){
                                dataMsg.get(s);
                            }
                        }
                        case MAP :{
                            for(String s: (List<String>) msg.d){
                                dataMsg.get(s);
                            }
                        }
                        case SET :{
                            for(String s: (List<String>) msg.d){
                                dataMsg.get(s);
                            }
                        }
                    }
                }


                case IN_REMOVE :{
                    DataMsg dataMsg=  (DataMsg)get(msg.k,msg.creatDataMsg().toString(), msg.s);
                    switch(msg.t){
                        case LIST :{
                            for(String s: (List<String>) msg.d){
                                dataMsg.remove(s);
                            }
                        }
                        case MAP :{
                            for(String s: (List<String>) msg.d){
                                dataMsg.remove(s);
                            }
                        }
                        case SET :{
                            for(String s: (List<String>) msg.d){
                                dataMsg.remove(s);
                            }
                        }
                    }
                }

                case CP_TO :
                    return null;

                //你可以有任意数量的case语句
                case CP_FROM :
                    return null;

                default : //可选
                    return  null;
            }
        }catch (Exception exception){
            return exception;
        }
    }
    @Override
    public Object get(String key, String type, String scope ) {
        return youngStroe.get( key,  type,  scope);
    }

    @Override
    public Object put(String key, Object val, String type, String scope ) {
        return youngStroe.put( key,val,  type,  scope);
    }

    @Override
    public Object remove(String key, String type, String scope ) {
        return youngStroe.remove( key,  type,  scope);
    }

    @Override
    public Object creat(String key, Object val, String type, String scope) {
        return youngStroe.creat( key, val, type,  scope);
    }

    @Override
    public Object delele(String type, String scope ) {
        return youngStroe.delele(   type,  scope);
    }
}
