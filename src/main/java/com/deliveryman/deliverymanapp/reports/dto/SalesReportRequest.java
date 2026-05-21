package com.deliveryman.deliverymanapp.reports.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SalesReportRequest {

    @NotBlank(message = "fromDate is required")
    private String fromDate;   // format: yyyy-MM-dd HH:mm

    @NotBlank(message = "toDate is required")
    private String toDate;     // format: yyyy-MM-dd HH:mm
}
