package com.asgrim.harvest;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HarvestClient {
    private static final String TIME_URL = "https://api.harvestapp.com/v2/time_entries";

    private final String harvestApiKey;
    private final String harvestAccountId;

    public HarvestClient(@NotNull String harvestApiKey, @NotNull String harvestAccountId)
    {
        this.harvestApiKey = harvestApiKey;
        this.harvestAccountId = harvestAccountId;
    }

    public String requestTimeEntriesForPeriod(@NotNull DatePeriod datePeriod) throws IOException, InterruptedException {
        String url = TIME_URL
                + "?from="
                + datePeriod.fromDateFormatted()
                + "&to="
                + datePeriod.toDateFormatted();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer ".concat(harvestApiKey))
                .header("Harvest-Account-Id", harvestAccountId)
                .header("User-Agent", "https://github.com/asgrim/harvest-summary")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}
