package com.huydo.be.config.payment;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.*;

@Configuration
public class VNPayConfig {

    @Getter
    private String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";

    @Getter
    @Value("${vnpay.return-url:http://127.0.0.1:5173/payment-success}")
    private String vnp_ReturnUrl;

    @Getter
    @Value("${vnpay.tmn-code:TXOOZNX4}")
    private String vnp_TmnCode;

    @Getter
    @Value("${vnpay.hash-secret:HUQHTRVXVRGJJWHMBFCAUBAXOSAJBIND}")
    private String vnp_HashSecret;

    @Getter
    private String vnp_ApiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

    public VNPayConfig() {
        this.vnp_TmnCode = "TXOOZNX4";
        this.vnp_HashSecret = "HUQHTRVXVRGJJWHMBFCAUBAXOSAJBIND";
    }

    public Map<String, String> getVNPayConfig(String orderType) {
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", getRandomNumber(8));
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + getRandomNumber(8));
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", "127.0.0.1");

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        return vnp_Params;
    }

    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}