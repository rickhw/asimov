package com.gtcafe.asimov.platform.capacity.domain;

import org.springframework.stereotype.Service;

@Service
public class NormalCapacityUnit implements ICapacityUnit {

    private int capacityUnit = DEFAULT_CAPACITY_UNIT;

    // public NormalCapacityUnit() {
    // System.out.printf("Init counter, value: [%s]\n", this.getValue());
    // }

    public int getValue() {
        return capacityUnit;
    }

    public void reset() {
        capacityUnit = DEFAULT_CAPACITY_UNIT;
    }

    public void consume(int value) {
        capacityUnit -= value;
    }

    public void resume(int value) {
        capacityUnit += value;
    }
    // public void operate(int value) {
    //     capacityUnit += value;
    // }

    // public void increase(int value) {
    //     capacityUnit = capacityUnit + value;
    // }

    // public void decrease(int value) {
    //     capacityUnit = capacityUnit - value;
    // }

}
