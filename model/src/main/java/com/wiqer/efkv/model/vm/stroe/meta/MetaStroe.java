package com.wiqer.efkv.model.vm.stroe.meta;

import com.wiqer.efkv.model.vm.stroe.BaseStroe;
import com.wiqer.efkv.model.vm.stroe.Stroe;

import java.util.List;

public class MetaStroe implements Stroe {
    private BaseStroe meta;

    @Override
    public Object get(String key, String type, String scope ) {
        return meta.get( key,  type,  scope);
    }

    @Override
    public Object put(String key, Object val, String type, String scope ) {
        return meta.put( key,val,  type,  scope);
    }

    @Override
    public Object remove(String key, String type, String scope ) {
        return meta.remove( key,  type,  scope);
    }

    @Override
    public Object creat(String key, Object val, String type, String scope) {
        return meta.creat( key, val, type,  scope);
    }

    @Override
    public Object delele(String type, String scope ) {
        return meta.delele(   type,  scope);
    }
}
