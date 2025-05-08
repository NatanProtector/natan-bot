package com.handson.chatbot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class chatController {

    @GetMapping("")
    public String homePage() {
        return "chat"; // corresponds to home.html
    }
}
