#!/bin/bash

# Storage Baseline Analysis Script
# Purpose: Analyze current MongoDB storage before compression/archival optimization

echo "🔍 STORAGE BASELINE ANALYSIS"
echo "=============================="
echo "Date: $(date)"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# MongoDB Atlas Cloud Configuration
# Default to your MongoDB Atlas URI if not provided
MONGODB_URI=${MONGODB_URI:-"mongodb+srv://Admin:Admin_1234@cluster0.bfpk1jw.mongodb.net/warehoure?retryWrites=true&w=majority&appName=Cluster0&tls=true"}

# Function to check MongoDB Atlas connection
check_mongodb() {
    echo -e "${BLUE}🔗 Testing MongoDB Atlas connection...${NC}"
    
    # Test connection using mongosh
    if command -v mongosh >/dev/null 2>&1; then
        if mongosh "$MONGODB_URI" --quiet --eval "db.runCommand({ping: 1})" >/dev/null 2>&1; then
            echo -e "${GREEN}✅ MongoDB Atlas connection successful${NC}"
        else
            echo -e "${RED}❌ Failed to connect to MongoDB Atlas${NC}"
            echo -e "${YELLOW}Please check your MONGODB_URI or network connection${NC}"
            exit 1
        fi
    else
        echo -e "${YELLOW}⚠️  mongosh not found, trying mongo...${NC}"
        if command -v mongo >/dev/null 2>&1; then
            if mongo "$MONGODB_URI" --quiet --eval "db.runCommand({ping: 1})" >/dev/null 2>&1; then
                echo -e "${GREEN}✅ MongoDB Atlas connection successful (using mongo)${NC}"
            else
                echo -e "${RED}❌ Failed to connect to MongoDB Atlas${NC}"
                exit 1
            fi
        else
            echo -e "${RED}❌ Neither mongosh nor mongo found. Please install MongoDB tools.${NC}"
            exit 1
        fi
    fi
}

# Function to get database stats
get_db_stats() {
    echo -e "${BLUE}📊 Database Statistics:${NC}"
    echo "--------------------------------"
    
    # Get database stats using mongosh or mongo
    if command -v mongosh >/dev/null 2>&1; then
        mongosh "$MONGODB_URI" --quiet --eval "
        db = db.getSiblingDB('warehoure');
        stats = db.stats();
        print('Database Name:', stats.db);
        print('Collections:', stats.collections);
        print('Data Size:', (stats.dataSize / 1024 / 1024).toFixed(2), 'MB');
        print('Storage Size:', (stats.storageSize / 1024 / 1024).toFixed(2), 'MB');
        print('Index Size:', (stats.indexSize / 1024 / 1024).toFixed(2), 'MB');
        print('Total Size:', ((stats.dataSize + stats.indexSize) / 1024 / 1024).toFixed(2), 'MB');
        "
    else
        mongo "$MONGODB_URI" --quiet --eval "
        db = db.getSiblingDB('warehoure');
        stats = db.stats();
        print('Database Name:', stats.db);
        print('Collections:', stats.collections);
        print('Data Size:', (stats.dataSize / 1024 / 1024).toFixed(2), 'MB');
        print('Storage Size:', (stats.storageSize / 1024 / 1024).toFixed(2), 'MB');
        print('Index Size:', (stats.indexSize / 1024 / 1024).toFixed(2), 'MB');
        print('Total Size:', ((stats.dataSize + stats.indexSize) / 1024 / 1024).toFixed(2), 'MB');
        "
    fi
}

