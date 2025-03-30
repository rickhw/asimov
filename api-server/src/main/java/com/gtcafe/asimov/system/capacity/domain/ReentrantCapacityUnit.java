package com.gtcafe.asimov.system.capacity.domain;

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
            locker.unlock();    // free the lock before throwing exception, to avoid deadlock
            throw new CapacityInsufficient("capacity unit is insufficient: required=" + value + ", current=" + capacityUnit);
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
