//package com.serethewind.NeoClantech.config;
//
//import com.vonage.client.VonageClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class VonageConfig {
//
//    @Value("${vonage.api-key}")
//    private String apiKey;
//
//    @Value("${vonage.api-secret}")
//    private String apiSecret;
//
//    @Bean
//    public VonageClient vonageClient() {
//        VonageClient client = VonageClient.builder().apiKey(apiKey)
//                .apiSecret(apiSecret)
//                .build();
//        return client;
//    }
//}
