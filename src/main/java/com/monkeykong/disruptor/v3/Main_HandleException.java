package com.monkeykong.disruptor.v3;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
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
public class Main_HandleException {

    public static void main(String[] args) throws InterruptedException {
        // 队列大小，必须是 2 的整数次幂
        int ringBufferSize = 1024;

        // 获取 disruptor
        // ProducerType.SINGLE：对 sequence 的操作不加锁，效率更高
        // ProducerType.MUTI：对 sequence 的操作要加锁
        Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, ringBufferSize,
                DaemonThreadFactory.INSTANCE, ProducerType.MULTI, new BlockingWaitStrategy());

        // 设置消费者
        EventHandler h = (event, sequence, endOfBatch) -> {
            System.out.println(event);
            throw new Exception("消费者出现异常");
        };
        disruptor.handleEventsWith(h);

        // 设置消费者异常处理
        disruptor.handleExceptionsFor(h).with(new ExceptionHandler() {
            @Override
            public void handleEventException(Throwable ex, long sequence, Object event) {
                ex.printStackTrace();
            }

            @Override
            public void handleOnStartException(Throwable ex) {
                System.out.println("启动时出现异常");
            }

            @Override
            public void handleOnShutdownException(Throwable ex) {
                System.out.println("关闭时出现异常");
            }
        });

        // 启动 disruptor
        disruptor.start();

        // 生产数据
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);

        for (long j = 0; j < 500; j++) {
            byteBuffer.putLong(0, j);
            ringBuffer.publishEvent(Main::translateTo, byteBuffer);
        }

        // 关闭 disruptor
        disruptor.shutdown();
    }
}
