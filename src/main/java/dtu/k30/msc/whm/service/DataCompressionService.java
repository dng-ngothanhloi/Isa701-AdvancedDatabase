package dtu.k30.msc.whm.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Service for managing MongoDB data compression and storage optimization.
 * Handles compression configuration, monitoring, and performance analysis.
 */
@Service
public class DataCompressionService {

    private static final Logger log = LoggerFactory.getLogger(DataCompressionService.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoClient mongoClient;

    private static final List<String> COMPRESSIBLE_COLLECTIONS = Arrays.asList(
        "PhieuNhapXuat", "ChiTietNhapXuat", "KhachHang", "DanhMucHang"
    );

    /**
     * Configure compression for all collections using WiredTiger Snappy compression.
     */
    public void configureCompression() {
        log.info("Configuring MongoDB compression for collections: {}", COMPRESSIBLE_COLLECTIONS);
        
        MongoDatabase database = mongoClient.getDatabase("warehouse-mgmt");
        
        for (String collectionName : COMPRESSIBLE_COLLECTIONS) {
            try {
                configureCollectionCompression(database, collectionName);
                log.info("Compression configured for collection: {}", collectionName);
            } catch (Exception e) {
                log.error("Failed to configure compression for collection: {}", collectionName, e);
            }
        }
    }

    /**
     * Configure compression for a specific collection.
     */
    private void configureCollectionCompression(MongoDatabase database, String collectionName) {
        Document collModCommand = new Document()
            .append("collMod", collectionName)
            .append("storageEngine", new Document()
                .append("wiredTiger", new Document()
                    .append("configString", "block_compressor=snappy")
                )
            );

        // Add validation schema for embedded documents
        if ("PhieuNhapXuat".equals(collectionName)) {
            collModCommand.append("validator", createPhieuNhapXuatValidator());
        } else if ("ChiTietNhapXuat".equals(collectionName)) {
            collModCommand.append("validator", createChiTietNhapXuatValidator());
        }

        database.runCommand(collModCommand);
    }

    /**
     * Create validation schema for PhieuNhapXuat with optimized embedded structure.
     */
    private Document createPhieuNhapXuatValidator() {
        return new Document()
            .append("$jsonSchema", new Document()
                .append("bsonType", "object")
                .append("properties", new Document()
                    .append("_id", new Document("bsonType", "objectId"))
                    .append("ma_phieu", new Document("bsonType", "string"))
                    .append("ngay_lap_phieu", new Document("bsonType", "date"))
                    .append("loai_phieu", new Document("bsonType", "string"))
                    .append("khach_hang", new Document()
                        .append("bsonType", "object")
                        .append("properties", new Document()
                            .append("id", new Document("bsonType", "string"))
                            .append("maKH", new Document("bsonType", "string"))
                            .append("tenKH", new Document("bsonType", "string"))
                        )
                        .append("required", Arrays.asList("id", "maKH", "tenKH"))
                    )
                )
                .append("required", Arrays.asList("ma_phieu", "ngay_lap_phieu", "loai_phieu"))
            );
    }

    /**
     * Create validation schema for ChiTietNhapXuat with optimized embedded structure.
     */
    private Document createChiTietNhapXuatValidator() {
        return new Document()
            .append("$jsonSchema", new Document()
                .append("bsonType", "object")
                .append("properties", new Document()
                    .append("_id", new Document("bsonType", "objectId"))
                    .append("so_luong", new Document("bsonType", "int"))
                    .append("don_gia", new Document("bsonType", "decimal"))
                    .append("phieu_nhap_xuat", new Document()
                        .append("bsonType", "object")
                        .append("properties", new Document()
                            .append("id", new Document("bsonType", "string"))
                            .append("ma_phieu", new Document("bsonType", "string"))
                            .append("ngay_lap_phieu", new Document("bsonType", "date"))
                            .append("loai_phieu", new Document("bsonType", "string"))
                        )
                        .append("required", Arrays.asList("id", "ma_phieu", "ngay_lap_phieu", "loai_phieu"))
                    )
                    .append("ma_hang", new Document()
                        .append("bsonType", "object")
                        .append("properties", new Document()
                            .append("id", new Document("bsonType", "string"))
                            .append("ma_hang", new Document("bsonType", "string"))
                            .append("ten_hang", new Document("bsonType", "string"))
                            .append("don_vi_tinh", new Document("bsonType", "string"))
                        )
                        .append("required", Arrays.asList("id", "ma_hang", "ten_hang", "don_vi_tinh"))
                    )
                )
                .append("required", Arrays.asList("so_luong", "don_gia"))
            );
    }

    /**
     * Analyze compression effectiveness for all collections.
     */
    public Document analyzeCompressionEffectiveness() {
        log.info("Analyzing compression effectiveness...");
        
        Document analysis = new Document();
        MongoDatabase database = mongoClient.getDatabase("warehouse-mgmt");
        
        for (String collectionName : COMPRESSIBLE_COLLECTIONS) {
            try {
                Document collectionStats = database.runCommand(new Document("collStats", collectionName));
                Document collectionAnalysis = analyzeCollectionCompression(collectionStats);
                analysis.append(collectionName, collectionAnalysis);
            } catch (Exception e) {
                log.error("Failed to analyze compression for collection: {}", collectionName, e);
            }
        }
        
        return analysis;
    }

    /**
     * Analyze compression for a specific collection.
     */
    private Document analyzeCollectionCompression(Document stats) {
        long dataSize = stats.getLong("size");
        long storageSize = stats.getLong("storageSize");
        long count = stats.getLong("count");
        
        double compressionRatio = dataSize > 0 ? (double) (dataSize - storageSize) / dataSize : 0;
        double avgDocumentSize = count > 0 ? (double) dataSize / count : 0;
        double avgStorageSize = count > 0 ? (double) storageSize / count : 0;
        
        return new Document()
            .append("documentCount", count)
            .append("dataSizeMB", dataSize / (1024 * 1024))
            .append("storageSizeMB", storageSize / (1024 * 1024))
            .append("avgDocumentSizeKB", avgDocumentSize / 1024)
            .append("avgStorageSizeKB", avgStorageSize / 1024)
            .append("compressionRatio", compressionRatio)
            .append("compressionSavingsMB", (dataSize - storageSize) / (1024 * 1024));
    }

    /**
     * Get compression statistics for monitoring.
     */
    public Document getCompressionStats() {
        Document stats = new Document();
        MongoDatabase database = mongoClient.getDatabase("warehouse-mgmt");
        
        long totalDataSize = 0;
        long totalStorageSize = 0;
        long totalDocuments = 0;
        
        for (String collectionName : COMPRESSIBLE_COLLECTIONS) {
            try {
                Document collectionStats = database.runCommand(new Document("collStats", collectionName));
                totalDataSize += collectionStats.getLong("size");
                totalStorageSize += collectionStats.getLong("storageSize");
                totalDocuments += collectionStats.getLong("count");
            } catch (Exception e) {
                log.error("Failed to get stats for collection: {}", collectionName, e);
            }
        }
        
        double overallCompressionRatio = totalDataSize > 0 ? 
            (double) (totalDataSize - totalStorageSize) / totalDataSize : 0;
        
        return stats
            .append("totalDocuments", totalDocuments)
            .append("totalDataSizeMB", totalDataSize / (1024 * 1024))
            .append("totalStorageSizeMB", totalStorageSize / (1024 * 1024))
            .append("overallCompressionRatio", overallCompressionRatio)
            .append("totalSavingsMB", (totalDataSize - totalStorageSize) / (1024 * 1024))
            .append("timestamp", System.currentTimeMillis());
    }

    /**
     * Monitor compression performance and log metrics.
     */
    public void monitorCompressionPerformance() {
        Document stats = getCompressionStats();
        
        log.info("Compression Performance Metrics:");
        log.info("  Total Documents: {}", stats.getLong("totalDocuments"));
        log.info("  Total Data Size: {} MB", stats.getDouble("totalDataSizeMB"));
        log.info("  Total Storage Size: {} MB", stats.getDouble("totalStorageSizeMB"));
        log.info("  Compression Ratio: {:.2f}%", stats.getDouble("overallCompressionRatio") * 100);
        log.info("  Total Savings: {:.2f} MB", stats.getDouble("totalSavingsMB"));
        
        // Alert if compression ratio is below threshold
        if (stats.getDouble("overallCompressionRatio") < 0.2) {
            log.warn("Compression ratio below 20% threshold: {:.2f}%", 
                stats.getDouble("overallCompressionRatio") * 100);
        }
    }

    /**
     * Optimize indexes for compressed collections.
     */
    public void optimizeIndexesForCompression() {
        log.info("Optimizing indexes for compressed collections...");
        
        // Create compound indexes for better compression
        mongoTemplate.getCollection("PhieuNhapXuat").createIndex(
            new Document("ngay_lap_phieu", 1).append("loai_phieu", 1)
        );
        
        mongoTemplate.getCollection("ChiTietNhapXuat").createIndex(
            new Document("phieu_nhap_xuat.ngay_lap_phieu", 1)
                .append("phieu_nhap_xuat.loai_phieu", 1)
                .append("ma_hang.id", 1)
        );
        
        log.info("Index optimization completed");
    }
} 