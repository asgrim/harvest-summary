package com.asgrim.harvest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HarvestResponseProcessor {
    public static CompleteHoursList processHarvestResponse(String harvestResponseJsonString) throws ParseException {
        JSONArray timeEntries = new JSONObject(harvestResponseJsonString).getJSONArray("time_entries");

        CompleteHoursList hoursList = new CompleteHoursList();

        for (int i = 0; i < timeEntries.length(); i++) {
            JSONObject timeEntry = timeEntries.getJSONObject(i);

            String clientName = timeEntry.getJSONObject("client").getString("name");
            Date recordDate = new SimpleDateFormat("yyyy-MM-dd").parse(timeEntry.getString("spent_date"));

            hoursList.addHoursForClient(new HoursForDay(clientName, recordDate, timeEntry.getFloat("hours")));
        }

        return hoursList;
    }
}
