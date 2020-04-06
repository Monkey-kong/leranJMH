package com.monkeykong.jmh;

import org.openjdk.jmh.annotations.*;

/**
 * @author alvin
 * @date 2020-04-06 15:23
 */
public class PSTest {
    // 测试
    @Benchmark
    // 预热
    @Warmup(iterations = 1, time = 3)
    // 线程数
    @Fork(5)
    // 测试模式
    @BenchmarkMode(Mode.Throughput)
    // 总共执行多少次测试
    @Measurement(iterations = 1, time = 3)
    public void testForEach(){
        // PS.foreach();
        PS.parallel();
    }
}
