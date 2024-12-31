package com.gtcafe.asimov.platform.capacity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.platform.capacity.domain.CapacityInsufficient;
import com.gtcafe.asimov.platform.capacity.domain.ReentrantCapacityUnit;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CapacityService {

	@Autowired
	private ReentrantCapacityUnit cu;

	public void aquireCapacity(int unit) {
		log.info("aquire: [{}], remain: [{}]", unit, cu.getValue());

		try {
			cu.consume(unit);
		} catch (CapacityInsufficient e) {
			log.error("capacity insuffient: {}", e.getMessage());
			return;
		}
		
		// async
		Handler handler = new Handler(cu, unit);
		handler.start();
		
	}
}
