package com.cosmos.system.config;


import com.cosmos.system.chatMemory.MySqlChatMemory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ChatClientConfig {

    @Qualifier("openAiChatModel")
    @Autowired
    private ChatModel dsChatModel;


    @Autowired
    private MySqlChatMemory mySqlChatMemory;


    private static final String DS_SYSTEM_PROMPT = "你是一个助手，请回答用户问题";


    @Bean("dsChatClient")
    public ChatClient dsChatClient() {
        return ChatClient.builder(dsChatModel)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(mySqlChatMemory)
                        )
                .defaultSystem(DS_SYSTEM_PROMPT).build();
    }


}
