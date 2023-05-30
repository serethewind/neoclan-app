package com.serethewind.NeoClantech.service;

import com.serethewind.NeoClantech.dto.EmailDetails;

public interface EmailService {

    String sendSimpleMessage(EmailDetails emailDetails);

    String sendMessageWithAttachment(EmailDetails emailDetails);
}
