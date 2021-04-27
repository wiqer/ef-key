package com.wiqer.efkv.model.rule.route;

import com.wiqer.efkv.model.rule.hash.Murmurs;

import java.util.HashSet;

public class RegisterRoute {
    public static void main(String[] args) {
        String s="1";
        HashSet<Integer> set =new HashSet<>();
        for(int i=2;i<52;i++){
            set.add(s.hashCode()%i);
            System.out.println(s.hashCode()%i);
        }
        for(int i=2;i<52;i++){
            set.add(Murmurs.hashUint32(s)%i);
            System.out.println(Murmurs.hashUint32(s)%i);
        }
        System.out.println("set.size()="+set.size());
    }
}