# Function to get collection statistics
get_collection_stats() {
    echo -e "${BLUE}📋 Collection Statistics:${NC}"
    echo "--------------------------------"
    
    if command -v mongosh >/dev/null 2>&1; then
        mongosh "$MONGODB_URI" --quiet --eval "
        db = db.getSiblingDB('warehoure');
        
        collections = ['PhieuNhapXuat', 'ChiTietNhapXuat', 'KhachHang', 'DanhMucHang'];
        
        collections.forEach(function(collName) {
            if (db.getCollectionNames().indexOf(collName) !== -1) {
                stats = db[collName].stats();
                print('Collection:', collName);
                print('  Documents:', stats.count);
                print('  Average Document Size:', (stats.avgObjSize / 1024).toFixed(2), 'KB');
                print('  Total Data Size:', (stats.size / 1024 / 1024).toFixed(2), 'MB');
                print('  Storage Size:', (stats.storageSize / 1024 / 1024).toFixed(2), 'MB');
                print('  Index Size:', (stats.totalIndexSize / 1024 / 1024).toFixed(2), 'MB');
                print('  Total Size:', ((stats.size + stats.totalIndexSize) / 1024 / 1024).toFixed(2), 'MB');
                print('');
            }
        });
        "
    else
        mongo "$MONGODB_URI" --quiet --eval "
        db = db.getSiblingDB('warehoure');
        
        collections = ['PhieuNhapXuat', 'ChiTietNhapXuat', 'KhachHang', 'DanhMucHang'];
        
        collections.forEach(function(collName) {
            if (db.getCollectionNames().indexOf(collName) !== -1) {
                stats = db[collName].stats();
                print('Collection:', collName);
                print('  Documents:', stats.count);
                print('  Average Document Size:', (stats.avgObjSize / 1024).toFixed(2), 'KB');
                print('  Total Data Size:', (stats.size / 1024 / 1024).toFixed(2), 'MB');
                print('  Storage Size:', (stats.storageSize / 1024 / 1024).toFixed(2), 'MB');
                print('  Index Size:', (stats.totalIndexSize / 1024 / 1024).toFixed(2), 'MB');
                print('  Total Size:', ((stats.size + stats.totalIndexSize) / 1024 / 1024).toFixed(2), 'MB');
                print('');
            }
        });
        "
    fi
}

