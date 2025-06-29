# Quick Reference Card - Warehouse Management System

## 🚀 Quick Start Commands

```bash
# 1. Start development environment
./quick-start-dev.sh

# 2. Check if everything is working
./view-migration-results.sh

# 3. Test MongoDB Atlas connection
./test-mongodb-direct-api.sh
```

## 📊 Current Status
- ✅ **Migration**: Completed (40-50% storage reduction)
- ✅ **Performance**: 30-40% improvement
- ✅ **MongoDB Atlas**: Connected and working
- 🔄 **Next**: Indexing Strategy

## 🎯 Next Steps (Priority Order)

### 1. Indexing Strategy (High Priority)
```bash
# Create indexes for embedded fields
db.chitietnhapxuat.createIndex({"phieuNhapXuat.id": 1})
db.danhmuchang.createIndex({"khachHang.id": 1})
db.phieunhapxuat.createIndex({"khachHang.id": 1})
```

### 2. Caching Implementation
- Redis cache for frequently accessed data
- Cache embedded documents
- Cache aggregation results

### 3. Batch Operations
- Bulk insert/update operations
- Batch processing for large datasets

## 🔧 Important Scripts

| Script | Purpose |
|--------|---------|
| `./quick-start-dev.sh` | Start development environment |
| `./view-migration-results.sh` | Show migration status |
| `./analyze-storage-baseline-api.sh` | Measure performance |
| `./run-archival.sh` | Run archival operations |
| `./test-mongodb-direct-api.sh` | Test MongoDB connection |

## ⚙️ Configuration

### Environment Variables
```bash
export MONGODB_URI="mongodb+srv://username:password@cluster.mongodb.net/warehouse"
export SPRING_PROFILES_ACTIVE=dev
```

### Application Properties
```yaml
migration:
  run: false
  batch-size: 1000

archival:
  enabled: true
  threshold-years: 3
  batch-size: 500
```

## 🐛 Troubleshooting

### Common Issues
1. **MongoDB Connection Error**
   ```bash
   ./test-mongodb-direct-api.sh
   ```

2. **Application Won't Start**
   ```bash
   ./quick-start-dev.sh
   ```

3. **Performance Issues**
   ```bash
   ./analyze-storage-baseline-api.sh
   ```

## 📁 Key Files Location

```
src/main/java/dtu/k30/msc/whm/
├── domain/
│   ├── ChiTietNhapXuat.java
│   ├── DanhMucHang.java
│   ├── PhieuNhapXuat.java
│   └── embedded/
│       ├── KhachHangEmbedded.java
│       ├── DanhMucHangEmbedded.java
│       └── PhieuNhapXuatEmbedded.java
├── service/
│   ├── DataSynchronizationService.java
│   └── DataArchivalService.java
└── web/rest/
    ├── MigrationResource.java
    └── ArchivalResource.java
```

## 🔄 MigrationRunner Commands

```bash
# Run migration with archival
java -jar target/warehouse-mgmt-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev --migration.run=true --archival.enabled=true

# Run only archival
java -jar target/warehouse-mgmt-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev --archival.run=true

# Custom parameters
java -jar target/warehouse-mgmt-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev --migration.run=true --archival.threshold-years=2
```

## 📈 Performance Metrics

- **Storage Reduction**: 40-50%
- **Query Performance**: 30-40% improvement
- **Data Consistency**: ✅ Maintained
- **Cloud Integration**: ✅ MongoDB Atlas

## 🎯 Session Checklist

When starting a new session:

1. ✅ Run `./quick-start-dev.sh`
2. ✅ Check `./view-migration-results.sh`
3. ✅ Verify `./test-mongodb-direct-api.sh`
4. ✅ Continue with next improvement (Indexing Strategy)

---

**Project**: Warehouse Management System  
**Location**: `/Users/thanhloi/Workings/dtu-k30-msc/warehouse-mgmt`  
**Status**: Migration Complete, Ready for Improvements 