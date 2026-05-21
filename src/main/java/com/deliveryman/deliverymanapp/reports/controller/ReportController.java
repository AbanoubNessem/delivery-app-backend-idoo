package com.deliveryman.deliverymanapp.reports.controller;

import com.deliveryman.deliverymanapp.common.ApiResponse;
import com.deliveryman.deliverymanapp.reports.dto.SalesReportRequest;
import com.deliveryman.deliverymanapp.reports.dto.SalesReportRowDto;
import com.deliveryman.deliverymanapp.reports.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * GET POS Sales Report
     * يحتاج JWT Token في الـ Header: Authorization: Bearer <token>
     *
     * Request Body:
     * {
     *   "fromDate": "2026-05-01",
     *   "toDate":   "2026-05-20 23:59"
     * }
     */
    @PostMapping("/pos/sales")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<SalesReportRowDto>>> getSalesReport(
            @Valid @RequestBody SalesReportRequest request
    ) {
        List<SalesReportRowDto> data = reportService.getSalesReport(request);
        return ResponseEntity.ok(ApiResponse.success(data, "Sales report retrieved successfully"));
    }
}
