package com.deliveryman.deliverymanapp.reports.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SalesByProductReportRequest {

    @NotNull(message = "storeCode is required")
    private Integer storeCode;

    @NotBlank(message = "fromDate is required")
    private String fromDate;

    @NotBlank(message = "toDate is required")
    private String toDate;

    @NotNull(message = "count is required")
    private Integer count;
}
