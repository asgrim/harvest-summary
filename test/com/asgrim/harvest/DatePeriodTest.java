package com.asgrim.harvest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DatePeriodTest {
    private static Stream<Arguments> datePeriodsProvider()
    {
        return Stream.of(
                Arguments.of(2020, Calendar.JANUARY, 1, new String[][]{
                        {"1 January 2020", "2019-12-15", "2019-12-31"},
                        {"15 December 2019", "2019-12-01", "2019-12-14"},
                        {"1 December 2019", "2019-11-15", "2019-11-30"},
                        {"15 November 2019", "2019-11-01", "2019-11-14"},
                }),
                Arguments.of(2020, Calendar.APRIL, 16, new String[][]{
                        {"15 April 2020", "2020-04-01", "2020-04-14"},
                        {"1 April 2020", "2020-03-15", "2020-03-31"},
                        {"15 March 2020", "2020-03-01", "2020-03-14"},
                        {"1 March 2020", "2020-02-15", "2020-02-29"},
                })
        );
    }

    @ParameterizedTest
    @MethodSource("datePeriodsProvider")
    void generateDatePeriodsRelativeToDate(int year, int month, int day, String[][] things) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(year, month, day);

        List<DatePeriod> periods = DatePeriod.generateDatePeriodsRelativeToDate(cal.getTime());

        int index = 0;
        for (String[] thing : things) {
            assertEquals(thing[0], periods.get(index).toString());
            assertEquals(thing[1], periods.get(index).fromDateFormatted());
            assertEquals(thing[2], periods.get(index).toDateFormatted());
            index++;
        }
    }
}