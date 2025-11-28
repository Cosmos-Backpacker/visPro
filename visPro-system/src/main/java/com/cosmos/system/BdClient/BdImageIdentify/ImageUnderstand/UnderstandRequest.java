package com.cosmos.system.BdClient.BdImageIdentify.ImageUnderstand;

import com.cosmos.system.service.AuthService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;


/**
 * 需要添加依赖
 * <!-- https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp -->
 * <dependency>
 * <groupId>com.squareup.okhttp3</groupId>
 * <artifactId>okhttp</artifactId>
 * <version>4.12.0</version>
 * </dependency>
 */


@Service
public class UnderstandRequest {


    private final AuthService authService;

    @Autowired
    public UnderstandRequest(AuthService authService) {
        this.authService = authService;
    }

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public String getTaskId(@RequestParam(required = false, value = "file") MultipartFile file, @RequestParam(value = "question") String question, @RequestParam(required = false, value = "url") String imageUrl) throws IOException {


        MediaType mediaType = MediaType.parse("application/json");
        String json = "";
        if (file == null && imageUrl == null) {
            return "error, Either file or URL must be provided";
        }

        if (file != null && !file.isEmpty()) {

            byte[] b = file.getBytes();
            String imageBase64 = Base64.getEncoder().encodeToString(b);

            // 创建JSON格式的请求体，包含图片的Base64编码和问题
            //MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            json = "{\"image\":\"" + imageBase64 + "\",\"question\":\"" + question + "\"}";
        } else if (imageUrl != null && !imageUrl.isEmpty()) {
            json = "{\"url\":\"" + imageUrl + "\",\"question\":\"" + question + "\"}";
        }

        RequestBody body = RequestBody.create(mediaType, json);

        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rest/2.0/image-classify/v1/image-understanding/request?access_token=" + authService.getImageVisAuth())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        String taskId = response.body().string();
        return taskId;
    }


    /**
     * 获取文件base64编码
     *
     * @param path      文件路径
     * @param urlEncode 如果Content-Type是application/x-www-form-urlencoded时,传true
     * @return base64编码信息，不带文件头
     * @throws IOException IO异常
     */


//    static String getFileContentAsBase64(String path, boolean urlEncode) throws IOException {
//        byte[] b = Files.readAllBytes(Paths.get(path));
//        String base64 = Base64.getEncoder().encodeToString(b);
//        if (urlEncode) {
//            base64 = URLEncoder.encode(base64, "utf-8");
//        }
//        return base64;
//    }


}