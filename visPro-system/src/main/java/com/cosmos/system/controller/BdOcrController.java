package com.cosmos.system.controller;


import com.cosmos.common.domain.R;
import com.cosmos.system.BdClient.BdOcr.BdOcrClient;
import com.cosmos.system.BdClient.BdOcr.MathOcr.Formula;
import com.cosmos.system.BdClient.BdOcr.MedicalOCR.HealthReport;
import com.cosmos.system.BdClient.BdOcr.MedicalOCR.MedicalReportDetection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * OCR识别
 */
@RestController
@RequestMapping("/BdOcr")
@Slf4j
public class BdOcrController {

    @Autowired
    private BdOcrClient client;

    @Autowired
    private MedicalReportDetection medicalReportDetection;

    @Autowired
    private HealthReport healthReport;

    @Autowired
    private Formula formula;

    /**
     * 通用识别
     *
     * @param file 图片
     * @return 结果
     */
    @PostMapping("/general")
    public R<?> basicGeneralOcr(@RequestParam("file") MultipartFile file) {
        log.error("file{}", file);

        String res = client.basicAccurateGeneralOcr(file);
        System.out.println(res);

        return R.success("success", res);
    }


    @PostMapping("/generalUrl")
    public R<?> getFile(@RequestBody Map<String, String> requestBody) {

        String fileUrl = requestBody.get("fileUrl");
        log.error("fileUrl{}", fileUrl);

        String res = null;
        try {
            res = client.basicAccurateGeneralOcrUrl(fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(res);
        return R.success("后端上传成功", res);
    }

    @PostMapping("/generalUrlTest")
    public R<?> getFileTest(@RequestBody Map<String, String> requestBody) {
        String fileUrl = requestBody.get("fileUrl");
        log.error("fileUrl{}", fileUrl);

        String res = null;
        try {
            res = client.basicAccurateGeneralOcrUrl(fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(res);
        return R.success("后端上传成功", fileUrl);
    }


    @PostMapping(value = "/generalBytes", consumes = "application/octet-stream")
    public R<?> basicGeneralOcrByte(@RequestBody byte[] fileBytes) {
        log.error("file length: {}", fileBytes.length);
        // 检查文件是否为空
        if (fileBytes.length == 0) {
            return R.error("File is empty or not provided");
        }

        try {
            // 调用 OCR 客户端方法，假设 client.basicAccurateGeneralOcr 接受字节数组
            String res = client.basicAccurateGeneralBytesOcr(fileBytes);
            System.out.println(res);

            return R.success("success", res);
        } catch (Exception e) {
            // 日志记录错误
            log.error("Error processing file", e);
            return R.error("Error processing file: " + e.getMessage());
        }
    }


    /**
     * 医疗识别
     *
     * @param file 图片
     * @return 结果
     */
    @PostMapping("/MedicalReport")
    public R<?> medicalReportOcr(@RequestPart(value = "file", required = false) MultipartFile file,
                                 @RequestParam(required = false) String imageUrl) {
        if (file != null && !file.isEmpty()) {
            // 处理上传的文件
            String res = medicalReportDetection.medicalReportDetection(file, null);
            return R.success("success", res);
        } else if (imageUrl != null && !imageUrl.isEmpty()) {
            // 处理图片链接
            try {
                // 从链接下载图片并处理
                String res = medicalReportDetection.medicalReportDetection(null, imageUrl);
                return R.success("success", res);
            } catch (Exception e) {
                return R.error("error ，Failed to download image from URL");
            }
        } else {
            return R.error("error ，Either file or URL must be provided");
        }
    }


    /**
     * 健康识别
     *
     * @param file 图片
     * @return 结果
     */
    @PostMapping("/healthReport")
    public R<?> healthReportOcr(@RequestPart(value = "file", required = false) MultipartFile file,
                                @RequestParam(required = false) String imageUrl) {
        if (file != null && !file.isEmpty()) {
            // 处理上传的文件
            String res = healthReport.healthReport(file, null);
            return R.success("success", res);
        } else if (imageUrl != null && !imageUrl.isEmpty()) {
            // 处理图片链接
            try {
                // 从链接下载图片并处理
                String res = medicalReportDetection.medicalReportDetection(null, imageUrl);
                return R.success("success", res);
            } catch (Exception e) {
                return R.error("error ，Failed to download image from URL");
            }
        } else {
            return R.error("error ，Either file or URL must be provided");
        }
    }

    /**
     * 公式识别
     *
     * @param file 图片
     * @return 结果
     */
    @Deprecated
    @PostMapping("/Formula")
    public R<?> formulaOcr(@RequestPart(value = "file", required = false) MultipartFile file,
                           @RequestParam(required = false) String imageUrl) {
        if (file != null && !file.isEmpty()) {
            // 处理上传的文件
            String res = formula.formula(file, null);
            return R.success("success", res);
        } else if (imageUrl != null && !imageUrl.isEmpty()) {
            // 处理图片链接
            try {
                // 从链接下载图片并处理
                String res = formula.formula(null, imageUrl);
                return R.success("success", res);
            } catch (Exception e) {
                return R.error("error ，Failed to download image from URL");
            }
        } else {
            return R.error("error ，Either file or URL must be provided");
        }

    }


}
