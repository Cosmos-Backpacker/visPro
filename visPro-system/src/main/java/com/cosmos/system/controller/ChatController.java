package com.cosmos.system.controller;

import com.cosmos.system.config.ChatApp;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    @Resource
    private ChatApp chatApp;



    @GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatStream(String question, String chatId) {
        return chatApp.doChatStream(question, chatId);
    }


}

