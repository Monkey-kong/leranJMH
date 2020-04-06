package com.monkeykong.disruptor.v3;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.monkeykong.disruptor.v1.LongEvent;
import com.monkeykong.disruptor.v2.Main;

import java.nio.ByteBuffer;

/**
 * @author alvin
 * @date 2020-04-06 19:46
 */
public class Main_ProducerType {

    public static void main(String[] args) throws InterruptedException {
        // 队列大小，必须是 2 的整数次幂
        int ringBufferSize = 1024;

        // 获取 disruptor
        // ProducerType.SINGLE：对 sequence 的操作不加锁，效率更高
        // ProducerType.MUTI：对 sequence 的操作要加锁
        Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, ringBufferSize,
                DaemonThreadFactory.INSTANCE, ProducerType.MULTI, new BlockingWaitStrategy());

        // 设置消费者
        disruptor.handleEventsWith(new LongEventHandler());

        // 启动 disruptor
        disruptor.start();

        // 生产数据
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (long j = 0; j < 500; j++) {
                    byteBuffer.putLong(0, j);
                    ringBuffer.publishEvent(Main::translateTo, byteBuffer);
                }
            }).start();
        }

        Thread.sleep(5000);
        System.out.println(LongEventHandler.count);

        // 关闭 disruptor
        // disruptor.shutdown();
    }
}
