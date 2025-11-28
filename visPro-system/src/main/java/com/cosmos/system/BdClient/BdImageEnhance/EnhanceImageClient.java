package com.cosmos.system.BdClient.BdImageEnhance;


import com.baidu.aip.imageprocess.AipImageProcess;
import com.cosmos.system.config.BDConfig;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

@Service
@NoArgsConstructor
public class EnhanceImageClient {

    private static AipImageProcess client;

    @Autowired
    private BDConfig bdConfig;

    // 使用 @PostConstruct 注解确保在依赖注入完成后初始化 client
    @PostConstruct
    public void initClient() {
        // 注意：这里我们不需要再次注入 bdConfig，因为它已经在构造函数或字段注入中完成
        client = new AipImageProcess(bdConfig.getEnhanceAppId(), bdConfig.getEnhanceApiKey(), bdConfig.getEnhanceSecretKey());
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

    }


    public String selfieAnime(@RequestParam(value = "file", required = false) MultipartFile file,
                              @RequestParam(value = "url", required = false) String imageUrl) {
        // 创建一个options用于请求时设置要求
        HashMap<String, Object> options = new HashMap<String, Object>();
        JSONObject res;

        try {
            if (file != null && !file.isEmpty()) {
                // 处理上传的文件
                res = client.selfieAnime(file.getBytes(), options);
                return res.toString(2);
            } else if (imageUrl != null && !imageUrl.isEmpty()) {
                // 处理图片链接：从链接下载图片并转换为字节数组
                byte[] imageBytes = downloadImageFromUrl(imageUrl);
                res = client.selfieAnime(imageBytes, options);
                return res.toString(2);
            } else {
                throw new IllegalArgumentException("Either file or URL must be provided");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //图像风格转换接口
    public String styleTrans(@RequestParam(value = "file", required = false) MultipartFile file,
                             @RequestParam(value = "url", required = false) String imageUrl,
                             @RequestParam("choice") int choice) {
        // 创建一个options用于请求时设置要求
        HashMap<String, String> options = new HashMap<String, String>();

        // 根据选择设置要转换的风格
        switch (choice) {
            case 0 -> options.put("option", "cartoon"); // 卡通画风格
            case 1 -> options.put("option", "pencil"); // 铅笔风格f
            case 2 -> options.put("option", "color_pencil"); // 彩色铅笔画风格
            case 3 -> options.put("option", "warm"); // 彩色糖块油画风格
            case 4 -> options.put("option", "wave"); // 神奈川冲浪里油画风格
            case 5 -> options.put("option", "lavender"); // 薰衣草油画风格
            case 6 -> options.put("option", "mononoke"); // 奇异油画风格
            case 7 -> options.put("option", "scream"); // 呐喊油画风格
            case 8 -> options.put("option", "gothic"); // 哥特油画风格
            default -> options.put("option", "cartoon"); // 默认为卡通
        }

        JSONObject res; // 用于获取返回的response

        try {
            if (file != null && !file.isEmpty()) {
                // 处理上传的文件
                res = client.styleTrans(file.getBytes(), options);
                return res.toString(2);
            } else if (imageUrl != null && !imageUrl.isEmpty()) {
                // 处理图片链接
                byte[] imageBytes = downloadImageFromUrl(imageUrl);
                res = client.styleTrans(imageBytes, options);
                return res.toString(2);
            } else {
                throw new IllegalArgumentException("Either file or URL must be provided");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    //图像无损放大接口
    public String imageQualityEnhance(@RequestParam(value = "file", required = false) MultipartFile file,
                                      @RequestParam(value = "url", required = false) String imageUrl) {
        // 创建一个options用于请求时设置要求
        HashMap<String, String> options = new HashMap<String, String>();
        JSONObject res;

        try {
            if (file != null && !file.isEmpty()) {
                // 处理上传的文件
                res = client.imageQualityEnhance(file.getBytes(), options);
                return res.toString(2); // 2是缩进两个空格，方便显示
            } else if (imageUrl != null && !imageUrl.isEmpty()) {
                // 处理图片链接
                byte[] imageBytes = downloadImageFromUrl(imageUrl);
                res = client.imageQualityEnhance(imageBytes, options);
                return res.toString(2); // 2是缩进两个空格，方便显示
            } else {
                throw new IllegalArgumentException("Either file or URL must be provided");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //图像清晰度增强接口
    public String imageDefinitionEnhance(@RequestParam(value = "file", required = false) MultipartFile file,
                                         @RequestParam(value = "url", required = false) String imageUrl) {
        // 创建一个options用于请求时设置要求
        HashMap<String, String> options = new HashMap<String, String>();
        JSONObject res;

        try {
            if (file != null && !file.isEmpty()) {
                // 处理上传的文件
                res = client.imageDefinitionEnhance(file.getBytes(), options);
                return res.toString(2); // 2是缩进两个空格，方便显示
            } else if (imageUrl != null && !imageUrl.isEmpty()) {
                // 处理图片链接
                byte[] imageBytes = downloadImageFromUrl(imageUrl);
                res = client.imageDefinitionEnhance(imageBytes, options);
                return res.toString(2); // 2是缩进两个空格，方便显示
            } else {
                throw new IllegalArgumentException("Either file or URL must be provided");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    //黑白图像上色
    public String colourize(@RequestParam(value = "file", required = false) MultipartFile file,
                            @RequestParam(value = "url", required = false) String imageUrl) {
        // 创建一个options用于请求时设置要求
        HashMap<String, String> options = new HashMap<String, String>();
        JSONObject res;

        try {
            if (file != null && !file.isEmpty()) {
                // 处理上传的文件
                res = client.colourize(file.getBytes(), options);
                return res.toString(2); // 2是缩进两个空格，方便显示
            } else if (imageUrl != null && !imageUrl.isEmpty()) {
                // 处理图片链接

                byte[] imageBytes = downloadImageFromUrl(imageUrl);
                res = client.colourize(imageBytes, options);
                return res.toString(2); // 2是缩进两个空格，方便显示
            } else {
                throw new IllegalArgumentException("Either file or URL must be provided");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // 辅助方法：从图片链接下载图片并转换为字节数组
    private byte[] downloadImageFromUrl(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream in = url.openStream()) {
            return in.readAllBytes();
        }
    }
}