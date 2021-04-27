package com.wiqer.efkv.model.vm.stroe.young;

import com.wiqer.efkv.model.vm.stroe.BaseStroe;
import com.wiqer.efkv.model.vm.stroe.Stroe;

import java.util.List;

public class YoungStroe implements Stroe {
    BaseStroe eden;
    SurvivorStroe survivor;
    public YoungStroe(){
        eden=new BaseStroe();
        survivor=new SurvivorStroe();
    }
    @Override
    public Object get(String key, String type, String scope ) {
        return eden.get( key,  type,  scope);
    }

    @Override
    public Object put(String key, Object val, String type, String scope ) {
        return eden.put( key,val,  type,  scope);
    }

    @Override
    public Object remove(String key, String type, String scope ) {
        return eden.remove( key,  type,  scope);
    }

    @Override
    public Object creat(String key, Object val, String type, String scope) {
        return eden.creat( key, val, type,  scope);
    }

    @Override
    public Object delele(String type, String scope ) {
        return eden.delele(   type,  scope);
    }
}
