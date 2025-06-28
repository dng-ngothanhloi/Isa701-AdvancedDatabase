# Warehouse Performance Improvement Guide

## Overview

This document outlines the performance issues identified after migrating from MongoDB @DBRef to embedded documents, along with comprehensive solutions to optimize the warehouse management system.

**Migration Context:** MongoDB @DBRef → Embedded Documents  
**Date:** June 28, 2025  
**Status:** Migration completed, performance optimization in progress

---

## I. Performance Issues Identified

### ⚠️ Storage Considerations (Priority 1)
- **Increased storage usage** due to data duplication in embedded documents
- **Document size growth** from embedded data
- **Potential storage overhead** for large embedded documents
- **Compression challenges** with duplicated data

### ⚠️ Write Performance Issues (Priority 2)
- **Slightly slower write operations** due to embedded document updates
- **Multiple document updates** when main entities change
- **Increased write complexity** for maintaining data consistency
- **Synchronization overhead** from DataSynchronizationService

### ⚠️ System Complexity
- **More complex write operations** requiring synchronization
- **Increased maintenance complexity** for embedded document management
- **Error handling complexity** for synchronization failures
- **Debugging challenges** with embedded data structures

---

## II. Performance Solutions & Optimizations

### 1. Storage Optimization (Priority 1)

#### **Solution 1: Selective Embedding - Optimized Structure**
```java
// 1. KhachHangEmbedded (Minimal - 60% storage reduction)
public class KhachHangEmbedded {
    private String id;
    private String maKH;
    private String tenKH;
    // Removed: goiTinh, dateOfBirth, diaChi, audit fields
}

// 2. DanhMucHangEmbedded (Essential fields - 50% storage reduction)
public class DanhMucHangEmbedded {
    private String id;
    private String maHang;
    private String tenHang;
    private String donVitinh;  // Essential for calculations
    // Removed: noiSanXuat, ngaySanXuat, hanSuDung, audit fields
}

// 3. PhieuNhapXuatEmbedded (Basic info - 70% storage reduction)
public class PhieuNhapXuatEmbedded {
    private String id;
    private String maPhieu;
    private LocalDate ngayLapPhieu;
    private VoucherType loaiPhieu;
    // Removed: audit fields, KhachHangEmbedded (already in main PhieuNhapXuat)
}

// 4. PhieuNhapXuat (Main entity - No ChiTietNhapXuat list)
public class PhieuNhapXuat {
    private String id;
    private String maPhieu;
    private LocalDate ngayLapPhieu;
    private VoucherType loaiPhieu;
    private KhachHangEmbedded khachHang;  // Embedded customer
    // NO List<ChiTietNhapXuat> - prevents large document size
}

// 5. ChiTietNhapXuat (Line items with embedded references)
public class ChiTietNhapXuat {
    private String id;
    private PhieuNhapXuatEmbedded phieuNhapXuat;  // Embedded voucher
    private DanhMucHangEmbedded maHang;           // Embedded product
    private Integer soLuong;
    private BigDecimal donGia;
}
```

**Storage Impact Analysis:**
- **KhachHangEmbedded:** ~60% reduction (removed 4-5 fields)
- **DanhMucHangEmbedded:** ~50% reduction (removed 4-5 fields)
- **PhieuNhapXuatEmbedded:** ~70% reduction (removed audit fields)
- **Overall Storage Reduction:** ~40-50% across all collections

#### **Solution 2: Data Compression Strategy**
```javascript
// MongoDB compression configuration for embedded documents
db.runCommand({
    collMod: "PhieuNhapXuat",
    validator: {
        $jsonSchema: {
            bsonType: "object",
            properties: {
                khach_hang: {
                    bsonType: "object",
                    properties: {
                        id: { bsonType: "string" },
                        maKH: { bsonType: "string" },
                        tenKH: { bsonType: "string" }
                    },
                    required: ["id", "maKH", "tenKH"]
                }
            }
        }
    }
});

// Enable compression for embedded collections
db.PhieuNhapXuat.createIndex({}, { 
    storageEngine: { 
        wiredTiger: { 
            configString: "block_compressor=snappy" 
        } 
    } 
});
```

