package com.deliveryman.deliverymanapp.reports.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesReportRowDto {

    @JsonProperty("s")
    private Integer s;

    @JsonProperty("storeNameA")
    private String storeNameA;

    @JsonProperty("sales")
    private BigDecimal sales;

    @JsonProperty("percent")
    private BigDecimal percent;

    @JsonProperty("cash")
    private BigDecimal cash;

    @JsonProperty("visa")
    private BigDecimal visa;

    @JsonProperty("instaPay")
    private BigDecimal instaPay;
}
