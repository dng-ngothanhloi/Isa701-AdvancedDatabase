package dtu.k30.msc.whm.config.dbmigrations;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Migration script to convert DBRef references to embedded documents.
 * This script should be run once to migrate existing data to the new structure.
 */
@ChangeUnit(id = "migrate-dbref-to-embedded", order = "001", author = "warehouse-mgmt")
public class MongoDBMigrationScript {

    private static final Logger LOG = LoggerFactory.getLogger(MongoDBMigrationScript.class);

    private final MongoTemplate mongoTemplate;

    public MongoDBMigrationScript(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Execution
    public void migrateDBRefToEmbedded() {
        LOG.info("Starting MongoDB migration: DBRef to Embedded Documents");
        
        try {
            // Step 1: Migrate ChiTietNhapXuat collection
            migrateChiTietNhapXuat();
            
            // Step 2: Migrate PhieuNhapXuat collection  
            migratePhieuNhapXuat();
            
            LOG.info("MongoDB migration completed successfully");
            
        } catch (Exception e) {
            LOG.error("Migration failed: {}", e.getMessage(), e);
            throw new RuntimeException("Migration failed", e);
        }
    }

    @RollbackExecution
    public void rollback() {
        LOG.warn("Rollback not implemented for this migration. Manual intervention required.");
    }

    private void migrateChiTietNhapXuat() {
        LOG.info("Migrating ChiTietNhapXuat collection...");
        
        MongoCollection<Document> chiTietCollection = mongoTemplate.getCollection("ChiTietNhapXuat");
        MongoCollection<Document> phieuCollection = mongoTemplate.getCollection("PhieuNhapXuat");
        MongoCollection<Document> danhMucCollection = mongoTemplate.getCollection("DanhMucHang");
        
        List<Document> chiTietDocuments = chiTietCollection.find().into(new ArrayList<>());
        int migratedCount = 0;
        
        for (Document chiTietDoc : chiTietDocuments) {
            try {
                // Check if document already has embedded structure
                if (chiTietDoc.containsKey("phieu_nhap_xuat") && 
                    chiTietDoc.get("phieu_nhap_xuat") instanceof Document) {
                    Document phieuEmbedded = (Document) chiTietDoc.get("phieu_nhap_xuat");
                    if (phieuEmbedded.containsKey("id") && !phieuEmbedded.containsKey("$ref")) {
                        LOG.debug("Document {} already migrated, skipping", chiTietDoc.get("_id"));
                        continue;
                    }
                }
                
                // Migrate phieuNhapXuat DBRef to embedded
                if (chiTietDoc.containsKey("phieuNhapXuat")) {
                    Document phieuRef = (Document) chiTietDoc.get("phieuNhapXuat");
                    if (phieuRef.containsKey("$ref") && phieuRef.containsKey("$id")) {
                        String phieuId = phieuRef.getString("$id");
                        Document phieuDoc = phieuCollection.find(Filters.eq("_id", new ObjectId(phieuId))).first();
                        
                        if (phieuDoc != null) {
                            Document phieuEmbedded = createPhieuNhapXuatEmbedded(phieuDoc);
                            chiTietDoc.put("phieu_nhap_xuat", phieuEmbedded);
                            chiTietDoc.remove("phieuNhapXuat");
                        }
                    }
                }
                
                // Migrate maHang DBRef to embedded
                if (chiTietDoc.containsKey("maHang")) {
                    Document maHangRef = (Document) chiTietDoc.get("maHang");
                    if (maHangRef.containsKey("$ref") && maHangRef.containsKey("$id")) {
                        String maHangId = maHangRef.getString("$id");
                        Document maHangDoc = danhMucCollection.find(Filters.eq("_id", new ObjectId(maHangId))).first();
                        
                        if (maHangDoc != null) {
                            Document maHangEmbedded = createDanhMucHangEmbedded(maHangDoc);
                            chiTietDoc.put("ma_hang", maHangEmbedded);
                            chiTietDoc.remove("maHang");
                        }
                    }
                }
                
                // Update the document
                chiTietCollection.replaceOne(
                    Filters.eq("_id", chiTietDoc.get("_id")), 
                    chiTietDoc
                );
                
                migratedCount++;
                LOG.debug("Migrated ChiTietNhapXuat document: {}", chiTietDoc.get("_id"));
                
            } catch (Exception e) {
                LOG.error("Failed to migrate ChiTietNhapXuat document {}: {}", 
                    chiTietDoc.get("_id"), e.getMessage());
            }
        }
        
        LOG.info("Migrated {} ChiTietNhapXuat documents", migratedCount);
    }

    private void migratePhieuNhapXuat() {
        LOG.info("Migrating PhieuNhapXuat collection...");
        
        MongoCollection<Document> phieuCollection = mongoTemplate.getCollection("PhieuNhapXuat");
        MongoCollection<Document> khachHangCollection = mongoTemplate.getCollection("KhachHang");
        
        List<Document> phieuDocuments = phieuCollection.find().into(new ArrayList<>());
        int migratedCount = 0;
        
        for (Document phieuDoc : phieuDocuments) {
            try {
                // Check if document already has embedded structure
                if (phieuDoc.containsKey("khach_hang") && 
                    phieuDoc.get("khach_hang") instanceof Document) {
                    Document khachHangEmbedded = (Document) phieuDoc.get("khach_hang");
                    if (khachHangEmbedded.containsKey("id") && !khachHangEmbedded.containsKey("$ref")) {
                        LOG.debug("Document {} already migrated, skipping", phieuDoc.get("_id"));
                        continue;
                    }
                }
                
                // Migrate khachHang DBRef to embedded - check both field names
                Document khachHangRef = null;
                if (phieuDoc.containsKey("khachHang")) {
                    khachHangRef = (Document) phieuDoc.get("khachHang");
                } else if (phieuDoc.containsKey("khach_hang")) {
                    khachHangRef = (Document) phieuDoc.get("khach_hang");
                }
                
                if (khachHangRef != null && khachHangRef.containsKey("$ref") && khachHangRef.containsKey("$id")) {
                    String khachHangId = khachHangRef.getString("$id");
                    LOG.debug("Found DBRef for khachHang: {}", khachHangId);
                    
                    Document khachHangDoc = khachHangCollection.find(Filters.eq("_id", new ObjectId(khachHangId))).first();
                    
                    if (khachHangDoc != null) {
                        Document khachHangEmbedded = createKhachHangEmbedded(khachHangDoc);
                        phieuDoc.put("khach_hang", khachHangEmbedded);
                        
                        // Remove old field regardless of name
                        phieuDoc.remove("khachHang");
                        phieuDoc.remove("khach_hang");
                        
                        // Add the new embedded field
                        phieuDoc.put("khach_hang", khachHangEmbedded);
                        
                        LOG.debug("Created embedded khachHang for phieu: {}", phieuDoc.get("_id"));
                    } else {
                        LOG.warn("KhachHang with id {} not found for phieu: {}", khachHangId, phieuDoc.get("_id"));
                    }
                } else {
                    LOG.debug("No DBRef found for khachHang in phieu: {}", phieuDoc.get("_id"));
                }
                
                // Update the document
                phieuCollection.replaceOne(
                    Filters.eq("_id", phieuDoc.get("_id")), 
                    phieuDoc
                );
                
                migratedCount++;
                LOG.debug("Migrated PhieuNhapXuat document: {}", phieuDoc.get("_id"));
                
            } catch (Exception e) {
                LOG.error("Failed to migrate PhieuNhapXuat document {}: {}", 
                    phieuDoc.get("_id"), e.getMessage(), e);
            }
        }
        
        LOG.info("Migrated {} PhieuNhapXuat documents", migratedCount);
    }

    private Document createPhieuNhapXuatEmbedded(Document phieuDoc) {
        Document embedded = new Document();
        embedded.put("id", phieuDoc.get("_id").toString());
        embedded.put("ma_phieu", phieuDoc.get("ma_phieu"));
        embedded.put("ngay_lap_phieu", phieuDoc.get("ngay_lap_phieu"));
        embedded.put("loai_phieu", phieuDoc.get("loai_phieu"));
        embedded.put("created_at", phieuDoc.get("created_at"));
        embedded.put("created_by", phieuDoc.get("created_by"));
        embedded.put("updated_at", phieuDoc.get("updated_at"));
        embedded.put("updated_by", phieuDoc.get("updated_by"));
        embedded.put("is_deleted", phieuDoc.get("is_deleted"));
        
        // Handle khachHang if exists
        if (phieuDoc.containsKey("khach_hang")) {
            Document khachHangEmbedded = (Document) phieuDoc.get("khach_hang");
            embedded.put("khach_hang", khachHangEmbedded);
        }
        
        return embedded;
    }

    private Document createDanhMucHangEmbedded(Document maHangDoc) {
        Document embedded = new Document();
        embedded.put("id", maHangDoc.get("_id").toString());
        embedded.put("ma_hang", maHangDoc.get("ma_hang"));
        embedded.put("ten_hang", maHangDoc.get("ten_hang"));
        embedded.put("don_vitinh", maHangDoc.get("don_vitinh"));
        embedded.put("noi_san_xuat", maHangDoc.get("noi_san_xuat"));
        embedded.put("ngay_san_xuat", maHangDoc.get("ngay_san_xuat"));
        embedded.put("han_su_dung", maHangDoc.get("han_su_dung"));
        embedded.put("created_at", maHangDoc.get("created_at"));
        embedded.put("created_by", maHangDoc.get("created_by"));
        embedded.put("updated_at", maHangDoc.get("updated_at"));
        embedded.put("updated_by", maHangDoc.get("updated_by"));
        embedded.put("is_deleted", maHangDoc.get("is_deleted"));
        
        return embedded;
    }

    private Document createKhachHangEmbedded(Document khachHangDoc) {
        Document embedded = new Document();
        embedded.put("id", khachHangDoc.get("_id").toString());
        embedded.put("ma_kh", khachHangDoc.get("ma_kh"));
        embedded.put("ten_kh", khachHangDoc.get("ten_kh"));
        embedded.put("goi_tinh", khachHangDoc.get("goi_tinh"));
        embedded.put("date_of_birth", khachHangDoc.get("date_of_birth"));
        embedded.put("dia_chi", khachHangDoc.get("dia_chi"));
        embedded.put("created_at", khachHangDoc.get("created_at"));
        embedded.put("created_by", khachHangDoc.get("created_by"));
        embedded.put("updated_at", khachHangDoc.get("updated_at"));
        embedded.put("updated_by", khachHangDoc.get("updated_by"));
        embedded.put("is_deleted", khachHangDoc.get("is_deleted"));
        
        return embedded;
    }
} 