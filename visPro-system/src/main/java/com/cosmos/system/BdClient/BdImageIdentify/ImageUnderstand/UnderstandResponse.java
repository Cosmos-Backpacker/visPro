package com.cosmos.system.BdClient.BdImageIdentify.ImageUnderstand;


import com.cosmos.system.config.BDConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class UnderstandResponse {

    private BDConfig bdConfig;
    private String API_KEY;
    private String SECRET_KEY;

    @Autowired
    public UnderstandResponse(BDConfig bdConfig) {
        this.bdConfig = bdConfig;
    }

    @PostConstruct
    public void init() {
        // 在依赖注入完成后初始化
        this.API_KEY = bdConfig.getUnderstandApiKey();
        this.SECRET_KEY = bdConfig.getUnderstandSecretKey();
        log.info("BD Config initialized - API_KEY: {}, SECRET_KEY: {}",
                API_KEY != null ? "***" + API_KEY.substring(API_KEY.length() - 4) : "null",
                SECRET_KEY != null ? "***" + SECRET_KEY.substring(SECRET_KEY.length() - 4) : "null");
    }

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public String getResponse(String taskId) throws IOException {
        System.out.println("这里的TASKID" + taskId);
        MediaType mediaType = MediaType.parse("application/json");
        // 创建包含taskId的JSON格式的RequestBody
        RequestBody body = RequestBody.create(mediaType, "{\"task_id\":\"" + taskId + "\"}");
        log.info("tag{}", taskId);
        System.out.println("这里的TASKID2" + taskId);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rest/2.0/image-classify/v1/image-understanding/get-result?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        //System.out.println(response.body().string());
        String result = response.body().string();
        return result;
    }


    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    private String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return new JSONObject(response.body().string()).getString("access_token");
    }

}