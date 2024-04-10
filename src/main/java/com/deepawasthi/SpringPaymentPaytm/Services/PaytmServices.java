package com.deepawasthi.SpringPaymentPaytm.Services;

import java.util.Map;

public interface PaytmServices {

    public Map<String, Object> payment(Map<String, Object> data);
}
