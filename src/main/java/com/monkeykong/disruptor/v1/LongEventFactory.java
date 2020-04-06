package com.monkeykong.disruptor.v1;

import com.lmax.disruptor.EventFactory;

/**
 * @author alvin
 * @date 2020-04-06 16:49
 * 消息工厂
 *
 */
public class LongEventFactory implements EventFactory<LongEvent> {
    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
