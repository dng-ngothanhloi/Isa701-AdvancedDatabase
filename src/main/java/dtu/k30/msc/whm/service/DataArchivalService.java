package dtu.k30.msc.whm.service;

import dtu.k30.msc.whm.domain.PhieuNhapXuat;
import dtu.k30.msc.whm.domain.ChiTietNhapXuat;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Service for managing data archival strategy.
 * Handles moving old data to archived collections with compression.
 */
@Service
public class DataArchivalService {

    private static final Logger log = LoggerFactory.getLogger(DataArchivalService.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DataCompressionService compressionService;

    // Archival configuration
    private static final int ARCHIVAL_THRESHOLD_DAYS = 3 * 365; // 3 years
    private static final String ARCHIVED_COLLECTION_SUFFIX = "Archived";
    private static final int BATCH_SIZE = 100;

    /**
     * Archive old PhieuNhapXuat documents to compressed archived collection.
     */
    public void archiveOldPhieuNhapXuat() {
        log.info("Starting archival of old PhieuNhapXuat documents...");
        
        LocalDate cutoffDate = LocalDate.now().minusDays(ARCHIVAL_THRESHOLD_DAYS);
        Date cutoffDateAsDate = Date.from(cutoffDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        Query query = Query.query(Criteria.where("ngay_lap_phieu").lt(cutoffDateAsDate));
        query.limit(BATCH_SIZE);
        
        List<PhieuNhapXuat> oldPhieu = mongoTemplate.find(query, PhieuNhapXuat.class);
        
        if (oldPhieu.isEmpty()) {
            log.info("No old PhieuNhapXuat documents found for archival");
            return;
        }
        
        log.info("Found {} old PhieuNhapXuat documents to archive", oldPhieu.size());
        
        for (PhieuNhapXuat phieu : oldPhieu) {
            try {
                archivePhieuNhapXuat(phieu);
            } catch (Exception e) {
                log.error("Failed to archive PhieuNhapXuat: {}", phieu.getId(), e);
            }
        }
        
        log.info("Archival of {} PhieuNhapXuat documents completed", oldPhieu.size());
    }

    /**
     * Archive a single PhieuNhapXuat document with minimal embedded data.
     */
    private void archivePhieuNhapXuat(PhieuNhapXuat phieu) {
        Document archivedDoc = new Document()
            .append("_id", phieu.getId())
            .append("ma_phieu", phieu.getMaPhieu())
            .append("ngay_lap_phieu", phieu.getNgayLapPhieu())
            .append("loai_phieu", phieu.getLoaiPhieu())
            .append("archived_date", new Date())
            .append("original_collection", "PhieuNhapXuat");

        // Add minimal customer information
        if (phieu.getKhachHang() != null) {
            archivedDoc.append("khach_hang", new Document()
                .append("id", phieu.getKhachHang().getId())
                .append("maKH", phieu.getKhachHang().getMaKH())
                .append("tenKH", phieu.getKhachHang().getTenKH())
            );
        }

        // Save to archived collection
        mongoTemplate.save(archivedDoc, "PhieuNhapXuat" + ARCHIVED_COLLECTION_SUFFIX);
        
        // Remove from main collection
        mongoTemplate.remove(phieu);
        
        log.debug("Archived PhieuNhapXuat: {} -> PhieuNhapXuatArchived", phieu.getId());
    }

    /**
     * Archive old ChiTietNhapXuat documents to compressed archived collection.
     */
    public void archiveOldChiTietNhapXuat() {
        log.info("Starting archival of old ChiTietNhapXuat documents...");
        
        LocalDate cutoffDate = LocalDate.now().minusDays(ARCHIVAL_THRESHOLD_DAYS);
        Date cutoffDateAsDate = Date.from(cutoffDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        // Find ChiTietNhapXuat documents with old PhieuNhapXuat
        Query query = Query.query(
            Criteria.where("phieu_nhap_xuat.ngay_lap_phieu").lt(cutoffDateAsDate)
        );
        query.limit(BATCH_SIZE);
        
        List<ChiTietNhapXuat> oldChiTiet = mongoTemplate.find(query, ChiTietNhapXuat.class);
        
        if (oldChiTiet.isEmpty()) {
            log.info("No old ChiTietNhapXuat documents found for archival");
            return;
        }
        
        log.info("Found {} old ChiTietNhapXuat documents to archive", oldChiTiet.size());
        
        for (ChiTietNhapXuat chiTiet : oldChiTiet) {
            try {
                archiveChiTietNhapXuat(chiTiet);
            } catch (Exception e) {
                log.error("Failed to archive ChiTietNhapXuat: {}", chiTiet.getId(), e);
            }
        }
        
        log.info("Archival of {} ChiTietNhapXuat documents completed", oldChiTiet.size());
    }

    /**
     * Archive a single ChiTietNhapXuat document with minimal embedded data.
     */
    private void archiveChiTietNhapXuat(ChiTietNhapXuat chiTiet) {
        Document archivedDoc = new Document()
            .append("_id", chiTiet.getId())
            .append("so_luong", chiTiet.getSoLuong())
            .append("don_gia", chiTiet.getDonGia())
            .append("archived_date", new Date())
            .append("original_collection", "ChiTietNhapXuat");

        // Add minimal PhieuNhapXuat information
        if (chiTiet.getPhieuNhapXuat() != null) {
            archivedDoc.append("phieu_nhap_xuat", new Document()
                .append("id", chiTiet.getPhieuNhapXuat().getId())
                .append("ma_phieu", chiTiet.getPhieuNhapXuat().getMaPhieu())
                .append("ngay_lap_phieu", chiTiet.getPhieuNhapXuat().getNgayLapPhieu())
                .append("loai_phieu", chiTiet.getPhieuNhapXuat().getLoaiPhieu())
            );
        }

        // Add minimal product information
        if (chiTiet.getMaHang() != null) {
            archivedDoc.append("ma_hang", new Document()
                .append("id", chiTiet.getMaHang().getId())
                .append("ma_hang", chiTiet.getMaHang().getMaHang())
                .append("ten_hang", chiTiet.getMaHang().getTenHang())
                .append("don_vi_tinh", chiTiet.getMaHang().getDonviTinh())
            );
        }

        // Save to archived collection
        mongoTemplate.save(archivedDoc, "ChiTietNhapXuat" + ARCHIVED_COLLECTION_SUFFIX);
        
        // Remove from main collection
        mongoTemplate.remove(chiTiet);
        
        log.debug("Archived ChiTietNhapXuat: {} -> ChiTietNhapXuatArchived", chiTiet.getId());
    }

    /**
     * Configure compression for archived collections.
     */
    public void configureArchivedCollectionsCompression() {
        log.info("Configuring compression for archived collections...");
        
        // Configure compression for archived collections
        compressionService.configureCompression();
        
        // Create optimized indexes for archived collections
        mongoTemplate.getCollection("PhieuNhapXuatArchived").createIndex(
            new Document("ngay_lap_phieu", 1).append("loai_phieu", 1)
        );
        
        mongoTemplate.getCollection("ChiTietNhapXuatArchived").createIndex(
            new Document("phieu_nhap_xuat.ngay_lap_phieu", 1)
                .append("phieu_nhap_xuat.loai_phieu", 1)
        );
        
        log.info("Archived collections compression configured");
    }

    /**
     * Get archival statistics and metrics.
     */
    public Document getArchivalStats() {
        Document stats = new Document();
        
        // Get main collection stats
        long phieuNhapXuatCount = mongoTemplate.getCollection("PhieuNhapXuat").countDocuments();
        long chiTietNhapXuatCount = mongoTemplate.getCollection("ChiTietNhapXuat").countDocuments();
        
        // Get archived collection stats
        long archivedPhieuNhapXuatCount = mongoTemplate.getCollection("PhieuNhapXuatArchived").countDocuments();
        long archivedChiTietNhapXuatCount = mongoTemplate.getCollection("ChiTietNhapXuatArchived").countDocuments();
        
        // Calculate archival ratios
        double phieuNhapXuatArchivalRatio = (phieuNhapXuatCount + archivedPhieuNhapXuatCount) > 0 ?
            (double) archivedPhieuNhapXuatCount / (phieuNhapXuatCount + archivedPhieuNhapXuatCount) : 0;
        
        double chiTietNhapXuatArchivalRatio = (chiTietNhapXuatCount + archivedChiTietNhapXuatCount) > 0 ?
            (double) archivedChiTietNhapXuatCount / (chiTietNhapXuatCount + archivedChiTietNhapXuatCount) : 0;
        
        return stats
            .append("activePhieuNhapXuat", phieuNhapXuatCount)
            .append("archivedPhieuNhapXuat", archivedPhieuNhapXuatCount)
            .append("phieuNhapXuatArchivalRatio", phieuNhapXuatArchivalRatio)
            .append("activeChiTietNhapXuat", chiTietNhapXuatCount)
            .append("archivedChiTietNhapXuat", archivedChiTietNhapXuatCount)
            .append("chiTietNhapXuatArchivalRatio", chiTietNhapXuatArchivalRatio)
            .append("archivalThresholdDays", ARCHIVAL_THRESHOLD_DAYS)
            .append("timestamp", System.currentTimeMillis());
    }

    /**
     * Restore archived documents to main collections (for testing/debugging).
     */
    public void restoreArchivedDocuments(String documentId, String collectionType) {
        log.info("Restoring archived document: {} from {}", documentId, collectionType);
        
        try {
            if ("PhieuNhapXuat".equals(collectionType)) {
                restorePhieuNhapXuat(documentId);
            } else if ("ChiTietNhapXuat".equals(collectionType)) {
                restoreChiTietNhapXuat(documentId);
            }
        } catch (Exception e) {
            log.error("Failed to restore archived document: {}", documentId, e);
            throw e;
        }
    }

    /**
     * Restore a PhieuNhapXuat document from archived collection.
     */
    private void restorePhieuNhapXuat(String documentId) {
        Document archivedDoc = mongoTemplate.findById(documentId, Document.class, "PhieuNhapXuatArchived");
        
        if (archivedDoc != null) {
            // Remove archival-specific fields
            archivedDoc.remove("archived_date");
            archivedDoc.remove("original_collection");
            
            // Save to main collection
            mongoTemplate.save(archivedDoc, "PhieuNhapXuat");
            
            // Remove from archived collection
            mongoTemplate.remove(archivedDoc, "PhieuNhapXuatArchived");
            
            log.info("Restored PhieuNhapXuat: {} from archived collection", documentId);
        }
    }

    /**
     * Restore a ChiTietNhapXuat document from archived collection.
     */
    private void restoreChiTietNhapXuat(String documentId) {
        Document archivedDoc = mongoTemplate.findById(documentId, Document.class, "ChiTietNhapXuatArchived");
        
        if (archivedDoc != null) {
            // Remove archival-specific fields
            archivedDoc.remove("archived_date");
            archivedDoc.remove("original_collection");
            
            // Save to main collection
            mongoTemplate.save(archivedDoc, "ChiTietNhapXuat");
            
            // Remove from archived collection
            mongoTemplate.remove(archivedDoc, "ChiTietNhapXuatArchived");
            
            log.info("Restored ChiTietNhapXuat: {} from archived collection", documentId);
        }
    }

    /**
     * Scheduled archival task - runs daily at 2 AM.
     */
    @Scheduled(cron = "0 0 2 1 * ?") // 2h sáng ngày 1 mỗi tháng
    public void scheduledArchival() {
        log.info("Running scheduled archival task...");
        
        try {
            archiveOldPhieuNhapXuat();
            archiveOldChiTietNhapXuat();
            
            // Monitor archival performance
            Document stats = getArchivalStats();
            log.info("Archival stats: {}", stats.toJson());
            
        } catch (Exception e) {
            log.error("Scheduled archival task failed", e);
        }
    }

    /**
     * Monitor archival performance and log metrics.
     */
    public void monitorArchivalPerformance() {
        Document stats = getArchivalStats();
        
        log.info("Archival Performance Metrics:");
        log.info("  Active PhieuNhapXuat: {}", stats.getLong("activePhieuNhapXuat"));
        log.info("  Archived PhieuNhapXuat: {}", stats.getLong("archivedPhieuNhapXuat"));
        log.info("  PhieuNhapXuat Archival Ratio: {:.2f}%", stats.getDouble("phieuNhapXuatArchivalRatio") * 100);
        log.info("  Active c: {}", stats.getLong("activeChiTietNhapXuat"));
        log.info("  Archived ChiTietNhapXuat: {}", stats.getLong("archivedChiTietNhapXuat"));
        log.info("  ChiTietNhapXuat Archival Ratio: {:.2f}%", stats.getDouble("chiTietNhapXuatArchivalRatio") * 100);
        
        // Alert if archival ratio is too high
        if (stats.getDouble("phieuNhapXuatArchivalRatio") > 0.8) {
            log.warn("PhieuNhapXuat archival ratio is high: {:.2f}%", 
                stats.getDouble("phieuNhapXuatArchivalRatio") * 100);
        }
    }
} 