package com.gtcafe.asimov.platform.capacity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.platform.capacity.domain.CapacityInsufficient;
import com.gtcafe.asimov.platform.capacity.domain.ReentrantCapacityUnit;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CapacityService {

    @Autowired
    private ReentrantCapacityUnit cu;

    @Autowired
    private MeterRegistry meterRegistry;

    public void aquireCapacity(int unit) {
        log.info("aquire: [{}], remain: [{}]", unit, cu.getValue());

        try {
            cu.consume(unit);

            // 更新 metrics: consumed 和剩餘的 capacity
            meterRegistry.counter("capacity.consumed").increment(unit);
            meterRegistry.gauge("capacity.remaining", cu, ReentrantCapacityUnit::getValue);
            // meterRegistry.gauge("capacity.consumed", cu, ReentrantCapacityUnit::getValue);
        } catch (CapacityInsufficient e) {
            log.error("capacity insufficient: {}", e.getMessage());
            return;
        }

        // async
        Handler handler = new Handler(cu, unit, meterRegistry);
        handler.start();
    }
}
