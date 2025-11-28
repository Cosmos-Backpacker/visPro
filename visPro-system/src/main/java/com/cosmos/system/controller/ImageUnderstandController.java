package com.cosmos.system.controller;


import com.cosmos.common.domain.R;
import com.cosmos.system.BdClient.BdImageIdentify.ImageUnderstand.UnderstandRequest;
import com.cosmos.system.BdClient.BdImageIdentify.ImageUnderstand.UnderstandResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Slf4j
@RestController
@RequestMapping("/ImageUnderstand")
public class ImageUnderstandController {

    @Autowired
    private UnderstandRequest understandRequest;

    @Autowired
    private UnderstandResponse understandResponse;


    /**
     * 图像理解
     *
     * @param file 图片
     * @return 结果
     * @throws IOException          IO异常
     * @throws InterruptedException 中断异常
     */

    @PostMapping("/understand")
    public R<?> ImageUnderstand(@RequestPart(value = "file", required = false) MultipartFile file,
                                @RequestParam(value = "question") String question,
                                @RequestParam(value = "imageUrl", required = false) String imageUrl
    ) throws IOException, InterruptedException {


        if (question == null || question.isEmpty()) {
            return R.error("error, question is empty or invalid");
        }

        //先上传文件等待获取结果
        if (file == null && imageUrl == null) {
            return R.error("error, Either file or URL must be provided");
        }
        JSONObject result = null;
        if (file != null && !file.isEmpty()) {
            String resTaskId = understandRequest.getTaskId(file, question, null);
            JSONObject jsonObject = new JSONObject(resTaskId);
            result = jsonObject.getJSONObject("result");
        } else if (imageUrl != null && !imageUrl.isEmpty()) {
            String resTaskId = understandRequest.getTaskId(null, question, imageUrl);
            log.error("resTaskId{}", resTaskId);
            JSONObject jsonObject = new JSONObject(resTaskId);
            result = jsonObject.getJSONObject("result");
        }
        if (result == null) {
            return R.error("很抱歉，图像上传失败");
        }
        String taskId = result.getString("task_id");
        System.out.println(taskId);

        // 等待一段时间，6秒后再查询结果
        Thread.sleep(6000); // 参数是毫秒，这里表示等待5000毫秒，即5秒
        String ans = understandResponse.getResponse(taskId);
        //处理返回结果
        JSONObject responseObject = new JSONObject(ans);
        JSONObject resultAns = responseObject.getJSONObject("result");
        int retCode = resultAns.getInt("ret_code");

        if (retCode == 1) {
            return R.error("图像正在处理中，请稍后再试");
        }
        String description = resultAns.getString("description");


        return R.success("success", description);
    }

}
