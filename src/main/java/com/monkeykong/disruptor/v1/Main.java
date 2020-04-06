package com.monkeykong.disruptor.v1;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;

/**
 * @author alvin
 * @date 2020-04-06 16:59
 */
public class Main {
    public static void main(String[] args) {
        // 事件工厂
        LongEventFactory longEventFactory = new LongEventFactory();

        // 队列大小，必须是 2 的整数次幂
        int ringBufferSize = 1024;

        // 获取 disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<>(longEventFactory, ringBufferSize, Executors.defaultThreadFactory());

        // 设置消费者
        disruptor.handleEventsWith(new LongEventHandler());

        // 启动 disruptor
        disruptor.start();

        // 构造生产者，并生产数据
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        LongEventProducer producer = new LongEventProducer(ringBuffer);
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        for (long i = 0; i < 100; i++) {
            byteBuffer.putLong(0, i);
//            producer.onData(byteBuffer);
//            producer.onData2(byteBuffer);
            producer.onData3(byteBuffer);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 关闭 disruptor
        disruptor.shutdown();
    }
}
