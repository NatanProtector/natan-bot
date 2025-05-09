package com.handson.chatbot.controller;

import com.handson.chatbot.service.IMDBService;
import com.handson.chatbot.service.JokeService;
import com.handson.chatbot.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Service
@RestController
@RequestMapping("/bot")
public class BotController {

    @Autowired
    WeatherService weatherService;

    @Autowired
    IMDBService ImdbService;

    @Autowired
    JokeService jokeService;


    // For bot
    @RequestMapping(value = "", method = { RequestMethod.POST})
    public ResponseEntity<?> getBotResponse(@RequestBody BotQuery query) throws IOException {
        HashMap<String, Object> params = query.getQueryResult().getParameters();
        String res = "Not found";
        if (params.containsKey("city")) {
            Double daysDouble = (Double) params.get("days");
            int days = daysDouble.intValue();
            res = weatherService.getWeather((String) params.get("city"), days);
        } else if (params.containsKey("keyword")) {
            String keyword =(String) params.get("keyword");
            res = ImdbService.searchMovies(keyword);
        } else if (params.containsKey("type") || params.containsKey("blacklist")) {

            boolean Programming = false;
            boolean Miscellaneous = false;
            boolean Dark = false;
            boolean Pun = false;
            boolean Spooky = false;
            boolean Christmas = false;
            boolean NOT_nsfw = false;
            boolean NOT_religious = false;
            boolean NOT_political = false;
            boolean NOT_racist = false;
            boolean NOT_sexist = false;
            boolean NOT_explicit = false;
            boolean single = true;
            boolean twopart = true;

            ArrayList<String> type =(ArrayList<String>) params.get("type");
            ArrayList<String> blacklist = (ArrayList<String>) params.get("blacklist");

            // Mark joke types
            if (type.contains("any")) {
                Pun = true;
                Dark = true;
                Spooky = true;
                Christmas = true;
                Miscellaneous = true;
            } else {
                if (type.contains("puns"))
                    Pun = true;
                if (type.contains("dark"))
                    Dark = true;
                if (type.contains("spooky"))
                    Spooky = true;
                if (type.contains("christmas"))
                    Christmas = true;
                if (type.contains("miscellaneous"))
                    Miscellaneous = true;
            }

            if (blacklist.contains("nsfw"))
                NOT_nsfw = true;
            if (blacklist.contains("sexist"))
                NOT_sexist = true;
            if (blacklist.contains("racist"))
                NOT_racist = true;
            if (blacklist.contains("political"))
                NOT_political = true;
            if (blacklist.contains("religious"))
                NOT_religious = true;
            if (blacklist.contains("explicit"))
                NOT_explicit = true;



            res = jokeService.getJoke(
                    Programming,
                    Miscellaneous,
                    Dark,
                    Pun,
                    Spooky,
                    Christmas,
                    NOT_nsfw,
                    NOT_religious,
                    NOT_political,
                    NOT_racist,
                    NOT_sexist,
                    NOT_explicit,
                    single,
                    twopart
            );
        }

        return new ResponseEntity<>(BotResponse.of(res), HttpStatus.OK);
    }

    @RequestMapping(value = "/weather", method = RequestMethod.GET)
    public ResponseEntity<?> getWeather(@RequestParam String city, @RequestParam int daysAhead) throws IOException {
        return new ResponseEntity<>(weatherService.getWeather(city, daysAhead), HttpStatus.OK);
    }

    static class BotQuery {
        QueryResult queryResult;

        public QueryResult getQueryResult() {
            return queryResult;
        }
    }

    static class QueryResult {
        HashMap<String, Object> parameters;

        public HashMap<String, Object> getParameters() {
            return parameters;
        }
    }

    static class BotResponse {
        String fulfillmentText;
        String source = "BOT";

        public String getFulfillmentText() {
            return fulfillmentText;
        }

        public String getSource() {
            return source;
        }

        public static BotResponse of(String fulfillmentText) {
            BotResponse res = new BotResponse();
            res.fulfillmentText = fulfillmentText;
            return res;
        }

    }

    @RequestMapping(value = "/movies", method = RequestMethod.GET)
    public ResponseEntity<?> getProduct(@RequestParam String keyword) throws IOException {
        return new ResponseEntity<>(ImdbService.searchMovies(keyword), HttpStatus.OK);
    }

    @RequestMapping(value = "/joke", method = RequestMethod.GET)
    public ResponseEntity<?> getJoke(
            @RequestParam boolean Programming,
            @RequestParam boolean Miscellaneous,
            @RequestParam boolean Dark,
            @RequestParam boolean Pun,
            @RequestParam boolean Spooky,
            @RequestParam boolean Christmas,
            @RequestParam(defaultValue = "false") boolean NOT_nsfw ,
            @RequestParam(defaultValue = "false") boolean NOT_religious,
            @RequestParam(defaultValue = "false") boolean NOT_political,
            @RequestParam(defaultValue = "false") boolean NOT_racist,
            @RequestParam(defaultValue = "false") boolean NOT_sexist,
            @RequestParam(defaultValue = "false") boolean NOT_explicit,
            @RequestParam boolean single,
            @RequestParam boolean twopart
            ) throws IOException {
        return new ResponseEntity<>(
                jokeService.getJoke(
                        Programming,
                        Miscellaneous,
                        Dark,
                        Pun,
                        Spooky,
                        Christmas,
                        NOT_nsfw,
                        NOT_religious,
                        NOT_political,
                        NOT_racist,
                        NOT_sexist,
                        NOT_explicit,
                        single,
                        twopart
                ), HttpStatus.OK);
    }
}