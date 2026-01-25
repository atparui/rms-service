package com.atparui.rmsservice.web.rest;
import java.util.Optional;

import com.atparui.rmsservice.service.ReportService;
import com.atparui.rmsservice.service.dto.DailySummaryReportDTO;
import com.atparui.rmsservice.service.dto.SalesReportDTO;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing reports.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReportResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportService reportService;

    public ReportResource(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * {@code GET /api/reports/sales} : Get sales report
     *
     * @param branchId the branch ID
     * @param startDate the start date
     * @param endDate the end date
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and sales report
     */
    @GetMapping("/sales")
    public ResponseEntity<SalesReportDTO> getSalesReport(
        @RequestParam UUID branchId,
        @RequestParam @org.springframework.format.annotation.DateTimeFormat(
            iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME
        ) Instant startDate,
        @RequestParam @org.springframework.format.annotation.DateTimeFormat(
            iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME
        ) Instant endDate
    ) {
        LOG.debug("REST request to get sales report : {} - {} to {}", branchId, startDate, endDate);
        Optional<SalesReportDTO> result = reportService.getSalesReport(branchId, startDate, endDate);
        return ResponseEntity.ok().body(result.orElse(new SalesReportDTO()));
    }

    /**
     * {@code GET /api/reports/daily-summary} : Get daily summary report
     *
     * @param branchId the branch ID
     * @param date the date
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and daily summary
     */
    @GetMapping("/daily-summary")
    public ResponseEntity<DailySummaryReportDTO> getDailySummary(
        @RequestParam UUID branchId,
        @RequestParam @org.springframework.format.annotation.DateTimeFormat(
            iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE
        ) LocalDate date
    ) {
        LOG.debug("REST request to get daily summary : {} - {}", branchId, date);
        Optional<DailySummaryReportDTO> result = reportService.getDailySummary(branchId, date);
        return ResponseEntity.ok().body(result.orElse(new DailySummaryReportDTO()));
    }
}
