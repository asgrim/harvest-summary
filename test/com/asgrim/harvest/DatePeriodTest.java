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
                        {"1 January 2020", "2019-12-01", "2019-12-31"},
                        {"1 December 2019", "2019-11-01", "2019-11-30"},
                        {"1 November 2019", "2019-10-01", "2019-10-31"},
                        {"1 October 2019", "2019-09-01", "2019-09-30"},
                }),
                Arguments.of(2020, Calendar.APRIL, 16, new String[][]{
                        {"1 April 2020", "2020-03-01", "2020-03-31"},
                        {"1 March 2020", "2020-02-01", "2020-02-29"},
                        {"1 February 2020", "2020-01-01", "2020-01-31"},
                        {"1 January 2020", "2019-12-01", "2019-12-31"},
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
