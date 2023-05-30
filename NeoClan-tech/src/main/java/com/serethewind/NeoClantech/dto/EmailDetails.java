package com.serethewind.NeoClantech.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailDetails {

    private String recipient;
    private String messageBody;
    private String subject;
    private String attachment;
}
