package com.gtcafe.asimov.system.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gtcafe.asimov.system.bean.singleton.TimeConfig;

class TimeUtilsTest {

    // @Mock
    // private TimeConfig timeConfig;

    // @InjectMocks
    // private TimeUtils timeUtils;

    // @BeforeEach
    // void setUp() {
    //     MockitoAnnotations.openMocks(this);

    //     // 模擬 TimeConfig 參數
    //     when(timeConfig.getZone()).thenReturn("UTC");
    //     when(timeConfig.getFormat()).thenReturn("yyyy-MM-dd'T'HH:mm:ss'Z'");
    // }

    // @Test
    // void testCurrentTimeIso8601() {
    //     // 使用當前時間進行測試
    //     Date now = new Date();
    //     String expectedFormat = formatDate(now, "UTC", "yyyy-MM-dd'T'HH:mm:ss'Z'");

    //     String actualTime = timeUtils.currentTimeIso8601();

    //     assertThat(actualTime).isEqualTo(expectedFormat);
    // }

    // @Test
    // void testCurrentTimeIso8601WithDate() {
    //     // 測試指定日期
    //     Date testDate = new Date(1700000000000L); // 2023-11-14T20:26:40Z (示例時間)
    //     String expectedFormat = formatDate(testDate, "UTC", "yyyy-MM-dd'T'HH:mm:ss'Z'");

    //     String actualTime = timeUtils.currentTimeIso8601(testDate);

    //     assertThat(actualTime).isEqualTo(expectedFormat);
    // }

    // @Test
    // void testTimeFormatStaticMethod() {
    //     // 測試靜態方法
    //     Date testDate = new Date(1700000000000L); // 2023-11-14T20:26:40Z
    //     String expectedFormat = "2023-11-14T20:26:40Z";

    //     String actualTime = TimeUtils.TimeFormat(testDate, "UTC", "yyyy-MM-dd'T'HH:mm:ss'Z'");

    //     assertThat(actualTime).isEqualTo(expectedFormat);
    // }

    // // Helper method to format date for expected results
    // private String formatDate(Date date, String timeZone, String format) {
    //     SimpleDateFormat sdf = new SimpleDateFormat(format);
    //     sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
    //     return sdf.format(date);
    // }
}
