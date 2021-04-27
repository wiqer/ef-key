package com.wiqer.efkv.model.vm.stroe.young;

import com.wiqer.efkv.model.vm.stroe.BaseStroe;
import com.wiqer.efkv.model.vm.stroe.Stroe;

import java.util.List;

public class SurvivorStroe implements Stroe {
    private BaseStroe from;
    private BaseStroe to;

    @Override
    public Object get(String key, String type, String scope ) {
        return from.get( key,  type,  scope);
    }

    @Override
    public Object put(String key, Object val, String type, String scope ) {
        return from.put( key,val,  type,  scope);
    }

    @Override
    public Object remove(String key, String type, String scope ) {
        return from.remove( key,  type,  scope);
    }

    @Override
    public Object creat(String key, Object val, String type, String scope) {
        return from.creat( key, val, type,  scope);
    }

    @Override
    public Object delele(String type, String scope ) {
        return from.delele(   type,  scope);
    }
}
