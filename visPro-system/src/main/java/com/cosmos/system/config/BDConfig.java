package com.cosmos.system.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties("bd.config")
@Component
public class BDConfig {

    //有OCR功能的应用
    private String ocrAppId;

    private String ocrSecretKey;

    private String ocrApiKey;


    //有图片增强功能的应用
    private String enhanceAppId;

    private String enhanceSecretKey;

    private String enhanceApiKey;


    private String understandApiKey;

    private String understandSecretKey;

    private String clientId;
    private String clientSecret;


}
