package dtu.k30.msc.whm.repository.custom;

import dtu.k30.msc.whm.domain.ChiTietNhapXuat;
import dtu.k30.msc.whm.service.ChiTietNhapXuatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ChiTietNhapXuatRepositoryCustomImpl implements ChiTietNhapXuatRepositoryCustom {
    private static final Logger LOG = LoggerFactory.getLogger(ChiTietNhapXuatService.class);
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Override
    public List<ChiTietNhapXuat> findByPhieuNhapXuatCustom(String pnxId) {
        LOG.debug("Request to ChiTietNhapXuatRepositoryCustomImpl.findByPhieuNhapXuatCustom with PhieuNhapXuat.id: {}", pnxId);
        if (pnxId == null || pnxId.length() == 0) {
            return List.of();
        }
        
        // Simple query using embedded document structure
        Query query = new Query(Criteria.where("phieu_nhap_xuat.id").is(pnxId));
        return mongoTemplate.find(query, ChiTietNhapXuat.class);
    }

    /**
     * Find transaction details by customer with aggregation
     */
    public List<ChiTietNhapXuat> findByCustomerId(String customerId) {
        LOG.debug("Request to find ChiTietNhapXuat by customer ID: {}", customerId);
        
        MatchOperation matchOperation = Aggregation.match(
            Criteria.where("phieu_nhap_xuat.khach_hang.id").is(customerId)
        );
        
        Aggregation aggregation = Aggregation.newAggregation(matchOperation);
        AggregationResults<ChiTietNhapXuat> results = mongoTemplate.aggregate(
            aggregation, "ChiTietNhapXuat", ChiTietNhapXuat.class
        );
        
        return results.getMappedResults();
    }

    /**
     * Find transaction details by product with aggregation
     */
    public List<ChiTietNhapXuat> findByProductId(String productId) {
        LOG.debug("Request to find ChiTietNhapXuat by product ID: {}", productId);
        
        MatchOperation matchOperation = Aggregation.match(
            Criteria.where("ma_hang.id").is(productId)
        );
        
        Aggregation aggregation = Aggregation.newAggregation(matchOperation);
        AggregationResults<ChiTietNhapXuat> results = mongoTemplate.aggregate(
            aggregation, "ChiTietNhapXuat", ChiTietNhapXuat.class
        );
        
        return results.getMappedResults();
    }

    /**
     * Find transaction details by date range with aggregation
     */
    public List<ChiTietNhapXuat> findByDateRange(LocalDate startDate, LocalDate endDate) {
        LOG.debug("Request to find ChiTietNhapXuat by date range: {} to {}", startDate, endDate);
        
        MatchOperation matchOperation = Aggregation.match(
            Criteria.where("phieu_nhap_xuat.ngay_lap_phieu")
                .gte(startDate)
                .lte(endDate)
        );
        
        Aggregation aggregation = Aggregation.newAggregation(matchOperation);
        AggregationResults<ChiTietNhapXuat> results = mongoTemplate.aggregate(
            aggregation, "ChiTietNhapXuat", ChiTietNhapXuat.class
        );
        
        return results.getMappedResults();
    }

    /**
     * Find transaction details by voucher type with aggregation
     */
    public List<ChiTietNhapXuat> findByVoucherType(String voucherType) {
        LOG.debug("Request to find ChiTietNhapXuat by voucher type: {}", voucherType);
        
        MatchOperation matchOperation = Aggregation.match(
            Criteria.where("phieu_nhap_xuat.loai_phieu").is(voucherType)
        );
        
        Aggregation aggregation = Aggregation.newAggregation(matchOperation);
        AggregationResults<ChiTietNhapXuat> results = mongoTemplate.aggregate(
            aggregation, "ChiTietNhapXuat", ChiTietNhapXuat.class
        );
        
        return results.getMappedResults();
    }

    /**
     * Complex aggregation: Get total quantity and value by product
     */
    public List<Object> getProductSummary() {
        LOG.debug("Request to get product summary with aggregation");
        
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.group("ma_hang.id", "ma_hang.ten_hang")
                .sum("so_luong").as("totalQuantity")
                .sum("don_gia").as("totalValue")
                .avg("don_gia").as("averagePrice"),
            Aggregation.sort(org.springframework.data.domain.Sort.by("totalValue").descending())
        );
        
        AggregationResults<Object> results = mongoTemplate.aggregate(
            aggregation, "ChiTietNhapXuat", Object.class
        );
        
        return results.getMappedResults();
    }

    /**
     * Complex aggregation: Get customer transaction summary
     */
    public List<Object> getCustomerSummary() {
        LOG.debug("Request to get customer summary with aggregation");
        
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.group("phieu_nhap_xuat.khach_hang.id", "phieu_nhap_xuat.khach_hang.ten_kh")
                .sum("so_luong").as("totalQuantity")
                .sum("don_gia").as("totalValue")
                .count().as("transactionCount"),
            Aggregation.sort(org.springframework.data.domain.Sort.by("totalValue").descending())
        );
        
        AggregationResults<Object> results = mongoTemplate.aggregate(
            aggregation, "ChiTietNhapXuat", Object.class
        );
        
        return results.getMappedResults();
    }

    /**
     * Complex aggregation: Get monthly transaction summary
     */
    public List<Object> getMonthlySummary() {
        LOG.debug("Request to get monthly summary with aggregation");
        
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.project()
                .and("phieu_nhap_xuat.ngay_lap_phieu").extractYear().as("year")
                .and("phieu_nhap_xuat.ngay_lap_phieu").extractMonth().as("month")
                .and("so_luong").as("so_luong")
                .and("don_gia").as("don_gia")
                .and("phieu_nhap_xuat.loai_phieu").as("loai_phieu"),
            Aggregation.group("year", "month", "loai_phieu")
                .sum("so_luong").as("totalQuantity")
                .sum("don_gia").as("totalValue")
                .count().as("transactionCount"),
            Aggregation.sort(org.springframework.data.domain.Sort.by("year", "month").descending())
        );
        
        AggregationResults<Object> results = mongoTemplate.aggregate(
            aggregation, "ChiTietNhapXuat", Object.class
        );
        
        return results.getMappedResults();
    }
}
