package com.avengers.yoribogo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

@Configuration

// MongoDB에 UTC 기반으로 들어가 KST와 시간 차이가 나는 문제를 해결하기 위해 사용한 클래스

public class MongoDBConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Arrays.asList(
                new StringToZonedDateTimeConverter(),   // String -> ZonedDateTime
                new ZonedDateTimeToStringConverter(),   // ZonedDateTime -> String
                new KSTToDateConverter(),               // KST 시간 -> Date
                new DateToKSTConverter()                // Date -> KST 시간
        ));
    }

    // String -> ZonedDateTime 변환기 (읽을 때)
    public static class StringToZonedDateTimeConverter implements org.springframework.core.convert.converter.Converter<String, ZonedDateTime> {
        @Override
        public ZonedDateTime convert(String source) {
            return ZonedDateTime.parse(source, DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(ZoneId.of("Asia/Seoul")));
        }
    }

    // ZonedDateTime -> String 변환기 (저장할 때)
    public static class ZonedDateTimeToStringConverter implements org.springframework.core.convert.converter.Converter<ZonedDateTime, String> {
        @Override
        public String convert(ZonedDateTime source) {
            return source.format(DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(ZoneId.of("Asia/Seoul")));
        }
    }

    // ZonedDateTime을 UTC로 저장하기 위한 변환기 (저장할 때)
    public static class KSTToDateConverter implements org.springframework.core.convert.converter.Converter<ZonedDateTime, Date> {
        @Override
        public Date convert(ZonedDateTime source) {
            ZonedDateTime adjustedZonedDateTime = source.withZoneSameInstant(ZoneId.of("UTC")).plusHours(9);
            return Date.from(adjustedZonedDateTime.toInstant());
        }
    }

    // Date를 KST로 변환하기 위한 변환기 (불러올 때)
    public static class DateToKSTConverter implements org.springframework.core.convert.converter.Converter<Date, ZonedDateTime> {
        @Override
        public ZonedDateTime convert(Date source) {
            return source.toInstant().atZone(ZoneId.of("Asia/Seoul")).minusHours(9);
        }
    }
}