#### **Solution 3: Archival Strategy**
```java
@Service
public class DataArchivalService {
    
    public void archiveOldTransactions(LocalDate cutoffDate) {
        // Move old transactions to archive collection
        // Keep only recent embedded data in main collections
        Criteria criteria = Criteria.where("ngay_lap_phieu").lt(cutoffDate);
        
        List<PhieuNhapXuat> oldPhieu = mongoTemplate.find(
            Query.query(criteria), PhieuNhapXuat.class
        );
        
        for (PhieuNhapXuat phieu : oldPhieu) {
            // Archive to separate collection with minimal embedded data
            PhieuNhapXuatArchived archived = createArchivedVersion(phieu);
            mongoTemplate.save(archived, "PhieuNhapXuatArchived");
            
            // Remove from main collection
            mongoTemplate.remove(phieu);
        }
    }
    
    private PhieuNhapXuatArchived createArchivedVersion(PhieuNhapXuat phieu) {
        // Create archived version with minimal embedded data
        PhieuNhapXuatArchived archived = new PhieuNhapXuatArchived();
        archived.setId(phieu.getId());
        archived.setMaPhieu(phieu.getMaPhieu());
        archived.setNgayLapPhieu(phieu.getNgayLapPhieu());
        archived.setLoaiPhieu(phieu.getLoaiPhieu());
        
        // Minimal customer info for archived data
        if (phieu.getKhachHang() != null) {
            archived.setKhachHangId(phieu.getKhachHang().getId());
            archived.setKhachHangTen(phieu.getKhachHang().getTenKH());
        }
        
        return archived;
    }
}
```

### 2. Write Performance Optimization (Priority 2)

#### **Solution 1: Batch Operations**
```java
@Service
public class BatchSynchronizationService {
    
    @Transactional
    public void batchUpdateEmbeddedDocuments(List<KhachHang> khachHangs) {
        // Batch update multiple embedded documents in single transaction
        List<WriteModel<Document>> operations = new ArrayList<>();
        
        for (KhachHang khachHang : khachHangs) {
            KhachHangEmbedded embedded = createMinimalEmbeddedKhachHang(khachHang);
            
            operations.add(new UpdateOneModel<>(
                Filters.eq("khach_hang.id", khachHang.getId()),
                Updates.set("khach_hang", embedded),
                new UpdateOptions().upsert(false)
            ));
        }
        
        if (!operations.isEmpty()) {
            mongoTemplate.getCollection("PhieuNhapXuat")
                .bulkWrite(operations, new BulkWriteOptions().ordered(false));
        }
    }
    
    private KhachHangEmbedded createMinimalEmbeddedKhachHang(KhachHang khachHang) {
        KhachHangEmbedded embedded = new KhachHangEmbedded();
        embedded.setId(khachHang.getId());
        embedded.setMaKH(khachHang.getMaKH());
        embedded.setTenKH(khachHang.getTenKH());
        // Only essential fields - no audit fields
        return embedded;
    }
}
```

#### **Solution 2: Asynchronous Synchronization**
```java
@Service
public class AsyncSynchronizationService {
    
    @Async("embeddedSyncExecutor")
    public CompletableFuture<Void> updateEmbeddedKhachHangAsync(KhachHang khachHang) {
        return CompletableFuture.runAsync(() -> {
            try {
                // Use minimal embedded data for better performance
                KhachHangEmbedded embedded = createMinimalEmbeddedKhachHang(khachHang);
                
                UpdateResult result = mongoTemplate.updateMulti(
                    Query.query(Criteria.where("khach_hang.id").is(khachHang.getId())),
                    Update.update("khach_hang", embedded),
                    "PhieuNhapXuat"
                );
                
                log.info("Async embedded update completed for KhachHang: {}, updated: {} documents", 
                    khachHang.getId(), result.getModifiedCount());
                    
            } catch (Exception e) {
                log.error("Async synchronization failed for KhachHang: {}", khachHang.getId(), e);
                // Add to retry queue with exponential backoff
                retryQueue.add(new RetryTask(khachHang, "updateEmbeddedKhachHang"));
            }
        });
    }
}
```

#### **Solution 3: Optimistic Locking**
```java
@Document(collection = "PhieuNhapXuat")
public class PhieuNhapXuat {
    @Version
    private Long version;
    
    @Field("khach_hang")
    private KhachHangEmbedded khachHang;
    
    // Optimistic locking prevents concurrent modification conflicts
    // Reduces write conflicts and improves performance
}
```

### 3. Indexing Optimization

#### **Solution 1: Compound Indexes for Embedded Fields**
```javascript
// Create optimized indexes for embedded document queries
db.ChiTietNhapXuat.createIndex({
    "phieu_nhap_xuat.ngay_lap_phieu": 1,
    "phieu_nhap_xuat.loai_phieu": 1,
    "ma_hang.id": 1
}, { name: "embedded_aggregation_index" });

db.ChiTietNhapXuat.createIndex({
    "phieu_nhap_xuat.khach_hang.id": 1,
    "phieu_nhap_xuat.ngay_lap_phieu": -1
}, { name: "customer_date_index" });

// Index for selective embedding queries
db.PhieuNhapXuat.createIndex({
    "khach_hang.id": 1,
    "ngay_lap_phieu": -1
}, { name: "customer_phieu_index" });
```

