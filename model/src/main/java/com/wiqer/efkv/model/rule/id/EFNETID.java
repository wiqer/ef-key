
package com.wiqer.efkv.model.rule.id;

import com.wiqer.efkv.model.tool.IpUtils;
import com.wiqer.efkv.model.util.format.EasyFastFormat;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.LongAdder;
/*
* 性能测试
* //100000000:一亿次插入，ms
* toStringMore->33306;
* randomUUID->66590;
* toStringFast->13654;
* toStringEasyFast4Bit->6754;
* toStringEasyFast8Bit->15590
*100年有30亿亿毫秒，1毫秒生成一万个id
* */
public final class EFNETID {
    volatile int mostSigBits;
    volatile int  leastSigBits;
    volatile static int efIdCount=159651;//随便打的
    static int efIdCountNoVolatile=159651;//随便打的
    static LongAdder adder =new LongAdder();
    static String  process;
    static String  ip;
    static String  finalLocal;

    volatile static byte[] randomBytes;
    static {
        EFNETID.numberGenerator=  new SecureRandom();
        EFNETID.randomBytes=new byte[8];
        EFNETID.numberGenerator.nextBytes(  EFID.randomBytes);
        process = EasyFastFormat.digits(getProcessID()&0Xff,2);
        ip=EasyFastFormat.digits(IpUtils.getIntIp(),4);

        finalLocal=process+ip;
    }
    static SecureRandom numberGenerator;

    //EFNETID的碰撞概率为万分之一，乘以上count，概率为千亿分之一，
    // 乘以ip+进程id+线程id，概率为千亿亿分之一
    //加端口号的话，概率为百亿亿亿亿分之一
    private EFNETID(byte[] data) {
        int msb = this.mostSigBits;
        int lsb = this.leastSigBits;
        EFNETID.efIdCount++;
        for (int i=0; i<4; i++)
            msb = (msb << 4) | (data[i] & 0xff);
        for (int i=4; i<8; i++)
            lsb = (lsb << 4) | (data[i] & 0xff);
        this.mostSigBits = msb;//+ count>>4;
        this.leastSigBits = lsb;//+ count<<4;
    }
    @Override
    public String toString() {
        int count =EFNETID.efIdCount;
        return (
                finalLocal +
                        EasyFastFormat.digits((int)Thread.currentThread().getId()&0xff, 2)//+"-"
                +
                        EasyFastFormat.digits(mostSigBits, 4) +// "-" +
                        EasyFastFormat.digits(leastSigBits, 4)
                +
                        EasyFastFormat.digits(count, 4)
        );
    }
    public String toStringMore() {
        int count =EFNETID.efIdCount;

        return (
                finalLocal +
                        EasyFastFormat.digits((int)Thread.currentThread().getId()&0xff, 2)//+"-"
                +
                        EasyFastFormat.digits(mostSigBits, 4) +// "-" +
                        EasyFastFormat.digits(leastSigBits, 4)
                +
                        EasyFastFormat.digits(count, 4) +
                        EasyFastFormat.digits(ThreadLocalRandom.current().nextInt(), 4)
        );
    }
    public static String toStringFast() {
        int count =++EFNETID.efIdCountNoVolatile;
        return (
            finalLocal +
                    EasyFastFormat.digits(ThreadLocalRandom.current().nextInt(), 4)+
                    EasyFastFormat.digits(ThreadLocalRandom.current().nextInt(), 4)+
                    EasyFastFormat.digits(count, 4)+
                    EasyFastFormat.digits(ThreadLocalRandom.current().nextInt(), 4)
        );
    }
    public static String toStringEasyFast8Bit() {
        adder.increment();
        return (
                finalLocal +
                        EasyFastFormat.digits(ThreadLocalRandom.current().nextLong(), 8)+
                        EasyFastFormat.digits(adder.longValue(), 8)
        );
    }
    //临时使用
    public static String toStringEasyFast4Bit() {
        adder.increment();
        return (
                finalLocal +
                        EasyFastFormat.digits(ThreadLocalRandom.current().nextInt(), 4)+
                        EasyFastFormat.digits(adder.intValue(), 4)
        );
    }
    /** Returns val represented by the specified number of hex digits. */

    public static EFNETID randomID() {
        SecureRandom ng = numberGenerator;

        byte[] randomBytes = EFNETID.randomBytes;
        //此处会加快速度，快3倍
        ng.nextBytes(randomBytes);
        return new EFNETID(randomBytes);
    }
    public static final int getProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        //System.out.println(runtimeMXBean.getName());
        //Runtime.getRuntime().availableProcessors()
        return Integer.valueOf(runtimeMXBean.getName().split("@")[0])
                .intValue();
    }


}
