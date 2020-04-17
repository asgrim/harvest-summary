package com.asgrim.harvest;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CompleteHoursList {
    private final HashMap<String, Float> totalHoursPerClient = new HashMap<>();
    private final HashMap<String, HoursForDay> totalHoursPerDayPerClient = new HashMap<>();

    public void addHoursForClient(@NotNull HoursForDay hoursForDay)
    {
        addOrUpdateDailyTotal(hoursForDay);
        addOrUpdateClientTotal(hoursForDay);
    }

    public ArrayList<HoursForDay> sortedDailyHours()
    {
        ArrayList<HoursForDay> sortedTimes = new ArrayList<>(totalHoursPerDayPerClient.values());
        Collections.sort(sortedTimes);
        return sortedTimes;
    }

    public Set<Map.Entry<String, Float>> totalHoursPerClient()
    {
        return totalHoursPerClient.entrySet();
    }

    private void addOrUpdateDailyTotal(@NotNull HoursForDay hoursForDay)
    {
        String keyName = hoursForDay.toKey();

        if (! totalHoursPerDayPerClient.containsKey(keyName)) {
            totalHoursPerDayPerClient.put(keyName, hoursForDay);
        } else {
            totalHoursPerDayPerClient.get(keyName).add(hoursForDay);
        }
    }

    private void addOrUpdateClientTotal(@NotNull HoursForDay hoursForDay)
    {
        String clientName = "Total " + hoursForDay.clientName() + " Hours";

        if (! totalHoursPerClient.containsKey(clientName)) {
            totalHoursPerClient.put(clientName, hoursForDay.hours());
        } else {
            totalHoursPerClient.replace(clientName, totalHoursPerClient.get(clientName) + hoursForDay.hours());
        }
    }
}
