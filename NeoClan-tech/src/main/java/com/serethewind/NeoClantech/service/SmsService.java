package com.serethewind.NeoClantech.service;

import com.serethewind.NeoClantech.dto.SMSDetails;

public interface SmsService {
    String sendSMS(SMSDetails smsDetails);
}
