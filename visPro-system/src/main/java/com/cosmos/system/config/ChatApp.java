package com.cosmos.system.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
public class ChatApp {


    private final ChatClient dsChatClient;

    @Autowired
    public ChatApp(ChatClient dsChatClient) {
        this.dsChatClient = dsChatClient;
    }

    /**
     * 流式返回结果
     */
    public Flux<String> doChatStream(String message, String chatId) {
        return dsChatClient.prompt()
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId) //顾问的具体参数是什么
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .stream()
                .content();
    }


}
