package com.wiqer.efkv.model.rule.id;

import com.wiqer.efkv.model.type.MsgType;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FEIDTest {
    static int c=0;
    static Set<String> s=new HashSet<>();//CopyOnWriteArray
    public static void main(String[] args) {
        System.out.println(MsgType.EMPTY.toString());

        EFBETIDT();
//        long endTime = System.currentTimeMillis();    //获取结束时间
//        System.out.println("EFID程序运行时间：" + (endTime - startTime)+",c="+c);    //输出程序运行时间

    }
    static void EFBETIDT(){
        EFNETID.randomID().toStringMore();
        long startTime = System.currentTimeMillis();    //获取开始时间
        for (int k=0; k<100000000;k++)
//            if (!s.add(EFNETID.toStringEasyFast())) {////UUID.randomUUID().toString();
//                //System.out.println("失败");
//                c++;
//            }
            EFNETID.toStringEasyFast4Bit();//100000000:toStringMore->33306;randomUUID->66590;toStringFast->13654;toStringEasyFast4Bit->6754;toStringEasyFast8Bit->15590
        //System.out.println(EFNETID.randomID().toStringEasyFast());
        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("EFID程序运行时间：" + (endTime - startTime)+",c="+c);    //输出程序运行时间
    }
    static void EFIDT(){

        long startTime = System.currentTimeMillis();    //获取开始时间


        EventExecutorGroup multipleGroup = new DefaultEventExecutorGroup(4);//业务线程池

        for (int k=0; k<1;k++) {

            multipleGroup.execute(() -> {

                for (int i = 0; i < 1000000; i++) {
                    if (!s.add(UUID.randomUUID().toString())) {
                        System.out.println("失败");
                        c++;
                    }

                }
                long endTime = System.currentTimeMillis();    //获取结束时间
                System.out.println("EFID程序运行时间：" + (endTime - startTime)+",c="+c);    //输出程序运行时间


                //System.out.println(EFID.randomEFID().toString());
                // EFID.randomEFID().toString();
                //UUID.randomUUID().toString();
                // byte[] randomBytes = new byte[8];
                // System.out.println( EFID.randomEFID().toString());

            });
        }
    }
}
