package com.gtcafe.asimov.platform.capacity;

import com.gtcafe.asimov.platform.capacity.domain.ReentrantCapacityUnit;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public class Handler extends Thread {
    
	private ReentrantCapacityUnit cu;
    private int unit;

    public Handler(ReentrantCapacityUnit cu, int unit) {
        this.cu = cu;
        this.unit = unit;
    }

    @Override
    public void run() {
		try {
            long processTime = (long) (Math.random() * 3000);
            log.info("process time: [{}ms]", processTime);
			Thread.sleep(processTime);

            cu.resume(unit);
            log.info("resume: [{}], remain: [{}]", unit, cu.getValue());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}
