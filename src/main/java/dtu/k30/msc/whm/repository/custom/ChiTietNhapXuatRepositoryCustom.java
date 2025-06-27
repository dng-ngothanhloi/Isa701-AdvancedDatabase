package dtu.k30.msc.whm.repository.custom;

import dtu.k30.msc.whm.domain.ChiTietNhapXuat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

public interface ChiTietNhapXuatRepositoryCustom {

    /**
     * Find transaction details by PhieuNhapXuat ID
     */
    public List<ChiTietNhapXuat> findByPhieuNhapXuatCustom(String pnxId);

    /**
     * Find transaction details by customer ID using aggregation
     */
    public List<ChiTietNhapXuat> findByCustomerId(String customerId);

    /**
     * Find transaction details by product ID using aggregation
     */
    public List<ChiTietNhapXuat> findByProductId(String productId);

    /**
     * Find transaction details by date range using aggregation
     */
    public List<ChiTietNhapXuat> findByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Find transaction details by voucher type using aggregation
     */
    public List<ChiTietNhapXuat> findByVoucherType(String voucherType);

    /**
     * Get product summary with aggregation (total quantity, value, average price)
     */
    public List<Object> getProductSummary();

    /**
     * Get customer summary with aggregation (total quantity, value, transaction count)
     */
    public List<Object> getCustomerSummary();

    /**
     * Get monthly transaction summary with aggregation
     */
    public List<Object> getMonthlySummary();
}
