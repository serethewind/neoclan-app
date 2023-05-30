package com.serethewind.NeoClantech.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SMSDetails {
    private String recipientNumber;
    private String textBody;
}