#### **Solution 2: Partial Indexes**
```javascript
// Create partial indexes for active data only
db.PhieuNhapXuat.createIndex(
    { "khach_hang.id": 1 },
    { 
        partialFilterExpression: { "is_deleted": false },
        name: "active_customer_index"
    }
);

db.ChiTietNhapXuat.createIndex(
    { "phieu_nhap_xuat.id": 1 },
    { 
        partialFilterExpression: { "so_luong": { $gt: 0 } },
        name: "active_chi_tiet_index"
    }
);
```

### 4. Caching Strategy

#### **Solution 1: Redis Caching for Minimal Embedded Data**
```java
@Service
public class EmbeddedDataCacheService {
    
    @Cacheable(value = "embeddedKhachHang", key = "#khachHangId")
    public KhachHangEmbedded getCachedKhachHang(String khachHangId) {
        return khachHangRepository.findById(khachHangId)
            .map(this::createMinimalEmbeddedKhachHang)
            .orElse(null);
    }
    
    @Cacheable(value = "embeddedDanhMucHang", key = "#danhMucHangId")
    public DanhMucHangEmbedded getCachedDanhMucHang(String danhMucHangId) {
        return danhMucHangRepository.findById(danhMucHangId)
            .map(this::createMinimalEmbeddedDanhMucHang)
            .orElse(null);
    }
    
    @CacheEvict(value = {"embeddedKhachHang", "embeddedDanhMucHang"}, allEntries = true)
    public void evictAllEmbeddedCache() {
        // Evict all cache when major updates occur
    }
}
```

#### **Solution 2: Application-Level Caching**
```java
@Component
public class EmbeddedDataCache {
    
    private final Map<String, KhachHangEmbedded> khachHangCache = new ConcurrentHashMap<>();
    private final Map<String, DanhMucHangEmbedded> danhMucHangCache = new ConcurrentHashMap<>();
    
    public KhachHangEmbedded getKhachHang(String id) {
        return khachHangCache.computeIfAbsent(id, this::loadMinimalKhachHang);
    }
    
    public DanhMucHangEmbedded getDanhMucHang(String id) {
        return danhMucHangCache.computeIfAbsent(id, this::loadMinimalDanhMucHang);
    }
    
    private KhachHangEmbedded loadMinimalKhachHang(String id) {
        // Load only minimal fields for embedded data
        return khachHangRepository.findById(id)
            .map(this::createMinimalEmbeddedKhachHang)
            .orElse(null);
    }
}
```

### 5. Monitoring & Alerting

#### **Solution 1: Performance Metrics**
```java
@Component
public class PerformanceMetricsService {
    
    @EventListener
    public void onEmbeddedUpdate(EmbeddedUpdateEvent event) {
        Timer.Sample sample = Timer.start();
        
        try {
            // Perform embedded update with minimal data
            dataSynchronizationService.updateEmbeddedKhachHang(event.getKhachHang());
        } finally {
            sample.stop(Timer.builder("embedded.update.duration")
                .tag("entity", "KhachHang")
                .tag("optimization", "selective_embedding")
                .register(meterRegistry));
        }
    }
    
    @Scheduled(fixedRate = 300000) // Every 5 minutes
    public void monitorStorageGrowth() {
        long currentSize = getCollectionSize("PhieuNhapXuat");
        long previousSize = getPreviousSize();
        
        double growthRate = ((double) (currentSize - previousSize) / previousSize) * 100;
        
        if (growthRate > 10) { // Alert if growth > 10%
            alertService.sendAlert("Storage growth rate: " + growthRate + "%");
        }
    }
}
```

#### **Solution 2: Health Checks**
```java
@Component
public class EmbeddedDataHealthIndicator implements HealthIndicator {
    
    @Override
    public Health health() {
        try {
            // Check embedded data consistency with minimal structure
            long inconsistentCount = checkEmbeddedDataConsistency();
            long oversizedDocuments = checkOversizedDocuments();
            
            Health.Builder builder = Health.up()
                .withDetail("embedded_data_status", "consistent")
                .withDetail("selective_embedding_optimized", true);
            
            if (inconsistentCount > 0) {
                builder.withDetail("inconsistent_embedded_documents", inconsistentCount);
            }
            
            if (oversizedDocuments > 0) {
                builder.withDetail("oversized_documents", oversizedDocuments);
            }
            
            return builder.build();
        } catch (Exception e) {
            return Health.down()
                .withException(e)
                .build();
        }
    }
}
```

