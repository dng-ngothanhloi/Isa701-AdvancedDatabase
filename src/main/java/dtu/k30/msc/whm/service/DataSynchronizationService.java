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
     */
    public void updateEmbeddedDanhMucHang(DanhMucHang danhMucHang) {
        LOG.debug("Updating embedded DanhMucHang data for ID: {}", danhMucHang.getId());

        DanhMucHangEmbedded embedded = new DanhMucHangEmbedded();
        embedded.setId(danhMucHang.getId());
        embedded.setMaHang(danhMucHang.getMaHang());
        embedded.setTenHang(danhMucHang.getTenHang());
        embedded.setDonVitinh(danhMucHang.getDonVitinh());
        embedded.setNoiSanXuat(danhMucHang.getNoiSanXuat());
        embedded.setNgaySanXuat(danhMucHang.getNgaySanXuat());
        embedded.setHanSuDung(danhMucHang.getHanSuDung());
        embedded.setCreatedAt(danhMucHang.getCreatedAt());
        embedded.setCreatedBy(danhMucHang.getCreatedBy());
        embedded.setUpdatedAt(danhMucHang.getUpdatedAt());
        embedded.setUpdatedBy(danhMucHang.getUpdatedBy());
        embedded.setIsDeleted(danhMucHang.getIsDeleted());

        Query query = new Query(Criteria.where("ma_hang.id").is(danhMucHang.getId()));
        Update update = new Update().set("ma_hang", embedded);

        mongoTemplate.updateMulti(query, update, ChiTietNhapXuat.class);
        LOG.debug("Updated embedded DanhMucHang data for {} documents", 
            mongoTemplate.count(query, ChiTietNhapXuat.class));
    }

    /**
     * Update embedded KhachHang data when the main entity is updated
     */
    public void updateEmbeddedKhachHang(KhachHang khachHang) {
        LOG.debug("Updating embedded KhachHang data for ID: {}", khachHang.getId());

        KhachHangEmbedded embedded = new KhachHangEmbedded();
        embedded.setId(khachHang.getId());
        embedded.setMaKH(khachHang.getMaKH());
        embedded.setTenKH(khachHang.getTenKH());
        embedded.setGoiTinh(khachHang.getGoiTinh());
        embedded.setDateOfBirth(khachHang.getDateOfBirth());
        embedded.setDiaChi(khachHang.getDiaChi());
        embedded.setCreatedAt(khachHang.getCreatedAt());
        embedded.setCreatedBy(khachHang.getCreatedBy());
        embedded.setUpdatedAt(khachHang.getUpdatedAt());
        embedded.setUpdatedBy(khachHang.getUpdatedBy());
        embedded.setIsDeleted(khachHang.getIsDeleted());

        Query query = new Query(Criteria.where("phieu_nhap_xuat.khach_hang.id").is(khachHang.getId()));
        Update update = new Update().set("phieu_nhap_xuat.khach_hang", embedded);

        mongoTemplate.updateMulti(query, update, ChiTietNhapXuat.class);
        LOG.debug("Updated embedded KhachHang data for {} documents", 
            mongoTemplate.count(query, ChiTietNhapXuat.class));
    }

    /**
     * Update embedded PhieuNhapXuat data when the main entity is updated
     */
    public void updateEmbeddedPhieuNhapXuat(PhieuNhapXuat phieuNhapXuat, KhachHang khachHang) {
        LOG.debug("Updating embedded PhieuNhapXuat data for ID: {}", phieuNhapXuat.getId());

        PhieuNhapXuatEmbedded embedded = new PhieuNhapXuatEmbedded();
        embedded.setId(phieuNhapXuat.getId());
        embedded.setMaPhieu(phieuNhapXuat.getMaPhieu());
        embedded.setNgayLapPhieu(phieuNhapXuat.getNgayLapPhieu());
        embedded.setLoaiPhieu(phieuNhapXuat.getLoaiPhieu());
        embedded.setCreatedAt(phieuNhapXuat.getCreatedAt());
        embedded.setCreatedBy(phieuNhapXuat.getCreatedBy());
        embedded.setUpdatedAt(phieuNhapXuat.getUpdatedAt());
        embedded.setUpdatedBy(phieuNhapXuat.getUpdatedBy());
        embedded.setIsDeleted(phieuNhapXuat.getIsDeleted());

        // Set embedded KhachHang data
        if (khachHang != null) {
            KhachHangEmbedded khachHangEmbedded = new KhachHangEmbedded();
            khachHangEmbedded.setId(khachHang.getId());
            khachHangEmbedded.setMaKH(khachHang.getMaKH());
            khachHangEmbedded.setTenKH(khachHang.getTenKH());
            khachHangEmbedded.setGoiTinh(khachHang.getGoiTinh());
            khachHangEmbedded.setDateOfBirth(khachHang.getDateOfBirth());
            khachHangEmbedded.setDiaChi(khachHang.getDiaChi());
            khachHangEmbedded.setCreatedAt(khachHang.getCreatedAt());
            khachHangEmbedded.setCreatedBy(khachHang.getCreatedBy());
            khachHangEmbedded.setUpdatedAt(khachHang.getUpdatedAt());
            khachHangEmbedded.setUpdatedBy(khachHang.getUpdatedBy());
            khachHangEmbedded.setIsDeleted(khachHang.getIsDeleted());
            embedded.setKhachHang(khachHangEmbedded);
        }

        Query query = new Query(Criteria.where("phieu_nhap_xuat.id").is(phieuNhapXuat.getId()));
        Update update = new Update().set("phieu_nhap_xuat", embedded);

        mongoTemplate.updateMulti(query, update, ChiTietNhapXuat.class);
        LOG.debug("Updated embedded PhieuNhapXuat data for {} documents", 
            mongoTemplate.count(query, ChiTietNhapXuat.class));
    }

    /**
     * Create embedded documents for a new ChiTietNhapXuat
     */
    public void createEmbeddedDocuments(ChiTietNhapXuat chiTietNhapXuat, 
                                       PhieuNhapXuat phieuNhapXuat, 
                                       DanhMucHang danhMucHang,
                                       KhachHang khachHang) {
        LOG.debug("Creating embedded documents for ChiTietNhapXuat ID: {}", chiTietNhapXuat.getId());

        // Create embedded DanhMucHang
        if (danhMucHang != null) {
            DanhMucHangEmbedded danhMucHangEmbedded = new DanhMucHangEmbedded();
            danhMucHangEmbedded.setId(danhMucHang.getId());
            danhMucHangEmbedded.setMaHang(danhMucHang.getMaHang());
            danhMucHangEmbedded.setTenHang(danhMucHang.getTenHang());
            danhMucHangEmbedded.setDonVitinh(danhMucHang.getDonVitinh());
            danhMucHangEmbedded.setNoiSanXuat(danhMucHang.getNoiSanXuat());
            danhMucHangEmbedded.setNgaySanXuat(danhMucHang.getNgaySanXuat());
            danhMucHangEmbedded.setHanSuDung(danhMucHang.getHanSuDung());
            danhMucHangEmbedded.setCreatedAt(danhMucHang.getCreatedAt());
            danhMucHangEmbedded.setCreatedBy(danhMucHang.getCreatedBy());
            danhMucHangEmbedded.setUpdatedAt(danhMucHang.getUpdatedAt());
            danhMucHangEmbedded.setUpdatedBy(danhMucHang.getUpdatedBy());
            danhMucHangEmbedded.setIsDeleted(danhMucHang.getIsDeleted());
            chiTietNhapXuat.setMaHang(danhMucHangEmbedded);
        }

        // Create embedded PhieuNhapXuat with KhachHang
        if (phieuNhapXuat != null) {
            PhieuNhapXuatEmbedded phieuNhapXuatEmbedded = new PhieuNhapXuatEmbedded();
            phieuNhapXuatEmbedded.setId(phieuNhapXuat.getId());
            phieuNhapXuatEmbedded.setMaPhieu(phieuNhapXuat.getMaPhieu());
            phieuNhapXuatEmbedded.setNgayLapPhieu(phieuNhapXuat.getNgayLapPhieu());
            phieuNhapXuatEmbedded.setLoaiPhieu(phieuNhapXuat.getLoaiPhieu());
            phieuNhapXuatEmbedded.setCreatedAt(phieuNhapXuat.getCreatedAt());
            phieuNhapXuatEmbedded.setCreatedBy(phieuNhapXuat.getCreatedBy());
            phieuNhapXuatEmbedded.setUpdatedAt(phieuNhapXuat.getUpdatedAt());
            phieuNhapXuatEmbedded.setUpdatedBy(phieuNhapXuat.getUpdatedBy());
            phieuNhapXuatEmbedded.setIsDeleted(phieuNhapXuat.getIsDeleted());

            // Set embedded KhachHang
            if (khachHang != null) {
                KhachHangEmbedded khachHangEmbedded = new KhachHangEmbedded();
                khachHangEmbedded.setId(khachHang.getId());
                khachHangEmbedded.setMaKH(khachHang.getMaKH());
                khachHangEmbedded.setTenKH(khachHang.getTenKH());
                khachHangEmbedded.setGoiTinh(khachHang.getGoiTinh());
                khachHangEmbedded.setDateOfBirth(khachHang.getDateOfBirth());
                khachHangEmbedded.setDiaChi(khachHang.getDiaChi());
                khachHangEmbedded.setCreatedAt(khachHang.getCreatedAt());
                khachHangEmbedded.setCreatedBy(khachHang.getCreatedBy());
                khachHangEmbedded.setUpdatedAt(khachHang.getUpdatedAt());
                khachHangEmbedded.setUpdatedBy(khachHang.getUpdatedBy());
                khachHangEmbedded.setIsDeleted(khachHang.getIsDeleted());
                phieuNhapXuatEmbedded.setKhachHang(khachHangEmbedded);
            }

            chiTietNhapXuat.setPhieuNhapXuat(phieuNhapXuatEmbedded);
        }
    }
} 