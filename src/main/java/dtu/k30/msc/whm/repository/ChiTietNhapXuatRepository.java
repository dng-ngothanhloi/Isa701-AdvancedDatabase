package dtu.k30.msc.whm.repository;

import dtu.k30.msc.whm.domain.ChiTietNhapXuat;
import dtu.k30.msc.whm.repository.custom.ChiTietNhapXuatRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the ChiTietNhapXuat entity.
 */
@Repository
public interface ChiTietNhapXuatRepository extends MongoRepository<ChiTietNhapXuat, String>, ChiTietNhapXuatRepositoryCustom {
    
    /**
     * Find transaction details by PhieuNhapXuat ID using embedded document structure
     */
    @Query("{ 'phieu_nhap_xuat.id': ?0 }")
    List<ChiTietNhapXuat> findByPhieuNhapXuatId(String phieuNhapXuatId);
    
    /**
     * Find transaction details by product ID using embedded document structure
     */
    @Query("{ 'ma_hang.id': ?0 }")
    List<ChiTietNhapXuat> findByMaHangId(String maHangId);
    
    /**
     * Find transaction details by customer ID using embedded document structure
     */
    @Query("{ 'phieu_nhap_xuat.khach_hang.id': ?0 }")
    List<ChiTietNhapXuat> findByKhachHangId(String khachHangId);
}
