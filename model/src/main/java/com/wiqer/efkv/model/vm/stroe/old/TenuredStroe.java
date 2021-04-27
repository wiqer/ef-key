package com.wiqer.efkv.model.vm.stroe.old;

import com.wiqer.efkv.model.vm.stroe.BaseStroe;
import com.wiqer.efkv.model.vm.stroe.Stroe;

import java.util.List;

public class TenuredStroe implements Stroe {
    private BaseStroe tenured;

    @Override
    public Object get(String key, String type, String scope ) {
        return tenured.get( key,  type,  scope);
    }

    @Override
    public Object put(String key, Object val, String type, String scope ) {
        return tenured.put( key,val,  type,  scope);
    }

    @Override
    public Object remove(String key, String type, String scope ) {
        return tenured.remove( key,  type,  scope);
    }

    @Override
    public Object creat(String key, Object val, String type, String scope) {
        return tenured.creat( key, val, type,  scope);
    }

    @Override
    public Object delele(String type, String scope ) {
        return tenured.delele(   type,  scope);
    }
}
