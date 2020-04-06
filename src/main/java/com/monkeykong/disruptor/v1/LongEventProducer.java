package com.monkeykong.disruptor.v1;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * @author alvin
 * @date 2020-04-06 16:54
 * 消息生产者
 */
public class LongEventProducer {
    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    // 原生发布数据
    public void onData(ByteBuffer buffer){
        long sequence = ringBuffer.next();
        LongEvent longEvent = ringBuffer.get(sequence);
        longEvent.setValue(buffer.getLong(0));
        // 向环上设置数据
        ringBuffer.publish(sequence);
    }

    // 使用 lambda 发布数据(一个参数)
    public void onData2(ByteBuffer buffer) {
        ringBuffer.publishEvent(((event, sequence, arg0) -> event.setValue(arg0.getLong(0))),
                buffer);
    }

    // 使用 lambda 发布数据(两个参数)
    public void onData3(ByteBuffer buffer) {
        ringBuffer.publishEvent(((event, sequence, arg0, arg1) -> event.setValue(arg0 + arg1)),
                buffer.getLong(0), buffer.getLong(0));
    }
}
