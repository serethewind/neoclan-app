package com.serethewind.NeoClantech.dto;

import lombok.*;

@Getter
@lombok.Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response {
    private String responseCode;
    private String responseMessage;
    private Data data;
}