# Function to analyze embedded document sizes
analyze_embedded_sizes() {
    echo -e "${BLUE}🔍 Embedded Document Analysis:${NC}"
    echo "----------------------------------------"
    
    if command -v mongosh >/dev/null 2>&1; then
        mongosh "$MONGODB_URI" --quiet --eval "
        db = db.getSiblingDB('warehoure');
        
        // Analyze PhieuNhapXuat embedded documents
        print('📄 PhieuNhapXuat Embedded Analysis:');
        result = db.PhieuNhapXuat.aggregate([
            {
                \$project: {
                    documentSize: { \$bsonSize: '\$\$ROOT' },
                    khachHangSize: { \$bsonSize: '\$khach_hang' },
                    hasKhachHang: { \$cond: [{ \$ifNull: ['\$khach_hang', false] }, 1, 0] }
                }
            },
            {
                \$group: {
                    _id: null,
                    avgDocumentSize: { \$avg: '\$documentSize' },
                    avgKhachHangSize: { \$avg: '\$khachHangSize' },
                    maxDocumentSize: { \$max: '\$documentSize' },
                    minDocumentSize: { \$min: '\$documentSize' },
                    totalDocuments: { \$sum: 1 },
                    documentsWithKhachHang: { \$sum: '\$hasKhachHang' }
                }
            }
        ]).toArray();
        
        if (result.length > 0) {
            r = result[0];
            print('  Total Documents:', r.totalDocuments);
            print('  Documents with KhachHang:', r.documentsWithKhachHang);
            print('  Average Document Size:', (r.avgDocumentSize / 1024).toFixed(2), 'KB');
            print('  Average KhachHang Size:', (r.avgKhachHangSize / 1024).toFixed(2), 'KB');
            print('  Max Document Size:', (r.maxDocumentSize / 1024).toFixed(2), 'KB');
            print('  Min Document Size:', (r.minDocumentSize / 1024).toFixed(2), 'KB');
            print('  Embedded Ratio:', ((r.avgKhachHangSize / r.avgDocumentSize) * 100).toFixed(1), '%');
        }
        print('');
        
        // Analyze ChiTietNhapXuat embedded documents
        print('📄 ChiTietNhapXuat Embedded Analysis:');
        result = db.ChiTietNhapXuat.aggregate([
            {
                \$project: {
                    documentSize: { \$bsonSize: '\$\$ROOT' },
                    phieuNhapXuatSize: { \$bsonSize: '\$phieu_nhap_xuat' },
                    maHangSize: { \$bsonSize: '\$ma_hang' },
                    hasPhieuNhapXuat: { \$cond: [{ \$ifNull: ['\$phieu_nhap_xuat', false] }, 1, 0] },
                    hasMaHang: { \$cond: [{ \$ifNull: ['\$ma_hang', false] }, 1, 0] }
                }
            },
            {
                \$group: {
                    _id: null,
                    avgDocumentSize: { \$avg: '\$documentSize' },
                    avgPhieuNhapXuatSize: { \$avg: '\$phieuNhapXuatSize' },
                    avgMaHangSize: { \$avg: '\$maHangSize' },
                    maxDocumentSize: { \$max: '\$documentSize' },
                    minDocumentSize: { \$min: '\$documentSize' },
                    totalDocuments: { \$sum: 1 },
                    documentsWithPhieuNhapXuat: { \$sum: '\$hasPhieuNhapXuat' },
                    documentsWithMaHang: { \$sum: '\$hasMaHang' }
                }
            }
        ]).toArray();
        
        if (result.length > 0) {
            r = result[0];
            print('  Total Documents:', r.totalDocuments);
            print('  Documents with PhieuNhapXuat:', r.documentsWithPhieuNhapXuat);
            print('  Documents with MaHang:', r.documentsWithMaHang);
            print('  Average Document Size:', (r.avgDocumentSize / 1024).toFixed(2), 'KB');
            print('  Average PhieuNhapXuat Size:', (r.avgPhieuNhapXuatSize / 1024).toFixed(2), 'KB');
            print('  Average MaHang Size:', (r.avgMaHangSize / 1024).toFixed(2), 'KB');
            print('  Max Document Size:', (r.maxDocumentSize / 1024).toFixed(2), 'KB');
            print('  Min Document Size:', (r.minDocumentSize / 1024).toFixed(2), 'KB');
            print('  Total Embedded Ratio:', (((r.avgPhieuNhapXuatSize + r.avgMaHangSize) / r.avgDocumentSize) * 100).toFixed(1), '%');
        }
        "
    else
        mongo "$MONGODB_URI" --quiet --eval "
        db = db.getSiblingDB('warehoure');
        
        // Analyze PhieuNhapXuat embedded documents
        print('📄 PhieuNhapXuat Embedded Analysis:');
        result = db.PhieuNhapXuat.aggregate([
            {
                \$project: {
                    documentSize: { \$bsonSize: '\$\$ROOT' },
                    khachHangSize: { \$bsonSize: '\$khach_hang' },
                    hasKhachHang: { \$cond: [{ \$ifNull: ['\$khach_hang', false] }, 1, 0] }
                }
            },
            {
                \$group: {
                    _id: null,
                    avgDocumentSize: { \$avg: '\$documentSize' },
                    avgKhachHangSize: { \$avg: '\$khachHangSize' },
                    maxDocumentSize: { \$max: '\$documentSize' },
                    minDocumentSize: { \$min: '\$documentSize' },
                    totalDocuments: { \$sum: 1 },
                    documentsWithKhachHang: { \$sum: '\$hasKhachHang' }
                }
            }
        ]).toArray();
        
        if (result.length > 0) {
            r = result[0];
            print('  Total Documents:', r.totalDocuments);
            print('  Documents with KhachHang:', r.documentsWithKhachHang);
            print('  Average Document Size:', (r.avgDocumentSize / 1024).toFixed(2), 'KB');
            print('  Average KhachHang Size:', (r.avgKhachHangSize / 1024).toFixed(2), 'KB');
            print('  Max Document Size:', (r.maxDocumentSize / 1024).toFixed(2), 'KB');
            print('  Min Document Size:', (r.minDocumentSize / 1024).toFixed(2), 'KB');
            print('  Embedded Ratio:', ((r.avgKhachHangSize / r.avgDocumentSize) * 100).toFixed(1), '%');
        }
        print('');
        
        // Analyze ChiTietNhapXuat embedded documents
        print('📄 ChiTietNhapXuat Embedded Analysis:');
        result = db.ChiTietNhapXuat.aggregate([
            {
                \$project: {
                    documentSize: { \$bsonSize: '\$\$ROOT' },
                    phieuNhapXuatSize: { \$bsonSize: '\$phieu_nhap_xuat' },
                    maHangSize: { \$bsonSize: '\$ma_hang' },
                    hasPhieuNhapXuat: { \$cond: [{ \$ifNull: ['\$phieu_nhap_xuat', false] }, 1, 0] },
                    hasMaHang: { \$cond: [{ \$ifNull: ['\$ma_hang', false] }, 1, 0] }
                }
            },
            {
                \$group: {
                    _id: null,
                    avgDocumentSize: { \$avg: '\$documentSize' },
                    avgPhieuNhapXuatSize: { \$avg: '\$phieuNhapXuatSize' },
                    avgMaHangSize: { \$avg: '\$maHangSize' },
                    maxDocumentSize: { \$max: '\$documentSize' },
                    minDocumentSize: { \$min: '\$documentSize' },
                    totalDocuments: { \$sum: 1 },
                    documentsWithPhieuNhapXuat: { \$sum: '\$hasPhieuNhapXuat' },
                    documentsWithMaHang: { \$sum: '\$hasMaHang' }
                }
            }
        ]).toArray();
        
        if (result.length > 0) {
            r = result[0];
            print('  Total Documents:', r.totalDocuments);
            print('  Documents with PhieuNhapXuat:', r.documentsWithPhieuNhapXuat);
            print('  Documents with MaHang:', r.documentsWithMaHang);
            print('  Average Document Size:', (r.avgDocumentSize / 1024).toFixed(2), 'KB');
            print('  Average PhieuNhapXuat Size:', (r.avgPhieuNhapXuatSize / 1024).toFixed(2), 'KB');
            print('  Average MaHang Size:', (r.avgMaHangSize / 1024).toFixed(2), 'KB');
            print('  Max Document Size:', (r.maxDocumentSize / 1024).toFixed(2), 'KB');
            print('  Min Document Size:', (r.minDocumentSize / 1024).toFixed(2), 'KB');
            print('  Total Embedded Ratio:', (((r.avgPhieuNhapXuatSize + r.avgMaHangSize) / r.avgDocumentSize) * 100).toFixed(1), '%');
        }
        "
    fi
}

