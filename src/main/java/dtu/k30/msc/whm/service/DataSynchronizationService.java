package dtu.k30.msc.whm.service;

import dtu.k30.msc.whm.domain.ChiTietNhapXuat;
import dtu.k30.msc.whm.domain.DanhMucHang;
import dtu.k30.msc.whm.domain.DanhMucHangEmbedded;
import dtu.k30.msc.whm.domain.KhachHang;
import dtu.k30.msc.whm.domain.KhachHangEmbedded;
import dtu.k30.msc.whm.domain.PhieuNhapXuat;
import dtu.k30.msc.whm.domain.PhieuNhapXuatEmbedded;
import dtu.k30.msc.whm.repository.ChiTietNhapXuatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for synchronizing data between main entities and embedded documents.
 * This ensures data consistency when related entities are updated.
 * 
 * Selective embedding optimization: Only essential fields are synchronized
 * to reduce storage usage and improve performance.
 */
@Service
public class DataSynchronizationService {

    private static final Logger LOG = LoggerFactory.getLogger(DataSynchronizationService.class);

    @Autowired
    private ChiTietNhapXuatRepository chiTietNhapXuatRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Update embedded DanhMucHang data when the main entity is updated
     * Selective embedding: Only essential fields (id, maHang, tenHang, donviTinh)
     */
    public void updateEmbeddedDanhMucHang(DanhMucHang danhMucHang) {
        LOG.debug("Updating embedded DanhMucHang data for ID: {}", danhMucHang.getId());

        DanhMucHangEmbedded embedded = new DanhMucHangEmbedded();
        embedded.setId(danhMucHang.getId());
        embedded.setMaHang(danhMucHang.getMaHang());
        embedded.setTenHang(danhMucHang.getTenHang());
        embedded.setDonviTinh(danhMucHang.getDonviTinh());
        // Removed: noiSanXuat, ngaySanXuat, hanSuDung, audit fields for storage optimization

        Query query = new Query(Criteria.where("ma_hang.id").is(danhMucHang.getId()));
        Update update = new Update().set("ma_hang", embedded);

        mongoTemplate.updateMulti(query, update, ChiTietNhapXuat.class);
        LOG.debug("Updated embedded DanhMucHang data for {} documents", 
            mongoTemplate.count(query, ChiTietNhapXuat.class));
    }

    /**
     * Update embedded KhachHang data when the main entity is updated
     * Selective embedding: Only essential fields (id, maKH, tenKH)
     */
    public void updateEmbeddedKhachHang(KhachHang khachHang) {
        LOG.debug("Updating embedded KhachHang data for ID: {}", khachHang.getId());

        KhachHangEmbedded embedded = new KhachHangEmbedded();
        embedded.setId(khachHang.getId());
        embedded.setMaKH(khachHang.getMaKH());
        embedded.setTenKH(khachHang.getTenKH());
        // Removed: goiTinh, dateOfBirth, diaChi, audit fields for storage optimization

        Query query = new Query(Criteria.where("phieu_nhap_xuat.khach_hang.id").is(khachHang.getId()));
        Update update = new Update().set("phieu_nhap_xuat.khach_hang", embedded);

        mongoTemplate.updateMulti(query, update, ChiTietNhapXuat.class);
        LOG.debug("Updated embedded KhachHang data for {} documents", 
            mongoTemplate.count(query, ChiTietNhapXuat.class));
    }

    /**
     * Update embedded PhieuNhapXuat data when the main entity is updated
     * Selective embedding: Only essential fields (id, maPhieu, ngayLapPhieu, loaiPhieu)
     */
    public void updateEmbeddedPhieuNhapXuat(PhieuNhapXuat phieuNhapXuat, KhachHang khachHang) {
        LOG.debug("Updating embedded PhieuNhapXuat data for ID: {}", phieuNhapXuat.getId());

        PhieuNhapXuatEmbedded embedded = new PhieuNhapXuatEmbedded();
        embedded.setId(phieuNhapXuat.getId());
        embedded.setMaPhieu(phieuNhapXuat.getMaPhieu());
        embedded.setNgayLapPhieu(phieuNhapXuat.getNgayLapPhieu());
        embedded.setLoaiPhieu(phieuNhapXuat.getLoaiPhieu());
        // Removed: audit fields and khachHang (already embedded in main PhieuNhapXuat)

        Query query = new Query(Criteria.where("phieu_nhap_xuat.id").is(phieuNhapXuat.getId()));
        Update update = new Update().set("phieu_nhap_xuat", embedded);

        mongoTemplate.updateMulti(query, update, ChiTietNhapXuat.class);
        LOG.debug("Updated embedded PhieuNhapXuat data for {} documents", 
            mongoTemplate.count(query, ChiTietNhapXuat.class));
    }

    /**
     * Create embedded documents for a new ChiTietNhapXuat
     * Selective embedding: Only essential fields for storage optimization
     */
    public void createEmbeddedDocuments(ChiTietNhapXuat chiTietNhapXuat, 
                                       PhieuNhapXuat phieuNhapXuat, 
                                       DanhMucHang danhMucHang,
                                       KhachHang khachHang) {
        LOG.debug("Creating embedded documents for ChiTietNhapXuat ID: {}", chiTietNhapXuat.getId());

        // Create embedded DanhMucHang with minimal fields
        if (danhMucHang != null) {
            DanhMucHangEmbedded danhMucHangEmbedded = new DanhMucHangEmbedded();
            danhMucHangEmbedded.setId(danhMucHang.getId());
            danhMucHangEmbedded.setMaHang(danhMucHang.getMaHang());
            danhMucHangEmbedded.setTenHang(danhMucHang.getTenHang());
            danhMucHangEmbedded.setDonviTinh(danhMucHang.getDonviTinh());
            // Removed: noiSanXuat, ngaySanXuat, hanSuDung, audit fields
            chiTietNhapXuat.setMaHang(danhMucHangEmbedded);
        }

        // Create embedded PhieuNhapXuat with minimal fields
        if (phieuNhapXuat != null) {
            PhieuNhapXuatEmbedded phieuNhapXuatEmbedded = new PhieuNhapXuatEmbedded();
            phieuNhapXuatEmbedded.setId(phieuNhapXuat.getId());
            phieuNhapXuatEmbedded.setMaPhieu(phieuNhapXuat.getMaPhieu());
            phieuNhapXuatEmbedded.setNgayLapPhieu(phieuNhapXuat.getNgayLapPhieu());
            phieuNhapXuatEmbedded.setLoaiPhieu(phieuNhapXuat.getLoaiPhieu());
            // Removed: audit fields and khachHang (already embedded in main PhieuNhapXuat)
            chiTietNhapXuat.setPhieuNhapXuat(phieuNhapXuatEmbedded);
        }
    }
} 