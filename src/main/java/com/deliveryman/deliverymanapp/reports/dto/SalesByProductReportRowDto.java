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
public class SalesByProductReportRowDto {

    @JsonProperty("storeNameA")
    private String storeNameA;

    @JsonProperty("productNameA")
    private String productNameA;

    @JsonProperty("qty")
    private BigDecimal qty;

    @JsonProperty("sales")
    private BigDecimal sales;

    @JsonProperty("p")
    private BigDecimal p;
}
