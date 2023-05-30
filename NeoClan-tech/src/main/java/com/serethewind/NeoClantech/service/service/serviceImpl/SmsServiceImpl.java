package com.serethewind.NeoClantech.service.service.serviceImpl;

import com.serethewind.NeoClantech.dto.SMSDetails;
import com.serethewind.NeoClantech.service.SmsService;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsServiceImpl implements SmsService {

//    @Value("${vonage.api-key}")
//    private String apiKey;
//
//    @Value("${vonage.api-secret}")
//    private String apiSecret;
//    private final VonageClient vonageClient;
//
//    public SmsServiceImpl(VonageClient vonageClient) {
//        this.vonageClient = vonageClient;
//    }

//    VonageClient client = VonageClient.builder().apiKey(apiKey).apiSecret(apiSecret).build();

    VonageClient client = VonageClient.builder().apiKey("5593f55d").apiSecret("w6KdBu6ZgSoNREhwnsvgCTRxJN29DdoIVHTdF6GslYWlYCXQoG").build();
    @Value("${twilio.accountSid}")
    private String accountSid;
    @Value("${twilio.authToken}")
    private String authToken;
    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;

    @Override
    public String sendSMS(SMSDetails smsDetails) {
        try {
            Twilio.init(accountSid, authToken);

            Message.creator(new PhoneNumber(smsDetails.getRecipientNumber()), new PhoneNumber(twilioPhoneNumber), smsDetails.getTextBody()).create();
            return "Message successfully sent";

        } catch (Exception e) {
            throw new RuntimeException("Failed to send sms" + e.getMessage());
        }

    }
}
//        TextMessage textMessage = new TextMessage("NeoClan Tech", smsDetails.getRecipientNumber(), smsDetails.getTextBody());
//        try {
//            SmsSubmissionResponse response = client.getSmsClient().submitMessage(textMessage);
//
//            if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
//                System.out.println("Message sent successfully.");
//                return "Message sent successfully.";
//            } else {
//                System.out.println("Message failed to send due to error");
//                return "Message failed with error: " + response.getMessages().get(0).getErrorText();
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to send sms" + e.getMessage());
//        }

