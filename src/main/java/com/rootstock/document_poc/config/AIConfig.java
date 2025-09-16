package com.rootstock.document_poc.config;

import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {
    @Bean
    public List<Advisor> defaultAdvisors(VectorStore vectorStore) {
        return List.of(
            new QuestionAnswerAdvisor(vectorStore)
        );
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, List<Advisor> defaultAdvisors) {
        return chatClientBuilder.defaultAdvisors(defaultAdvisors).build();
    }
}