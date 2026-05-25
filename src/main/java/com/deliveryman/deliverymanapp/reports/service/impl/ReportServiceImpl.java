package com.deliveryman.deliverymanapp.reports.service.impl;

import com.deliveryman.deliverymanapp.reports.dto.SalesReportRequest;
import com.deliveryman.deliverymanapp.reports.dto.SalesReportRowDto;
import com.deliveryman.deliverymanapp.reports.service.ReportService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final JdbcTemplate jdbcTemplate;

    public ReportServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<SalesReportRowDto> getSalesReport(SalesReportRequest request) {
        String sql = "EXEC upr_POS_SalesReport_ToDate ?, ?";
        return jdbcTemplate.query(sql, new SalesReportRowMapper(),
                request.getFromDate(),
                request.getToDate());
    }

    // RowMapper لتحويل كل صف من الـ ResultSet إلى SalesReportRowDto
    private static class SalesReportRowMapper implements RowMapper<SalesReportRowDto> {
        @Override
        public SalesReportRowDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return SalesReportRowDto.builder()
                    .s(getIntSafe(rs, "S"))
                    .storeNameA(rs.getString("Store_NameA"))
                    .sales(getBigDecimalSafe(rs, "Sales"))
                    .percent(getBigDecimalSafe(rs, "Percent"))
                    .cash(getBigDecimalSafe(rs, "Cash"))
                    .visa(getBigDecimalSafe(rs, "VISA"))
                    .instaPay(getBigDecimalSafe(rs, "InstaPay"))
                    .build();
        }

        private Integer getIntSafe(ResultSet rs, String col) throws SQLException {
            int val = rs.getInt(col);
            return rs.wasNull() ? null : val;
        }

        private BigDecimal getBigDecimalSafe(ResultSet rs, String col) throws SQLException {
            BigDecimal val = rs.getBigDecimal(col);
            return val != null ? val : BigDecimal.ZERO;
        }
    }

    @Override
    public List<com.deliveryman.deliverymanapp.reports.dto.BranchDto> getBranches() {
        String sql = "SELECT Code, NameA FROM vb_Coding_Store";
        return jdbcTemplate.query(sql, (rs, rowNum) -> 
            com.deliveryman.deliverymanapp.reports.dto.BranchDto.builder()
                .code(rs.getInt("Code"))
                .nameA(rs.getString("NameA"))
                .build()
        );
    }

    @Override
    public List<com.deliveryman.deliverymanapp.reports.dto.SalesByProductReportRowDto> getSalesByProductReport(
            com.deliveryman.deliverymanapp.reports.dto.SalesByProductReportRequest request) {
        String sql = "EXEC [upr_POS_SalesReport_Products] ?, ?, ?, ?";
        return jdbcTemplate.query(sql, new SalesByProductReportRowMapper(),
                request.getStoreCode(),
                request.getFromDate(),
                request.getToDate(),
                request.getCount());
    }

    private static class SalesByProductReportRowMapper implements RowMapper<com.deliveryman.deliverymanapp.reports.dto.SalesByProductReportRowDto> {
        @Override
        public com.deliveryman.deliverymanapp.reports.dto.SalesByProductReportRowDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return com.deliveryman.deliverymanapp.reports.dto.SalesByProductReportRowDto.builder()
                    .storeNameA(rs.getString("Store_NameA"))
                    .productNameA(rs.getString("Product_NameA"))
                    .qty(getBigDecimalSafe(rs, "Qty"))
                    .sales(getBigDecimalSafe(rs, "Sales"))
                    .p(getBigDecimalSafe(rs, "P"))
                    .build();
        }

        private BigDecimal getBigDecimalSafe(ResultSet rs, String col) throws SQLException {
            BigDecimal val = rs.getBigDecimal(col);
            return val != null ? val : BigDecimal.ZERO;
        }
    }
}
