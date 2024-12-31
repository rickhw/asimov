package com.gtcafe.asimov.platform.capacity.domain;

import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;

@Service
public class ReentrantCapacityUnit implements ICapacityUnit {

    private int capacityUnit = DEFAULT_CAPACITY_UNIT;
    private ReentrantLock locker = new ReentrantLock();

    public int getValue() {
        return capacityUnit;
    }

    public void reset() {
        capacityUnit = DEFAULT_CAPACITY_UNIT;
    }

    public void consume(int value) throws CapacityInsufficient {
        locker.lock();

        if (value > capacityUnit) {
            locker.unlock();
            throw new CapacityInsufficient("Not enough capacity unit");
        }

        try {
            capacityUnit -= value;
        } catch (Exception ex) {
        } finally {
            locker.unlock();
        }
    }

    public void resume(int value) {
        locker.lock();

        try {
            capacityUnit += value;
        } catch (Exception ex) {
        } finally {
            locker.unlock();
        }
    }
}
