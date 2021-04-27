package com.wiqer.efkv.model.util.container;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Optional;

public class MappedViewTest extends MappedViewModel{
    public MappedViewTest(String filename) throws FileNotFoundException {
        randomAccessFile = new RandomAccessFile(filename,"rw");
        channel=randomAccessFile.getChannel();
    }
    public static void main(String[] args) throws IOException {

    }
    static  void cpByMap() throws IOException {
        MappedViewTest test8= new MappedViewTest("A:\\test8.efd");
        MappedViewTest test= new MappedViewTest("A:\\test7.efd");
        long len=test.channel.size();
        long startTime = System.currentTimeMillis();    //获取开始时间
        MappedByteBuffer mappedByteBuffer8 = test8.channel.map(FileChannel.MapMode.READ_WRITE, 0, len);
        MappedByteBuffer mappedByteBuffer = test.channel.map(FileChannel.MapMode.READ_ONLY, 0, len);
        for (int i = 0; i < len; i++) {
            mappedByteBuffer8.put(i,mappedByteBuffer.get(i));
        }
        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("downDiskOneloop程序运行时间：" + (endTime - startTime));
    }
}
