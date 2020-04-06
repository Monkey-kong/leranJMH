package com.monkeykong.disruptor.v1;

import com.lmax.disruptor.EventHandler;

/**
 * @author alvin
 * @date 2020-04-06 16:51
 * 消息消费者
 */
public class LongEventHandler implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("event:"+event.getValue()+",sequence:" + sequence);
    }
}