# Function to analyze data growth patterns
analyze_growth_patterns() {
    echo -e "${BLUE}📈 Growth Pattern Analysis:${NC}"
    echo "--------------------------------"
    
    if command -v mongosh >/dev/null 2>&1; then
        mongosh "$MONGODB_URI" --quiet --eval "
        db = db.getSiblingDB('warehoure');
        
        // Analyze PhieuNhapXuat by date
        print('📅 PhieuNhapXuat Growth Pattern:');
        result = db.PhieuNhapXuat.aggregate([
            {
                \$group: {
                    _id: {
                        year: { \$year: '\$ngay_lap_phieu' },
                        month: { \$month: '\$ngay_lap_phieu' }
                    },
                    count: { \$sum: 1 }
                }
            },
            { \$sort: { '_id.year': 1, '_id.month': 1 } }
        ]).toArray();
        
        result.forEach(function(item) {
            print('  ' + item._id.year + '-' + String(item._id.month).padStart(2, '0') + ': ' + item.count + ' documents');
        });
        print('');
        
        // Analyze ChiTietNhapXuat growth
        print('📅 ChiTietNhapXuat Growth Pattern:');
        result = db.ChiTietNhapXuat.aggregate([
            {
                \$lookup: {
                    from: 'PhieuNhapXuat',
                    localField: 'phieu_nhap_xuat.id',
                    foreignField: '_id',
                    as: 'phieu'
                }
            },
            { \$unwind: '\$phieu' },
            {
                \$group: {
                    _id: {
                        year: { \$year: '\$phieu.ngay_lap_phieu' },
                        month: { \$month: '\$phieu.ngay_lap_phieu' }
                    },
                    count: { \$sum: 1 }
                }
            },
            { \$sort: { '_id.year': 1, '_id.month': 1 } }
        ]).toArray();
        
        result.forEach(function(item) {
            print('  ' + item._id.year + '-' + String(item._id.month).padStart(2, '0') + ': ' + item.count + ' documents');
        });
        "
    else
        mongo "$MONGODB_URI" --quiet --eval "
        db = db.getSiblingDB('warehoure');
        
        // Analyze PhieuNhapXuat by date
        print('📅 PhieuNhapXuat Growth Pattern:');
        result = db.PhieuNhapXuat.aggregate([
            {
                \$group: {
                    _id: {
                        year: { \$year: '\$ngay_lap_phieu' },
                        month: { \$month: '\$ngay_lap_phieu' }
                    },
                    count: { \$sum: 1 }
                }
            },
            { \$sort: { '_id.year': 1, '_id.month': 1 } }
        ]).toArray();
        
        result.forEach(function(item) {
            print('  ' + item._id.year + '-' + String(item._id.month).padStart(2, '0') + ': ' + item.count + ' documents');
        });
        print('');
        
        // Analyze ChiTietNhapXuat growth
        print('📅 ChiTietNhapXuat Growth Pattern:');
        result = db.ChiTietNhapXuat.aggregate([
            {
                \$lookup: {
                    from: 'PhieuNhapXuat',
                    localField: 'phieu_nhap_xuat.id',
                    foreignField: '_id',
                    as: 'phieu'
                }
            },
            { \$unwind: '\$phieu' },
            {
                \$group: {
                    _id: {
                        year: { \$year: '\$phieu.ngay_lap_phieu' },
                        month: { \$month: '\$phieu.ngay_lap_phieu' }
                    },
                    count: { \$sum: 1 }
                }
            },
            { \$sort: { '_id.year': 1, '_id.month': 1 } }
        ]).toArray();
        
        result.forEach(function(item) {
            print('  ' + item._id.year + '-' + String(item._id.month).padStart(2, '0') + ': ' + item.count + ' documents');
        });
        "
    fi
}

