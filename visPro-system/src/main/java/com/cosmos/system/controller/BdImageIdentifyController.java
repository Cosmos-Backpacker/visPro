package com.cosmos.system.controller;


import com.cosmos.common.domain.R;
import com.cosmos.system.BdClient.BdImageIdentify.AdvancedGeneral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * 图像理解
 */
@RestController
@RequestMapping("/BdImageIdentify")
public class BdImageIdentifyController {

    @Autowired
    private AdvancedGeneral advancedGeneral;

    /**
     * 通用理解
     *
     * @param file 图片
     * @return 结果
     */
    @PostMapping("/generalIdentify")
    public R<?> tyIdentify(@RequestPart(value = "file", required = false) MultipartFile file,
                           @RequestParam(required = false) String imageUrl) {

        if (file != null && !file.isEmpty()) {
            // 处理上传的文件
            String result = advancedGeneral.advancedGeneral(file, null);
            return R.success("success", result);
        } else if (imageUrl != null && !imageUrl.isEmpty()) {
            // 处理图片链接
            try {
                // 从链接下载图片并处理
                String res = advancedGeneral.advancedGeneral(null, imageUrl);
                return R.success("success", res);
            } catch (Exception e) {
                return R.error("error ，Failed to download image from URL");
            }
        } else {
            return R.error("error ，Either file or URL must be provided");
        }

    }


}
