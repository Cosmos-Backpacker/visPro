package com.cosmos.system.BdClient.BdOcr.MedicalOCR;


import com.cosmos.system.PhotoShiBie.Base64Util;
import com.cosmos.system.PhotoShiBie.HttpUtil;
import com.cosmos.system.config.BDConfig;
import com.cosmos.system.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;

/**
 * 医疗检验报告单识别
 */

@Service
@Slf4j
public class MedicalReportDetection {

    @Autowired
    private AuthService authService;

    @Autowired
    private BDConfig bdConfig;

    /**
     * 重要提示代码中所需工具类
     * FileUtil,Base64Util,HttpUtil,GsonUtils请从
     * 下载
     */
    public String medicalReportDetection(@RequestParam(value = "file", required = false) MultipartFile file,
                                         @RequestParam(value = "url", required = false) String imageUrl) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/medical_report_detection";
        try {
            String param;
            if (file != null && !file.isEmpty()) {
                // 处理上传的文件
                byte[] imgData = file.getBytes();
                String imgStr = Base64Util.encode(imgData);
                String imgParam = URLEncoder.encode(imgStr, "UTF-8");
                param = "image=" + imgParam;
            } else if (imageUrl != null && !imageUrl.isEmpty()) {
                // 处理图片链接
                param = "url=" + URLEncoder.encode(imageUrl, "UTF-8");
            } else {
                throw new IllegalArgumentException("Either file or URL must be provided");
            }

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间，客户端可自行缓存，过期后重新获取。
            String accessToken = authService.getAccessToken(bdConfig.getOcrApiKey(), bdConfig.getOcrSecretKey());
            System.out.println("MedicReporter" + accessToken);

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}