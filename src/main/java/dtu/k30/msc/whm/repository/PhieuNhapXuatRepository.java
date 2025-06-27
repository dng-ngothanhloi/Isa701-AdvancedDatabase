package dtu.k30.msc.whm.repository;

import dtu.k30.msc.whm.domain.PhieuNhapXuat;
import dtu.k30.msc.whm.repository.custom.PhieuNhapXuatRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the PhieuNhapXuat entity.
 */
@Repository
public interface PhieuNhapXuatRepository extends MongoRepository<PhieuNhapXuat, String>, PhieuNhapXuatRepositoryCustom {}
