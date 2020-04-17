package com.asgrim.harvest;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HoursForDay implements Comparable<HoursForDay> {
    private final String clientName;
    private final Date day;
    private Float hours;

    public HoursForDay(@NotNull String clientName, @NotNull Date day, @NotNull Float hoursForDay)
    {
        this.clientName = clientName;
        this.day = day;
        this.hours = hoursForDay;
    }

    public void add(@NotNull HoursForDay other)
    {
        this.hours += other.hours;
    }

    public Float hours()
    {
        return hours;
    }

    public String clientName()
    {
        return clientName;
    }

    public String toKey()
    {
        return clientName + " hours - " + new SimpleDateFormat("dd/MM/yyyy").format(day);
    }

    @Override
    public int compareTo(@NotNull HoursForDay o) {
        return this.day.compareTo(o.day);
    }
}
