package dtu.k30.msc.whm.repository;

import dtu.k30.msc.whm.domain.ChiTietNhapXuat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ChiTietNhapXuat entity.
 */
@Repository
public interface ChiTietNhapXuatRepository extends MongoRepository<ChiTietNhapXuat, String> {
}