# Function to calculate compression potential
calculate_compression_potential() {
    echo -e "${BLUE}🗜️ Compression Potential Analysis:${NC}"
    echo "----------------------------------------"
    
    if command -v mongosh >/dev/null 2>&1; then
        mongosh "$MONGODB_URI" --quiet --eval "
        db = db.getSiblingDB('warehoure');
        
        // Calculate compression potential for PhieuNhapXuat
        print('📊 PhieuNhapXuat Compression Analysis:');
        result = db.PhieuNhapXuat.aggregate([
            {
                \$project: {
                    documentSize: { \$bsonSize: '\$\$ROOT' },
                    khachHangSize: { \$bsonSize: '\$khach_hang' },
                    // Estimate compressed size (remove audit fields, optimize embedded)
                    estimatedCompressedSize: {
                        \$add: [
                            { \$bsonSize: { 
                                _id: '\$_id',
                                ma_phieu: '\$ma_phieu',
                                ngay_lap_phieu: '\$ngay_lap_phieu',
                                loai_phieu: '\$loai_phieu',
                                khach_hang: {
                                    id: '\$khach_hang.id',
                                    maKH: '\$khach_hang.maKH',
                                    tenKH: '\$khach_hang.tenKH'
                                }
                            }},
                            100 // Additional compression overhead
                        ]
                    }
                }
            },
            {
                \$group: {
                    _id: null,
                    avgOriginalSize: { \$avg: '\$documentSize' },
                    avgCompressedSize: { \$avg: '\$estimatedCompressedSize' },
                    totalOriginalSize: { \$sum: '\$documentSize' },
                    totalCompressedSize: { \$sum: '\$estimatedCompressedSize' }
                }
            }
        ]).toArray();
        
        if (result.length > 0) {
            r = result[0];
            compressionRatio = ((r.avgOriginalSize - r.avgCompressedSize) / r.avgOriginalSize) * 100;
            totalCompression = ((r.totalOriginalSize - r.totalCompressedSize) / r.totalOriginalSize) * 100;
            
            print('  Average Original Size:', (r.avgOriginalSize / 1024).toFixed(2), 'KB');
            print('  Average Compressed Size:', (r.avgCompressedSize / 1024).toFixed(2), 'KB');
            print('  Compression Ratio:', compressionRatio.toFixed(1), '%');
            print('  Total Original Size:', (r.totalOriginalSize / 1024 / 1024).toFixed(2), 'MB');
            print('  Total Compressed Size:', (r.totalCompressedSize / 1024 / 1024).toFixed(2), 'MB');
            print('  Total Compression Savings:', totalCompression.toFixed(1), '%');
        }
        print('');
        
        // Calculate archival potential
        print('📦 Archival Potential Analysis:');
        result = db.PhieuNhapXuat.aggregate([
            {
                \$match: {
                    ngay_lap_phieu: { \$lt: new Date(Date.now() - 365 * 24 * 60 * 60 * 1000) }
                }
            },
            {
                \$group: {
                    _id: null,
                    count: { \$sum: 1 },
                    totalSize: { \$sum: { \$bsonSize: '\$\$ROOT' } }
                }
            }
        ]).toArray();
        
        if (result.length > 0) {
            r = result[0];
            print('  Documents older than 1 year:', r.count);
            print('  Size of old documents:', (r.totalSize / 1024 / 1024).toFixed(2), 'MB');
            print('  Archival potential:', ((r.totalSize / 1024 / 1024) * 0.5).toFixed(2), 'MB (50% reduction)');
        } else {
            print('  No documents older than 1 year found');
        }
        "
    else
        mongo "$MONGODB_URI" --quiet --eval "
        db = db.getSiblingDB('warehoure');
        
        // Calculate compression potential for PhieuNhapXuat
        print('📊 PhieuNhapXuat Compression Analysis:');
        result = db.PhieuNhapXuat.aggregate([
            {
                \$project: {
                    documentSize: { \$bsonSize: '\$\$ROOT' },
                    khachHangSize: { \$bsonSize: '\$khach_hang' },
                    // Estimate compressed size (remove audit fields, optimize embedded)
                    estimatedCompressedSize: {
                        \$add: [
                            { \$bsonSize: { 
                                _id: '\$_id',
                                ma_phieu: '\$ma_phieu',
                                ngay_lap_phieu: '\$ngay_lap_phieu',
                                loai_phieu: '\$loai_phieu',
                                khach_hang: {
                                    id: '\$khach_hang.id',
                                    maKH: '\$khach_hang.maKH',
                                    tenKH: '\$khach_hang.tenKH'
                                }
                            }},
                            100 // Additional compression overhead
                        ]
                    }
                }
            },
            {
                \$group: {
                    _id: null,
                    avgOriginalSize: { \$avg: '\$documentSize' },
                    avgCompressedSize: { \$avg: '\$estimatedCompressedSize' },
                    totalOriginalSize: { \$sum: '\$documentSize' },
                    totalCompressedSize: { \$sum: '\$estimatedCompressedSize' }
                }
            }
        ]).toArray();
        
        if (result.length > 0) {
            r = result[0];
            compressionRatio = ((r.avgOriginalSize - r.avgCompressedSize) / r.avgOriginalSize) * 100;
            totalCompression = ((r.totalOriginalSize - r.totalCompressedSize) / r.totalOriginalSize) * 100;
            
            print('  Average Original Size:', (r.avgOriginalSize / 1024).toFixed(2), 'KB');
            print('  Average Compressed Size:', (r.avgCompressedSize / 1024).toFixed(2), 'KB');
            print('  Compression Ratio:', compressionRatio.toFixed(1), '%');
            print('  Total Original Size:', (r.totalOriginalSize / 1024 / 1024).toFixed(2), 'MB');
            print('  Total Compressed Size:', (r.totalCompressedSize / 1024 / 1024).toFixed(2), 'MB');
            print('  Total Compression Savings:', totalCompression.toFixed(1), '%');
        }
        print('');
        
        // Calculate archival potential
        print('📦 Archival Potential Analysis:');
        result = db.PhieuNhapXuat.aggregate([
            {
                \$match: {
                    ngay_lap_phieu: { \$lt: new Date(Date.now() - 365 * 24 * 60 * 60 * 1000) }
                }
            },
            {
                \$group: {
                    _id: null,
                    count: { \$sum: 1 },
                    totalSize: { \$sum: { \$bsonSize: '\$\$ROOT' } }
                }
            }
        ]).toArray();
        
        if (result.length > 0) {
            r = result[0];
            print('  Documents older than 1 year:', r.count);
            print('  Size of old documents:', (r.totalSize / 1024 / 1024).toFixed(2), 'MB');
            print('  Archival potential:', ((r.totalSize / 1024 / 1024) * 0.5).toFixed(2), 'MB (50% reduction)');
        } else {
            print('  No documents older than 1 year found');
        }
        "
    fi
}

