package com.deepawasthi.SpringPaymentPaytm.Controllers;

import com.deepawasthi.SpringPaymentPaytm.Services.PaytmServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/payment")
public class PaymentGatewayController {

    @Autowired
    PaytmServicesImpl paytmServices;

    @PostMapping("/paytm")
    public Map<String, Object> paytmPaymentStart(@RequestBody Map<String, Object> data){
        return paytmServices.payment(data);
    }

    @GetMapping("/success")
    public String paymentSuccess(){
        return "Payment Done Successfully";
    }
}
