package com.deliveryman.deliverymanapp.reports.service;

import com.deliveryman.deliverymanapp.reports.dto.SalesReportRequest;
import com.deliveryman.deliverymanapp.reports.dto.SalesReportRowDto;

import java.util.List;

public interface ReportService {

    List<SalesReportRowDto> getSalesReport(SalesReportRequest request);
    
    List<com.deliveryman.deliverymanapp.reports.dto.BranchDto> getBranches();
    
    List<com.deliveryman.deliverymanapp.reports.dto.SalesByProductReportRowDto> getSalesByProductReport(com.deliveryman.deliverymanapp.reports.dto.SalesByProductReportRequest request);
}
