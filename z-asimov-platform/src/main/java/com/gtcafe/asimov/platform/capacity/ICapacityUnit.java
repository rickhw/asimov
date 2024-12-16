package com.gtcafe.asimov.platform.capacity;

public interface ICapacityUnit {

    public int getValue();

    public void reset();

    public void operate(int value);

    public void increase(int value);

    public void decrease(int value);
}
