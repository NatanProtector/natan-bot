package com.handson.chatbot.service;

import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherService {

    private static final Pattern TABLE_PATTERN = Pattern.compile("<tr>.*?</tr>", Pattern.DOTALL);

    private static final String regex = "<font class='w3'>([^<]+).*?</td>.*?" +                      // Weather
            "color=red>(\\d+)</font>-.*?color=blue>(\\d+)</font>.*?" +  // Temp high-low
            "<font class=\"w3\">(\\d+)</font>.*?" +                     // Humidity
            "<font class=\"w3w\">([^<]+)";

    private static final Pattern extraction_pattern = Pattern.compile(regex, Pattern.DOTALL);

    private static String parseWeatherHtml(String html, String city, int daysAhead) {
        StringBuilder res = new StringBuilder();

        Matcher rowMatcher = TABLE_PATTERN.matcher(html);
        String lowerCity = city.toLowerCase();

        while (rowMatcher.find()) {
            String tableRow = rowMatcher.group(0);

            Matcher dataMatcher = extraction_pattern.matcher(tableRow);

            if (dataMatcher.find()) {
                String weather = dataMatcher.group(1).trim();
                String tempHigh = dataMatcher.group(2);
                String tempLow = dataMatcher.group(3);
                String humidity = dataMatcher.group(4);
                String cityName = dataMatcher.group(5).trim();

                if (cityName.toLowerCase().contains(lowerCity)) {
                    LocalDate targetDate = LocalDate.now().plusDays(daysAhead);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");  // Example: May 3, 2025
                    String formattedDate = targetDate.format(formatter);

                    res.append("Forecast for ").append(formattedDate).append("\n")
                            .append("City: ").append(cityName).append("\n")
                            .append("Weather: ").append(weather).append("\n")
                            .append("Temperature: ").append(tempHigh).append("-").append(tempLow).append("\n")
                            .append("Humidity: ").append(humidity).append("\n");
                }
            }
        }

        return res.length() > 0 ? res.toString() : "City not found.";
    }

    private static String getWeatherHtml(int daysAhead) throws IOException {
        LocalDate targetDate = LocalDate.now().plusDays(daysAhead);

        String day = String.valueOf(targetDate.getDayOfMonth());
        String month = String.valueOf(targetDate.getMonthValue());
        String year = String.valueOf(targetDate.getYear());

        String url = String.format(
                "https://www.israelweather.co.il/english/3w.asp?day_t=%s&month_t=%s&year_t=%s",
                day, month, year
        );

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("User-Agent", "Mozilla/5.0")
                .addHeader("Accept", "text/html")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "Request failed with code: " + response.code();
            }
            assert response.body() != null;
            return response.body().string();
        }
    }

    // Main method with city + daysAhead
    public static String getWeather(String city, int daysAhead) throws IOException {
        return parseWeatherHtml(getWeatherHtml(daysAhead), city, daysAhead);
    }

}
