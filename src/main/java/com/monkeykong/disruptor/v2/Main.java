package com.monkeykong.disruptor.v2;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.monkeykong.disruptor.v1.LongEvent;

import java.nio.ByteBuffer;

/**
 * @author alvin
 * @date 2020-04-06 19:34
 */
public class Main {
    // 消费者
    public static void handleEvvent(LongEvent event, long sequence, boolean endOfBatch) {
        System.out.println("event:"+event.getValue()+",sequence:" + sequence);
    }

    // 翻译
    public static void translateTo(LongEvent event, long sequence, ByteBuffer buffer){
        event.setValue(buffer.getLong(0));
    }

    public static void main(String[] args) {
        // 队列大小，必须是 2 的整数次幂
        int ringBufferSize = 1024;

        // 获取 disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, ringBufferSize, DaemonThreadFactory.INSTANCE);

        // 设置消费者
        disruptor.handleEventsWith(Main::handleEvvent);

        // 启动 disruptor
        disruptor.start();

        // 生产数据
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);

        for (long i = 0; i < 100; i++) {
            byteBuffer.putLong(0, i);
            ringBuffer.publishEvent(Main::translateTo, byteBuffer);
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
