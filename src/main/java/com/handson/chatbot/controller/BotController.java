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
import java.util.HashMap;

@Service
@RestController
@RequestMapping("/bot")
public class BotController {

    // Testing
    @Autowired
    WeatherService weatherService;

    @RequestMapping(value = "/weather", method = RequestMethod.GET)
    public ResponseEntity<?> getWeather(@RequestParam String city, @RequestParam int daysAhead) throws IOException {
        return new ResponseEntity<>(weatherService.getWeather(city, daysAhead), HttpStatus.OK);
    }

    // For bot

    @RequestMapping(value = "", method = { RequestMethod.POST})
    public ResponseEntity<?> getBotResponse(@RequestBody BotQuery query) throws IOException {
        HashMap<String, Object> params = query.getQueryResult().getParameters();
        String res = "Not found";
        if (params.containsKey("city")) {
            res = weatherService.getWeather((String)params.get("city"), (Integer) params.get("days"));
        } else if (params.containsKey("movie")) {
//            res = amazonService.searchProducts(params.get("product"));
        } else if (params.containsKey("joke")) {
            ;
        }
        return new ResponseEntity<>(BotResponse.of(res), HttpStatus.OK);
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


    @Autowired
    IMDBService ImdbService;

    @RequestMapping(value = "/movies", method = RequestMethod.GET)
    public ResponseEntity<?> getProduct(@RequestParam String keyword) throws IOException {
        return new ResponseEntity<>(ImdbService.searchMovies(keyword), HttpStatus.OK);
    }

    @Autowired
    JokeService jokeService;

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