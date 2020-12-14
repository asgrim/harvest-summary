package com.asgrim.harvest;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;

public class DatePeriod {
    private final Date invoiceDate;
    private final Date from;
    private final Date to;

    private DatePeriod(@NotNull Date invoiceDate, @NotNull Date from, @NotNull Date to)
    {
        this.invoiceDate = invoiceDate;
        this.from = from;
        this.to = to;
    }

    public static Date nextInvoiceDate()
    {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());

        while(cal.get(Calendar.DAY_OF_MONTH) != 1 && cal.get(Calendar.DAY_OF_MONTH) != 15) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        return cal.getTime();
    }

    public static List<DatePeriod> generateDatePeriodsRelativeToDate(Date now)
    {
        ArrayList<DatePeriod> periods = new ArrayList<>();
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(now);

        for(int i = 0; i < 4; i++) {
            Date invoiceDate, from, to;
            if (cal.get(Calendar.DAY_OF_MONTH) >= 15) {
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 15, 0, 0, 0);
                invoiceDate = cal.getTime();

                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
                from = cal.getTime();

                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 14);
                to = cal.getTime();

                periods.add(new DatePeriod(invoiceDate, from, to));
                cal.setTime(from);
                continue;
            }

            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0);
            invoiceDate = cal.getTime();

            cal.add(Calendar.MONTH, -1);

            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 15);
            from = cal.getTime();

            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            to = cal.getTime();

            periods.add(new DatePeriod(invoiceDate, from, to));
            cal.setTime(from);
        }

        return periods;
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("d MMMM yyyy").format(invoiceDate);
    }

    public String fromDateFormatted() {
        return new SimpleDateFormat("yyyy-MM-dd").format(from);
    }

    public String toDateFormatted() {
        return new SimpleDateFormat("yyyy-MM-dd").format(to);
    }
}
