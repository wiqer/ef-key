package com.wiqer.efkv.model.tool;


import cn.hutool.core.util.ZipUtil;
import com.wiqer.efkv.model.util.container.MappedView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * 对象序列化，反序列化（序列化对象转byte[],ByteBuffer, byte[]转object
 *
 * @author Vicky
 * @email eclipser@163.com
 */
public class ByteUtil {

    public static byte[] getBytes(Serializable obj) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(obj);
        out.flush();
        byte[] bytes = bout.toByteArray();
        bout.close();
        out.close();
        return bytes;
    }

    public static int sizeof(Serializable obj) throws IOException {
        return getBytes(obj).length;
    }

    public static Object getObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
        ObjectInputStream oi = new ObjectInputStream(bi);
        Object obj = oi.readObject();
        bi.close();
        oi.close();
        return obj;
    }

    public static Object getObject(ByteBuffer byteBuffer) throws ClassNotFoundException, IOException {
        InputStream input = new ByteArrayInputStream(byteBuffer.array());
        ObjectInputStream oi = new ObjectInputStream(input);
        Object obj = oi.readObject();
        input.close();
        oi.close();
        byteBuffer.clear();
        return obj;
    }

    public static ByteBuffer getByteBuffer(Serializable obj) throws IOException {
        byte[] bytes = ByteUtil.getBytes(obj);
        ByteBuffer buff = ByteBuffer.wrap(bytes);
        return buff;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("对象长度-------");
        System.out.println(ByteUtil.sizeof(new Player1()));
        System.out.println(ByteUtil.sizeof(new Player2()));
        System.out.println(ByteUtil.sizeof(new Player3()));
        System.out.println(ByteUtil.sizeof(new Player4()));
        System.out.println(ByteUtil.sizeof(new Player5()));

        System.out.println("---------");

        Player5 p5 = new Player5();
        System.out.println(ByteUtil.sizeof(p5));
        p5.id1 = 100000;
        p5.id2 = 200000;
        System.out.println(ByteUtil.sizeof(p5));
        p5.name = "ooxx";
        System.out.println(ByteUtil.sizeof(p5));
        p5.name = "ooxxooxx";
        System.out.println(ByteUtil.sizeof(p5));

        System.out.println("---------");
        byte[] bytes = ByteUtil.getBytes(p5);
        Player5 p5_2 = (Player5) ByteUtil.getObject(bytes);
        System.out.println(p5_2.id1);
        System.out.println(p5_2.id2);
        System.out.println(p5_2.name);

        System.out.println("---------");
        System.out.println(ByteUtil.sizeof(new Player6()));
        Player6 p6 = new Player6();
        System.out.println(ByteUtil.sizeof(p6));
        p6.id1 = 100000;
        p6.id2 = 200000;
        System.out.println(ByteUtil.sizeof(p6));
        p6.setName("Vicky");
        System.out.println(ByteUtil.sizeof(p6));
        p6.setName("中文名称");
        System.out.println(ByteUtil.sizeof(p6));

        bytes = ByteUtil.getBytes(p6);
        Player6 p6_2 = (Player6) ByteUtil.getObject(bytes);
        System.out.println(p6_2.id1);
        System.out.println(p6_2.id2);
        System.out.println(p6_2.getName());
        System.out.println("---------");
        Player7 p7= new Player7();
        p7.setName("小李");
        p7.id1 = 300000;
        p7.id2 = 400000;
        ByteBuffer byteBuffer=ByteUtil.getByteBuffer(p7);
        Player7 p7_1 =(Player7)ByteUtil.getObject(byteBuffer);
        System.out.println(p7_1.id1);
        System.out.println(p7_1.id2);
        System.out.println(p7_1.getName());
        long start = System.currentTimeMillis();
        MappedView mappedView= new MappedView("A:\\map.efdata");

        int wtf=mappedView.put(ZipUtil.gzip(bytes) );
        byte[] wtfs= mappedView.get(wtf);
        int gzipLen=wtfs.length;
        System.out.println("gzipLen="+gzipLen);
        Player6 p6_3=null;
        if(wtfs!=null){
             p6_3 = (Player6) ByteUtil.getObject(ZipUtil.unGzip(wtfs));
            System.out.println(p6_3.id1);
            System.out.println(p6_3.id2);
            System.out.println(p6_3.getName());
            System.out.println("---------");
        }
        //MappedViewUtils.mappedFile((Paths.get("A:\\磁盘备份\\F盘\\20210317\\面试资料.rar")));
        long end = System.currentTimeMillis();
        System.out.println(end-start+"ms");

        byte[] bytes1= FastJsonUtils.convertObjectToJSONBytes(p6_3);
        int jsonLen=bytes1.length;
        System.out.println("jsonLen="+jsonLen);
        start = System.currentTimeMillis();
        int wtf2=mappedView.put( bytes1);
        byte[] wtfs2= mappedView.get(wtf2);
        Player6 p6_4=null;
        p6_4= FastJsonUtils.toBean(wtfs2,Player6.class);
        System.out.println(p6_4.getName());
        System.out.println("---------");
        System.out.println(start-end+"ms");
    }
}

class Player1 implements Serializable {
    int id1;
}

class Player2 extends Player1 {
    int id2;
}

class Player3 implements Serializable {
    int id1;
    int id2;
}

class Player4 extends Player3 {
    String name;
}

class Player5 implements Serializable {
    int id1;
    int id2;
    String name;
}

class Player6 implements Serializable {
    final static Charset chrarSet = Charset.forName("UTF-8");

    int id1;
    int id2;
    private byte[] name = new byte[20];

    public String getName() {
        return new String(name, chrarSet);
    }

    public void setName(String name) {
        this.name = name.getBytes(chrarSet);
    }
}
class Player7 implements Serializable {
    final static Charset chrarSet = Charset.forName("UTF-8");
    private static final long serialVersionUID = -15214;////xxxx看自己喜欢
    int id1;
    int id2;
    private byte[] name = new byte[20];

    public String getName() {
        return new String(name, chrarSet);
    }

    public void setName(String name) {
        this.name = name.getBytes(chrarSet);
    }
}
