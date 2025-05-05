package com.handson.chatbot.service;

import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class IMDBService {
    public static final Pattern PRODUCT_PATTERN = Pattern.compile("<h3 class=\"ipc-title__text\">\\d+\\.\\s(.*?)<\\/h3>");

    public String searchMovies(String keyword) throws IOException {
        return parseMoviesHtml(getMoviesHtml(), keyword);
    }

    private String parseMoviesHtml(String html, String keyword) {
        StringBuilder res = new StringBuilder();
        Matcher matcher = PRODUCT_PATTERN.matcher(html);
        String lowerKeyword = keyword.toLowerCase();

        while (matcher.find()) {
            String title = matcher.group(1);
            if (title.toLowerCase().contains(lowerKeyword)) {
                res.append(title).append('\n');
            }
        }
        return res.toString();
    }
    private String getMoviesHtml() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://www.imdb.com/chart/top/?ref_=nv_mv_250")
                .get()
                .addHeader("accept", "text/html")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36 Edg/135.0.0.0")
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
