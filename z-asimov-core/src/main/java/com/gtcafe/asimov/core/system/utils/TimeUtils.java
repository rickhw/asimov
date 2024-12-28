package com.gtcafe.asimov.core.system.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtcafe.asimov.core.system.bean.singleton.TimeConfig;

@Service
public class TimeUtils {
    
    @Autowired
    private TimeConfig config;

    public String currentTimeIso8601() {
        return currentTimeIso8601(new Date());
    }

    public String currentTimeIso8601(Date date) {

        return TimeFormat(date, config.getZone(), config.getFormat());
    }

    public static String TimeFormat(Date date, String timeZone, String timeFormat) {
        TimeZone tz = TimeZone.getTimeZone(timeZone);
        DateFormat df = new SimpleDateFormat(timeFormat);
        df.setTimeZone(tz);

        String nowAsISO = df.format(date);

        return nowAsISO;
    }
}
