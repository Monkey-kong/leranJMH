package com.monkeykong.disruptor.v1;

/**
 * @author alvin
 * @date 2020-04-06 16:47
 * 消息对象
 */
public class LongEvent {
    private long value;
    public void setValue(long value) {
        this.value = value;
    }
    public long getValue() {
        return value;
    }
    @Override
    public String toString() {
        return "LongEvent[" + value + "]";
    }
}
