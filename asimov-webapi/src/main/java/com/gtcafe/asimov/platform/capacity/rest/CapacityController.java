package com.gtcafe.asimov.platform.capacity.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtcafe.asimov.platform.capacity.CapacityService;
import com.gtcafe.asimov.platform.capacity.domain.ReentrantCapacityUnit;

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
		int unit = (int) ((Math.random() * 100) + 1 % 100);

		service.aquireCapacity(unit);

		return cu.getValue();
	}

	@GetMapping("/value")
	public int getValue() {

		// LabelMarker marker = LabelMarker.of("value", () ->
		// Integer.toString(cu.getValue()));
		// LOG.info(marker, "getValue(), value is {}", cu.getValue());
		log.info("capacity unit is [{}]", cu.getValue());

		return cu.getValue();
	}

	// @GetMapping("/reset")
	// public int reset() {

	// 	cu.reset();

	// 	// LabelMarker marker = LabelMarker.of("value", () ->
	// 	// Integer.toString(cu.getValue()));
	// 	// LOG.info(marker, "reset(), value is {}", cu.getValue());

	// 	return cu.getValue();
	// }


	// @GetMapping("/metric")
	// // public String file(@RequestParam(required = true) String metricName) {
	// public String metric() {

	// 	String metric = String.format("%s,%d,%d", new Date(), (int) (Math.random() * 100),
	// 			(int) (Math.random() * 10000));

	// 	return metric;
	// }

}
