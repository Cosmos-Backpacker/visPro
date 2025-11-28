package com.cosmos.system.controller;


import com.cosmos.common.domain.R;
import com.cosmos.system.BdClient.BdImageEnhance.EnhanceImageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@Slf4j
@RequestMapping("/BdImageEnhance")
public class BdEnhanceController {

    @Autowired
    private EnhanceImageClient enhanceImageClient;


    @PostMapping("/selfieAnime")
    public R<?> selfieAnime(@RequestPart(value = "file", required = false) MultipartFile file,
                            @RequestParam(required = false) String imageUrl) {

        if (file != null && !file.isEmpty()) {
            // 处理上传的文件
            String result = enhanceImageClient.selfieAnime(file, null);
            return R.success("success", result);
        } else if (StringUtils.isNotBlank(imageUrl)) {
            try {
                // 从链接下载图片并处理
                String res = enhanceImageClient.selfieAnime(null, imageUrl);
                return R.success("success", res);
            } catch (Exception e) {
                return R.error("error ，Failed to download image from URL");
            }
        } else {
            return R.error("error ，Either file or URL must be provided");
        }
    }


    @PostMapping("/styleTrans")
    public R<?> styleTrans(@RequestPart(value = "file", required = false) MultipartFile file,
                           @RequestParam(required = false) String imageUrl,
                           @RequestParam("choice") int choice) {

        if (file != null && !file.isEmpty()) {
            // 处理上传的文件
            String result = enhanceImageClient.styleTrans(file, null, choice);
            return R.success("success", result);
        } else if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                // 从链接下载图片并处理
                String res = enhanceImageClient.styleTrans(null, imageUrl, choice);
                return R.success("success", res);
            } catch (Exception e) {
                return R.error("error ，Failed to download image from URL");
            }
        } else {
            return R.error("error ，Either file or URL must be provided");
        }
    }

    @PostMapping("/imageQualityEnhance")
    public R<?> imageQualityEnhance(@RequestPart(value = "file", required = false) MultipartFile file,
                                    @RequestParam(required = false) String imageUrl) {

        if (file != null && !file.isEmpty()) {
            // 处理上传的文件
            String result = enhanceImageClient.imageQualityEnhance(file, null);
            return R.success("success", result);
        } else if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                // 从链接下载图片并处理
                String res = enhanceImageClient.imageQualityEnhance(null, imageUrl);
                return R.success("success", res);
            } catch (Exception e) {
                return R.error("error ，Failed to download image from URL");
            }
        } else {
            return R.error("error ，Either file or URL must be provided");
        }
    }

    @PostMapping("/imageDefinitionEnhance")
    public R<?> imageDefinitionEnhance(@RequestPart(value = "file", required = false) MultipartFile file,
                                       @RequestParam(required = false) String imageUrl) {

        if (file != null && !file.isEmpty()) {
            // 处理上传的文件
            String result = enhanceImageClient.imageDefinitionEnhance(file, null);
            return R.success("success", result);
        } else if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                // 从链接下载图片并处理
                String res = enhanceImageClient.imageDefinitionEnhance(null, imageUrl);
                return R.success("success", res);
            } catch (Exception e) {
                return R.error("error ，Failed to download image from URL");
            }
        } else {
            return R.error("error ，Either file or URL must be provided");
        }
    }

    @PostMapping("/colourize")
    public R<?> colourize(@RequestPart(value = "file", required = false) MultipartFile file,
                          @RequestParam(required = false) String imageUrl) {

        if (file != null && !file.isEmpty()) {
            // 处理上传的文件
            String result = enhanceImageClient.colourize(file, null);
            return R.success("success", result);
        } else if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                // 从链接下载图片并处理
                String res = enhanceImageClient.colourize(null, imageUrl);
                return R.success("success", res);
            } catch (Exception e) {
                return R.error("error ，Failed to download image from URL");
            }
        } else {
            return R.error("error ，Either file or URL must be provided");
        }
    }


}
