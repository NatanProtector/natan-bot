package com.handson.chatbot.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Service
public class JokeService {
    public static String getJoke(
            @RequestParam boolean Programming,
            @RequestParam boolean Miscellaneous,
            @RequestParam boolean Dark,
            @RequestParam boolean Pun,
            @RequestParam boolean Spooky,
            @RequestParam boolean Christmas,
            @RequestParam boolean nsfw,
            @RequestParam boolean religious,
            @RequestParam boolean political,
            @RequestParam boolean racist,
            @RequestParam boolean sexist,
            @RequestParam boolean explicit,
            @RequestParam boolean single,
            @RequestParam boolean twopart
    ) {

        if (!twopart && !single) {
            return "Error! joke must be two part, single, or both!";
        }

        String path = createURL(
                Programming,
                Miscellaneous,
                Dark,
                Pun,
                Spooky,
                Christmas,
                nsfw,
                religious,
                political,
                racist,
                sexist,
                explicit,
                single,
                twopart
        );
        String JSON_result =  fetchJoke(path);

        JsonObject jsonObject = JsonParser.parseString(JSON_result).getAsJsonObject();
        boolean error = jsonObject.get("error").getAsBoolean();
        if (error) {
            String error_msg = jsonObject.get("message").getAsString();
            String cause_msg = jsonObject.get("causedBy").getAsString();

            return String.format(
                    "Error! %s\n%s",
                    error_msg,cause_msg
            );
        }


        String type = jsonObject.get("type").getAsString();
        if (type.equalsIgnoreCase("single"))
            return jsonObject.get("joke").getAsString();

        return String.format(
                "%s\n\n%s",
                jsonObject.get("setup").getAsString(),
                jsonObject.get("delivery").getAsString()
        );
    }

    private static String createURL(
            boolean Programming,
            boolean Miscellaneous,
            boolean Dark,
            boolean Pun,
            boolean Spooky,
            boolean Christmas,
            boolean nsfw,
            boolean religious,
            boolean political,
            boolean racist,
            boolean sexist,
            boolean explicit,
            boolean single,
            boolean twopart
    ) {
        // Categories
        StringBuilder categories = new StringBuilder();
        if (Programming) categories.append("Programming,");
        if (Miscellaneous) categories.append("Miscellaneous,");
        if (Dark) categories.append("Dark,");
        if (Pun) categories.append("Pun,");
        if (Spooky) categories.append("Spooky,");
        if (Christmas) categories.append("Christmas,");
        if (categories.length() > 0) {
            categories.setLength(categories.length() - 1); // Remove last comma
        } else {
            categories.append("Any");
        }

        // Query parameters
        StringBuilder query = new StringBuilder();
        boolean hasQuery = false;

        // Blacklist Flags
        StringBuilder blacklistFlags = new StringBuilder();
        if (nsfw) blacklistFlags.append("nsfw,");
        if (religious) blacklistFlags.append("religious,");
        if (political) blacklistFlags.append("political,");
        if (racist) blacklistFlags.append("racist,");
        if (sexist) blacklistFlags.append("sexist,");
        if (explicit) blacklistFlags.append("explicit,");
        if (blacklistFlags.length() > 0) {
            blacklistFlags.setLength(blacklistFlags.length() - 1); // Remove last comma
            query.append("blacklistFlags=").append(blacklistFlags);
            hasQuery = true;
        }

        // Type
        String type = "";
        if (single && !twopart) type = "type=single";
        else if (!single && twopart) type = "type=twopart";

        if (!type.isEmpty()) {
            if (hasQuery) {
                query.append("&");
            }
            query.append(type);
            hasQuery = true;
        }

        // Build final URL path
        StringBuilder path = new StringBuilder();
        path.append(categories);
        if (hasQuery) {
            path.append("?").append(query);
        }

        return path.toString();
    }


    public static String fetchJoke(String path) {

        String url = String.format(
                "https://v2.jokeapi.dev/joke/%s",
                path
        );

        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = new OkHttpClient();

        try (Response response = client.newCall(request).execute()) {

            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "Request failed: " + e.getMessage();
        }
    }
}
