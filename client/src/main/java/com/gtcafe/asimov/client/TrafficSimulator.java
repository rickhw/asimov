package com.gtcafe.asimov.client;

import java.time.LocalTime;

import org.springframework.stereotype.Component;

@Component
public class TrafficSimulator {

    // Define traffic multipliers for each hour (0-23)
    // These values are relative and can be tuned to match the desired pattern.
    private static final double[] HOURLY_MULTIPLIERS = {
        0.9, // 00:00 - 01:00 (late night high)
        0.2, // 01:00 - 02:00 (sharp drop)
        0.1, // 02:00 - 03:00 (low)
        0.1, // 03:00 - 04:00 (low)
        0.1, // 04:00 - 05:00 (low)
        0.1, // 05:00 - 06:00 (low)
        0.1, // 06:00 - 07:00 (low)
        0.2, // 07:00 - 08:00 (slight rise)
        0.4, // 08:00 - 09:00 (gradual rise)
        0.6, // 09:00 - 10:00 (gradual rise)
        0.7, // 10:00 - 11:00 (gradual rise)
        0.8, // 11:00 - 12:00 (lunch peak)
        0.8, // 12:00 - 13:00 (lunch peak)
        0.6, // 13:00 - 14:00 (slight drop)
        0.4, // 14:00 - 15:00 (afternoon dip)
        0.3, // 15:00 - 16:00 (afternoon dip)
        0.3, // 16:00 - 17:00 (afternoon dip)
        0.5, // 17:00 - 18:00 (rise towards evening)
        0.7, // 18:00 - 19:00 (evening peak)
        0.8, // 19:00 - 20:00 (evening peak)
        0.9, // 20:00 - 21:00 (evening peak)
        0.9, // 21:00 - 22:00 (evening peak)
        1.0, // 22:00 - 23:00 (late evening peak)
        1.0  // 23:00 - 00:00 (late evening peak)
    };

    public double getCurrentTrafficMultiplier() {
        int currentHour = LocalTime.now().getHour();
        return HOURLY_MULTIPLIERS[currentHour];
    }
}