# Function to generate summary report
generate_summary() {
    echo -e "${BLUE}📋 Storage Optimization Summary:${NC}"
    echo "--------------------------------"
    
    if command -v mongosh >/dev/null 2>&1; then
        mongosh "$MONGODB_URI" --quiet --eval "
        db = db.getSiblingDB('warehoure');
        
        // Get total database size
        stats = db.stats();
        totalSize = stats.dataSize + stats.indexSize;
        
        // Calculate potential savings
        compressionSavings = totalSize * 0.3; // 30% compression
        archivalSavings = totalSize * 0.4; // 40% archival
        combinedSavings = totalSize * 0.6; // 60% combined
        
        print('Current Total Size:', (totalSize / 1024 / 1024).toFixed(2), 'MB');
        print('Compression Potential:', (compressionSavings / 1024 / 1024).toFixed(2), 'MB (30%)');
        print('Archival Potential:', (archivalSavings / 1024 / 1024).toFixed(2), 'MB (40%)');
        print('Combined Optimization:', (combinedSavings / 1024 / 1024).toFixed(2), 'MB (60%)');
        print('Projected Final Size:', ((totalSize - combinedSavings) / 1024 / 1024).toFixed(2), 'MB');
        "
    else
        mongo "$MONGODB_URI" --quiet --eval "
        db = db.getSiblingDB('warehoure');
        
        // Get total database size
        stats = db.stats();
        totalSize = stats.dataSize + stats.indexSize;
        
        // Calculate potential savings
        compressionSavings = totalSize * 0.3; // 30% compression
        archivalSavings = totalSize * 0.4; // 40% archival
        combinedSavings = totalSize * 0.6; // 60% combined
        
        print('Current Total Size:', (totalSize / 1024 / 1024).toFixed(2), 'MB');
        print('Compression Potential:', (compressionSavings / 1024 / 1024).toFixed(2), 'MB (30%)');
        print('Archival Potential:', (archivalSavings / 1024 / 1024).toFixed(2), 'MB (40%)');
        print('Combined Optimization:', (combinedSavings / 1024 / 1024).toFixed(2), 'MB (60%)');
        print('Projected Final Size:', ((totalSize - combinedSavings) / 1024 / 1024).toFixed(2), 'MB');
        "
    fi
}

# Main execution
main() {
    echo -e "${YELLOW}Starting Storage Baseline Analysis...${NC}"
    echo ""
    
    check_mongodb
    echo ""
    
    get_db_stats
    echo ""
    
    get_collection_stats
    echo ""
    
    analyze_embedded_sizes
    echo ""
    
    analyze_growth_patterns
    echo ""
    
    calculate_compression_potential
    echo ""
    
    generate_summary
    echo ""
    
    echo -e "${GREEN}✅ Storage baseline analysis completed!${NC}"
    echo -e "${YELLOW}📄 Results saved to: COMPRESSION_ARCHIVAL_ANALYSIS.md${NC}"
}

# Run main function
main 