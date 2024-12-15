package com.gtcafe.asimov.platform.capacity;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.loki4j.slf4j.marker.LabelMarker;

@RestController
@RequestMapping("/api/capacity")
public class CapacityController {

	// private final Logger LOG = LoggerFactory.getLogger(CapacityController.class);

	@Autowired
	private ICapacityUnit cu;

	@GetMapping("/increase")
	public int operate(@RequestParam(required = true) Integer value) throws GeneralSecurityException, IOException {

		cu.operate(value);

		// MDC.put("consumedValue", Integer.toString(value));
		// MDC.put("capacityUnit", Integer.toString(cu.getValue()));
		// MDC.put("value", Integer.toString(cu.getValue()));

		LabelMarker marker1 = LabelMarker.of("consumedValue", () -> Integer.toString(value));
		LOG.info(marker1, "operate(), consumedValue: {}", value);

		LabelMarker marker2 = LabelMarker.of("capacityUnit", () -> Integer.toString(cu.getValue()));
		LOG.info(marker2, "operate(), totalCapcity: {}", cu.getValue());

		// LOG.info("operate(), totalCapcity: {}, consumedValue: {}", cu.getValue(), value);

		return cu.getValue();
	}

	@GetMapping("/value")
	public int getValue() {

		// LabelMarker marker = LabelMarker.of("value", () ->
		// Integer.toString(cu.getValue()));
		// LOG.info(marker, "getValue(), value is {}", cu.getValue());

		return cu.getValue();
	}

	@GetMapping("/reset")
	public int reset() {

		cu.reset();

		// LabelMarker marker = LabelMarker.of("value", () ->
		// Integer.toString(cu.getValue()));
		// LOG.info(marker, "reset(), value is {}", cu.getValue());

		return cu.getValue();
	}

	// @GetMapping("/file")
	// public int file(@RequestParam(required = true) Integer value) {

	// 	System.out.println(new Date());
	// 	try {
	// 		Thread.sleep(value);
	// 	} catch (Exception e) {
	// 	}

	// 	System.out.println(new Date());

	// 	return cu.getValue();
	// }

	@GetMapping("/metric")
	// public String file(@RequestParam(required = true) String metricName) {
	public String metric() {

		String metric = String.format("%s,%d,%d", new Date(), (int) (Math.random() * 100),
				(int) (Math.random() * 10000));

		return metric;
	}

}
