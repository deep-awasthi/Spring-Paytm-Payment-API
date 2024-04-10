package com.deepawasthi.SpringPaymentPaytm.Services;

import com.deepawasthi.SpringPaymentPaytm.Configurations.AppConfigurations;
import com.paytm.pg.merchant.PaytmChecksum;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.Map;
import java.util.UUID;

@Service
public class PaytmServicesImpl implements PaytmServices{

    @Override
    public Map<String, Object> payment(Map<String, Object> data) {

        Map response = null;

        UUID orderId = UUID.randomUUID();
        JSONObject paytmPaymentBody = new JSONObject();
        paytmPaymentBody.put("requestType", "Payment");
        paytmPaymentBody.put("mid", AppConfigurations.MERCHANT_ID);
        paytmPaymentBody.put("websiteName", AppConfigurations.WEBSITE);
        paytmPaymentBody.put("orderId", orderId);
        paytmPaymentBody.put("callbackUrl", "http://localhost:8080/api/v1/payment/success");

        JSONObject taxAmountBody = new JSONObject();
        taxAmountBody.put("value", data.get("amount"));
        taxAmountBody.put("currency", "INR");

        JSONObject userInfoBody = new JSONObject();
        userInfoBody.put("custId", data.get("customerId"));

        paytmPaymentBody.put("txnAmount", taxAmountBody);
        paytmPaymentBody.put("userInfo", userInfoBody);

        try {
            String paytmChecksum = PaytmChecksum.generateSignature(paytmPaymentBody.toString(), AppConfigurations.MERCHANT_KEY);

            JSONObject paytmPaymentHead = new JSONObject();
            paytmPaymentHead.put("signature", paytmChecksum);

            JSONObject paytmParams = new JSONObject();
            paytmParams.put("head", paytmPaymentHead);
            paytmParams.put("body", paytmPaymentBody);

            String postPaytmParams = paytmParams.toString();
            URL paytmUrl = new URL(AppConfigurations.PAYTM_URI + "?mid=" + AppConfigurations.MERCHANT_ID + "&orderId=" + orderId);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(paytmParams.toMap(), httpHeaders);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> httpResponse = restTemplate.postForEntity(paytmUrl.toString(), httpEntity, Map.class);

            response = httpResponse.getBody();
            response.put("orderId", orderId);
            response.put("amount", taxAmountBody.get("value"));

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return response;
    }
}
