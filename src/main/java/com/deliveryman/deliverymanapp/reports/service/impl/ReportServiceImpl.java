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
}