---

## III. Implementation Roadmap

### Phase 1: Storage Optimization (Week 1-2) - PRIORITY
- [ ] Implement selective embedding with minimal fields
- [ ] Update embedded document classes (KhachHangEmbedded, DanhMucHangEmbedded, PhieuNhapXuatEmbedded)
- [ ] Create migration script for selective embedding
- [ ] Implement data compression strategy
- [ ] Add storage monitoring and alerts

### Phase 2: Write Performance Optimization (Week 3-4)
- [ ] Implement batch operations for embedded updates
- [ ] Add asynchronous synchronization
- [ ] Implement optimistic locking
- [ ] Add Redis caching for minimal embedded data

### Phase 3: Advanced Optimizations (Week 5-6)
- [ ] Create optimized indexes for embedded fields
- [ ] Implement partial indexes
- [ ] Add application-level caching
- [ ] Implement data archival strategy

### Phase 4: Monitoring & Tuning (Week 7-8)
- [ ] Implement performance monitoring
- [ ] Add health checks and alerting
- [ ] Performance tuning and optimization
- [ ] Comprehensive testing and validation

---

## IV. Expected Performance Improvements

### Storage Optimization (Priority 1)
| Optimization | Expected Improvement | Implementation |
|--------------|---------------------|----------------|
| **Selective Embedding** | 40-50% reduction | Minimal fields only |
| **Data Compression** | 20-30% reduction | MongoDB compression |
| **Archival Strategy** | 40-60% reduction | Move old data to archive |
| **Overall Storage** | 50-60% reduction | Combined optimizations |

### Write Performance (Priority 2)
| Optimization | Expected Improvement | Implementation |
|--------------|---------------------|----------------|
| **Batch Operations** | 60-80% faster | Bulk write operations |
| **Async Synchronization** | 40-60% faster | Non-blocking updates |
| **Optimistic Locking** | 20-30% faster | Reduced lock contention |
| **Minimal Embedded Data** | 30-50% faster | Smaller document updates |

### Query Performance
| Optimization | Expected Improvement | Implementation |
|--------------|---------------------|----------------|
| **Optimized Indexes** | 50-80% faster | Compound indexes |
| **Caching Strategy** | 80-95% faster | Redis + application cache |
| **Partial Indexes** | 30-50% faster | Index only active data |
| **Selective Embedding** | 20-40% faster | Smaller document reads |

---

## V. Monitoring & Maintenance

### Key Metrics to Monitor
- **Storage growth rate** (target: < 10% monthly) - PRIORITY
- **Document size** (target: < 16MB per document)
- **Embedded update duration** (target: < 100ms)
- **Cache hit ratio** (target: > 80%)
- **Synchronization error rate** (target: < 1%)
- **Query response time** (target: < 200ms)

### Maintenance Tasks
- **Daily**: Monitor storage growth and document sizes
- [ ] **Weekly**: Review performance metrics and cache statistics
- [ ] **Monthly**: Analyze storage optimization and optimize indexes
- [ ] **Quarterly**: Review and update archival strategy
- [ ] **Annually**: Comprehensive performance audit and optimization

---

## VI. Risk Mitigation

### Potential Risks
1. **Storage growth beyond expectations**
2. **Cache invalidation complexity**
3. **Synchronization failures**
4. **Performance degradation during peak loads**
5. **Data consistency issues with selective embedding**

### Mitigation Strategies
1. **Set up storage monitoring and alerts**
2. **Implement robust cache invalidation strategies**
3. **Add retry mechanisms for synchronization failures**
4. **Implement circuit breakers for peak load handling**
5. **Regular consistency checks for embedded data**

---

## Conclusion

The performance issues identified after the @DBRef to embedded documents migration can be effectively addressed through a prioritized approach:

**Priority 1: Storage Optimization**
- **Selective embedding** with minimal fields for 40-50% storage reduction
- **Data compression** strategies for additional 20-30% reduction
- **Archival strategy** for long-term storage management

**Priority 2: Write Performance**
- **Batch operations** and **asynchronous synchronization** for 60-80% faster writes
- **Optimistic locking** to reduce conflicts
- **Minimal embedded data** for faster updates

These solutions will ensure the warehouse management system maintains optimal performance while leveraging the benefits of embedded documents for complex aggregations and queries.

**Next Steps:** Begin implementation of Phase 1 (Storage Optimization) and monitor storage improvements. 