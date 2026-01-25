package com.atparui.rmsservice.service;
import java.util.Optional;

import com.atparui.rmsservice.repository.OrderRepository;
import com.atparui.rmsservice.service.dto.DailySummaryReportDTO;
import com.atparui.rmsservice.service.dto.SalesReportDTO;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Service Implementation for generating reports.
 */
@Service
@Transactional
public class ReportService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportService.class);

    private final OrderRepository orderRepository;

    public ReportService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        // TODO: All reactive methods commented out for compilation. Reimplement as blocking JDBC/JPA.
    }

    /**
     * Get sales report for a branch and date range
     *
     * @param branchId the branch ID
     * @param startDate the start date
     * @param endDate the end date
     * @return the sales report DTO
     */
    @Transactional(readOnly = true)
    public Optional<SalesReportDTO> getSalesReport(UUID branchId, Instant startDate, Instant endDate) {
        LOG.debug("Request to get sales report : {} - {} to {}", branchId, startDate, endDate);
        var orders = orderRepository.findByBranchIdAndOrderDateBetween(branchId, startDate, endDate);
        
        SalesReportDTO report = new SalesReportDTO();
        report.setBranchId(branchId);
        report.setStartDate(startDate);
        report.setEndDate(endDate);

        BigDecimal totalSales = orders
            .stream()
            .map(order -> order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        report.setTotalSales(totalSales);
        report.setTotalOrders(orders.size());

        if (!orders.isEmpty()) {
            report.setAverageOrderValue(totalSales.divide(new BigDecimal(orders.size()), 2, java.math.RoundingMode.HALF_UP));
        } else {
            report.setAverageOrderValue(BigDecimal.ZERO);
        }

        // Sales by category (simplified - would need to join with menu items)
        report.setSalesByCategory(new java.util.ArrayList<>());

        // Sales by day (simplified - would need to group by day)
        report.setSalesByDay(new java.util.ArrayList<>());

        return Optional.of(report);
    }

    /**
     * Get daily summary report
     *
     * @param branchId the branch ID
     * @param date the date
     * @return the daily summary report DTO
     */
    @Transactional(readOnly = true)
    public Optional<DailySummaryReportDTO> getDailySummary(UUID branchId, LocalDate date) {
        LOG.debug("Request to get daily summary : {} - {}", branchId, date);
        Instant startOfDay = date.atStartOfDay().toInstant(java.time.ZoneOffset.UTC);
        Instant endOfDay = date.plusDays(1).atStartOfDay().toInstant(java.time.ZoneOffset.UTC);

        var orders = orderRepository.findByBranchIdAndOrderDateBetween(branchId, startOfDay, endOfDay);
        
        DailySummaryReportDTO summary = new DailySummaryReportDTO();
        summary.setBranchId(branchId);
        summary.setDate(date);

        BigDecimal totalSales = orders
            .stream()
            .map(order -> order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        summary.setTotalSales(totalSales);
        summary.setTotalOrders(orders.size());

        // Count unique customers
        long uniqueCustomers = orders.stream().map(order -> order.getCustomerId()).filter(id -> id != null).distinct().count();
        summary.setTotalCustomers((int) uniqueCustomers);

        // Calculate average order value
        if (!orders.isEmpty()) {
            summary.setAverageOrderValue(totalSales.divide(new BigDecimal(orders.size()), 2, java.math.RoundingMode.HALF_UP));
        } else {
            summary.setAverageOrderValue(BigDecimal.ZERO);
        }

        // Calculate totals
        BigDecimal totalTax = orders
            .stream()
            .map(order -> order.getTaxAmount() != null ? order.getTaxAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        summary.setTotalTax(totalTax);

        BigDecimal totalDiscount = orders
            .stream()
            .map(order -> order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        summary.setTotalDiscount(totalDiscount);

        summary.setNetSales(totalSales.subtract(totalDiscount));

        return Optional.of(summary);
    }
}
