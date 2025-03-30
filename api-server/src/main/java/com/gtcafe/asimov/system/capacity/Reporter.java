package com.gtcafe.asimov.system.capacity;

import org.springframework.stereotype.Service;

import com.gtcafe.asimov.system.capacity.domain.ReentrantCapacityUnit;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Reporter extends Thread {
    
	private ReentrantCapacityUnit cu;

    public Reporter(ReentrantCapacityUnit cu) {
        this.cu = cu;
    }

    @Override
    public void run() {
		try {
            log.info("capacity value: {}", cu.getValue()); 
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}
