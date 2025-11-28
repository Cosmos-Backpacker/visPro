package com.cosmos.system.BdClient.BdImageIdentify;


import com.baidu.aip.util.Base64Util;
import com.cosmos.system.PhotoShiBie.FileUtil;
import com.cosmos.system.PhotoShiBie.HttpUtil;
import com.cosmos.system.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;

/**
 * 通用物体和场景识别
 */

@Service
public class AdvancedGeneral {

    //手动注入一个
    private final AuthService authService;

    @Autowired
    public AdvancedGeneral(AuthService authService) {
        this.authService = authService;
    }

    public String advancedGeneral(@RequestPart(value = "file", required = false) MultipartFile file,
                                  @RequestParam(value = "url", required = false) String imageUrl) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general";
        try {
            String param;
            if (file != null && !file.isEmpty()) {
                // 处理上传的文件
                //String filePath = "D:\\SpringBootProject\\VIS\\src\\main\\resources\\static\\test.png";
                byte[] imgData = FileUtil.readFileByBytesFile(file);

                String imgStr = Base64Util.encode(imgData);
                String imgParam = URLEncoder.encode(imgStr, "UTF-8");
                // 构建请求参数
                StringBuilder paramsBuilder = new StringBuilder();
                paramsBuilder.append("image=").append(imgParam);
                paramsBuilder.append("&baike_num=5"); // 添加baike_num参数，值为3
                param = paramsBuilder.toString();
            } else if (imageUrl != null && !imageUrl.isEmpty()) {

                String imgParam = URLEncoder.encode(imageUrl, "UTF-8");
                // 构建请求参数
                StringBuilder paramsBuilder = new StringBuilder();
                paramsBuilder.append("url=").append(imgParam);
                paramsBuilder.append("&baike_num=5"); // 添加baike_num参数，值为3
                param = paramsBuilder.toString();
            } else {
                throw new IllegalArgumentException("Either file or URL must be provided");
            }


            String accessToken = authService.getImageVisAuth();
            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}