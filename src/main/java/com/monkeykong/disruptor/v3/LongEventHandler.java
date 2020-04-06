package com.monkeykong.disruptor.v3;

import com.lmax.disruptor.EventHandler;
import com.monkeykong.disruptor.v1.LongEvent;

/**
 * @author alvin
 * @date 2020-04-06 16:51
 * 消息消费者
 */
public class LongEventHandler implements EventHandler<LongEvent> {

    public static volatile int count;

    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        count++;
        System.out.println(Thread.currentThread().getName() + "==>event:"+event.getValue()+",sequence:" + sequence);
    }
}
