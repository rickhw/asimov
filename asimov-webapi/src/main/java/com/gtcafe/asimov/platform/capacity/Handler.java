package com.gtcafe.asimov.platform.capacity;

import com.gtcafe.asimov.platform.capacity.domain.ReentrantCapacityUnit;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public class Handler extends Thread {

    private ReentrantCapacityUnit cu;
    private int unit;
    private MeterRegistry meterRegistry;

    public Handler(ReentrantCapacityUnit cu, int unit, MeterRegistry meterRegistry) {
        this.cu = cu;
        this.unit = unit;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void run() {
        try {
            long processTime = (long) (Math.random() * 10000);
            log.info("process time: [{}ms]", processTime);
            Thread.sleep(processTime);

            cu.resume(unit);

            // 更新 metrics: 剩餘的 capacity
            meterRegistry.gauge("capacity.remaining", cu, ReentrantCapacityUnit::getValue);
            // meterRegistry.gauge("capacity.consumed", cu, unit);

            log.info("resume: [{}], remain: [{}], processTime: [{}ms]", unit, cu.getValue(), processTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
