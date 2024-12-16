package com.gtcafe.asimov.platform.capacity.impl;

import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.css.Counter;

import com.gtcafe.asimov.platform.capacity.ICapacityUnit;

// import io.micrometer.core.instrument.MeterRegistry;

@Service
public class ReentrantCapacityUnit implements ICapacityUnit {

    private int capacityUnit = 0;
    private ReentrantLock locker = new ReentrantLock();

    // private final Counter capacityConsumed;
	// private final Counter capacityRemaining;

    // @Autowired
    // private MeterRegistry registry;

	// public ReentrantCapacityUnit(MeterRegistry registry) {

	// 	this.capacityRemaining = Counter.builder("capacity.remaining")
	// 			.description("Total number of tasks completed")
	// 			.register(registry);

	// 	this.capacityConsumed = Counter.builder("capacity.consumed")
	// 			.description("Total number of tasks creation")
	// 			.register(registry);
	// }

    public int getValue() {
        return capacityUnit;
    }

    public void reset() {
        capacityUnit = 0;
    }

    public void operate(int value) {
        locker.lock();

        try {
            capacityUnit += value;
        } catch (Exception ex) {
        } finally {
            locker.unlock();
        }
    }

    public void increase(int value) {
        locker.lock();

        try {
            capacityUnit = capacityUnit + value;
        } catch (Exception ex) {
        } finally {
            locker.unlock();
        }
    }

    public void decrease(int value) {
        locker.lock();

        try {
            capacityUnit = capacityUnit - value;
        } catch (Exception ex) {
        } finally {
            locker.unlock();
        }
    }
}
