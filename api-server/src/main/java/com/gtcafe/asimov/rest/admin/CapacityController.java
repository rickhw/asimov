package com.gtcafe.asimov.rest.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtcafe.asimov.system.capacity.CapacityService;
import com.gtcafe.asimov.system.capacity.domain.ReentrantCapacityUnit;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1alpha/capacity")
@Slf4j
@Tag(name = "Platform/Capacity", description = "Capacity API")
public class CapacityController {

	@Autowired
	private CapacityService service;

	@Autowired
	private ReentrantCapacityUnit cu;

	@GetMapping("/consume")
	public int operate() throws Exception {
		// random unit value, between 1 and 100
		int unit = (int) ((Math.random() * 1000) + 1) % 10;

		service.aquireCapacity(unit);

		return cu.getValue();
	}

	@GetMapping("/value")
	public int getValue() {
		log.info("capacity unit is [{}", cu.getValue());

		return cu.getValue();
	}
}
