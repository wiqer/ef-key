package com.wiqer.efkv.model.vm.stroe;

import java.util.List;

public class RouteStroe implements Stroe{
    BaseStroe routeStroe;
    public RouteStroe(){
        routeStroe =new BaseStroe();
    }
    @Override
    public Object get(String key, String type, String scope ) {
        return routeStroe.get( key,  type,  scope);
    }

    @Override
    public Object put(String key, Object val, String type, String scope ) {
        return routeStroe.put( key,val,  type,  scope);
    }

    @Override
    public Object remove(String key, String type, String scope ) {
        return routeStroe.remove( key,  type,  scope);
    }

    @Override
    public Object creat(String key, Object val, String type, String scope) {
        return routeStroe.creat( key, val, type,  scope);
    }

    @Override
    public Object delele(String type, String scope ) {
        return routeStroe.delele(   type,  scope);
    }
}
