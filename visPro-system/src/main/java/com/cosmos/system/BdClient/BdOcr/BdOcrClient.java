package com.cosmos.system.BdClient.BdOcr;

import com.baidu.aip.ocr.AipOcr;
import com.cosmos.system.config.BDConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

//@Service注解默认就是单例模式，（即创建一个实例然后调用时再注入）
@Service
@Slf4j
public class BdOcrClient {
    @Autowired
    private BDConfig bdConfig;
    //初始化一个AipOcr实例
    private static AipOcr client;


    // 使用 @PostConstruct 注解确保在依赖注入完成后初始化 client
    @PostConstruct
    public void initClient() {
        // 注意：这里我们不需要再次注入 bdConfig，因为它已经在构造函数或字段注入中完成
        client = new AipOcr(bdConfig.getOcrAppId(), bdConfig.getOcrApiKey(), bdConfig.getOcrSecretKey());
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

    }


    //通用文字识别
    public String basicGeneralOCR(MultipartFile file) {
        //设置options，代表需要返回哪一些值
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");
        options.put("probability", "true");
        JSONObject res;
        try {
            res = client.basicAccurateGeneral(file.getBytes(), options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return res.toString(2);

    }


    public String basicAccurateGeneralOcrUrl(String url) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");    //代表上传图片时检测图片的朝向
        options.put("probability", "true");  //返回行置信度
        log.error(url);
        JSONObject res = client.basicAccurateGeneralUrl(url, options);
        return res.toString(2);  //这个2代表jsonObject对象转为String时每一行会缩进两个字符
    }


    public String basicAccurateGeneralOcr(MultipartFile file) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");    //代表上传图片时检测图片的朝向
        options.put("probability", "true");  //返回行置信度

        byte[] bytes; //


        try {
            bytes = file.getBytes();
            log.error("bytes:{}", bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject res = client.basicAccurateGeneral(bytes, options);
        return res.toString(2);  //这个2代表jsonObject对象转为String时每一行会缩进两个字符
    }


    public String basicAccurateGeneralBytesOcr(byte[] bytes) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");    //代表上传图片时检测图片的朝向
        options.put("probability", "true");  //返回行置信度

        JSONObject res = client.basicAccurateGeneral(bytes, options);
        return res.toString(2);  //这个2代表jsonObject对象转为String时每一行会缩进两个字符
    }


    //身份真正面识别
    public String idCardOcrFront(MultipartFile file) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");
        options.put("detect_risk", "false");
        String idCardSide = "front";

        try {
            byte[] bytes = file.getBytes();
            JSONObject res = client.idcard(bytes, idCardSide, options);
            return res.toString(2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //身份证反面识别
    public String idCardOcrBack(MultipartFile file) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");
        options.put("detect_risk", "false");
        String idCardSide = "back";

        try {
            byte[] bytes = file.getBytes();
            JSONObject res = client.idcard(bytes, idCardSide, options);
            return res.toString(2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // 网络图片文字识别
    public String webImageOcr(String imageUrl) {
        HashMap<String, String> options = new HashMap<>();
        options.put("detect_direction", "true");
        options.put("detect_language", "true");
        try {
            return client.webImageUrl(imageUrl, options).toString(2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 银行卡识别
    public String bankCardOcr(MultipartFile file) {
        try {
            return client.bankcard(file.getBytes(), new HashMap<>()).toString(2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 驾驶证识别
    public String drivingLicenseOcr(MultipartFile file) {
        HashMap<String, String> options = new HashMap<>();
        options.put("detect_direction", "true");
        try {
            return client.drivingLicense(file.getBytes(), options).toString(2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 行驶证识别
    public String vehicleLicenseOcr(MultipartFile file) {
        HashMap<String, String> options = new HashMap<>();
        options.put("detect_direction", "true");
        try {
            return client.vehicleLicense(file.getBytes(), options).toString(2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 车牌识别
    public String plateLicenseOcr(MultipartFile file) {
        HashMap<String, String> options = new HashMap<>();
        options.put("multi_detect", "true");
        try {
            return client.plateLicense(file.getBytes(), options).toString(2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 营业执照识别
    public String businessLicenseOcr(MultipartFile file) {
        try {
            return client.businessLicense(file.getBytes(), new HashMap<>()).toString(2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 通用票据识别
    public String receiptOcr(MultipartFile file) {
        HashMap<String, String> options = new HashMap<>();
        options.put("recognize_granularity", "big");
        options.put("probability", "true");
        options.put("accuracy", "normal");
        options.put("detect_direction", "true");
        try {
            return client.receipt(file.getBytes(), options).toString(2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 自定义模板文字识别
    public String customTemplateOcr(MultipartFile file, String templateSign) {
        HashMap<String, String> options = new HashMap<>();
        options.put("templateSign", templateSign);
        try {
            return client.custom(file.getBytes(), options).toString(2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // 试卷分析与识别
    public String docAnalysisOCR(MultipartFile file) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("language_type", "CHN_ENG"); // 可选值范围: CHN_ENG, ENG
        options.put("result_type", "big"); // 返回识别结果是按单行结果返回，还是按单字结果返回，默认为big
        options.put("detect_direction", "true"); // 是否检测图像朝向，默认不检测，即：false
        options.put("line_probability", "true"); // 是否返回每行识别结果的置信度。默认为false
        options.put("words_type", "handprint_mix"); // 文字类型，默认：印刷文字识别

        try {
            byte[] bytes = file.getBytes();
            JSONObject res = client.docAnalysis(bytes, options);
            return res.toString(2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 仪器仪表盘读数识别
    public String meterOCR(MultipartFile file) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("probability", "true"); // 是否返回每行识别结果的置信度。默认为false
        options.put("poly_location", "true"); // 位置信息返回形式，默认：false

        try {
            byte[] bytes = file.getBytes();
            JSONObject res = client.meter(bytes, options);
            return res.toString(2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 网络图片文字识别（含位置版）
    public String webImageLocOCR(MultipartFile file) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true"); // 是否检测图像朝向，默认不检测，即：false
        options.put("probability", "true"); // 是否返回每行识别结果的置信度。默认为false
        options.put("poly_location", "true"); // 是否返回文字所在区域的外接四边形的4个点坐标信息。默认为false
        options.put("recognize_granularity", "small"); // 是否定位单字符位置，big：不定位单字符位置，默认值；small：定位单字符位置

        try {
            byte[] bytes = file.getBytes();
            JSONObject res = client.webimageLoc(bytes, options);
            return res.toString(2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 增值税发票识别
    public String vatInvoiceOCR(MultipartFile file) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("type", "normal"); // 可选参数，进行识别的增值税发票类型，默认为 normal，可缺省

        try {
            byte[] bytes = file.getBytes();
            JSONObject res = client.vatInvoice(bytes, options);
            return res.toString(2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 出租车票识别
    public String taxiReceiptOCR(MultipartFile file) {
        HashMap<String, String> options = new HashMap<String, String>();
        // options中无需要设置的参数

        try {
            byte[] bytes = file.getBytes();
            JSONObject res = client.taxiReceipt(bytes, options);
            return res.toString(2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // vin码识别
    public String vinCodeOCR(MultipartFile file) {
        HashMap<String, String> options = new HashMap<String, String>();
        // options中无需要设置的参数

        try {
            byte[] bytes = file.getBytes();
            JSONObject res = client.vinCode(bytes, options);
            return res.toString(2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 火车票识别
    public String trainTicketOCR(MultipartFile file) {
        HashMap<String, String> options = new HashMap<String, String>();
        // options中无需要设置的参数

        try {
            byte[] bytes = file.getBytes();
            JSONObject res = client.trainTicket(bytes, options);
            return res.toString(2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 数字识别
    public String numbersOCR(MultipartFile file) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("recognize_granularity", "big"); // big：不定位单字符位置；small：定位单字符位置，默认值为big

        try {
            byte[] bytes = file.getBytes();
            JSONObject res = client.numbers(bytes, options);
            return res.toString(2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 二维码识别
    public String qrCodeOCR(MultipartFile file) {
        HashMap<String, String> options = new HashMap<String, String>();
        // options中无需要设置的参数

        try {
            byte[] bytes = file.getBytes();
            JSONObject res = client.qrcode(bytes, options);
            return res.toString(2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
