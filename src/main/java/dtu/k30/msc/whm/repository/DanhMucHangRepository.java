package dtu.k30.msc.whm.repository;

import dtu.k30.msc.whm.domain.DanhMucHang;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the DanhMucHang entity.
 */
@Repository
public interface DanhMucHangRepository extends MongoRepository<DanhMucHang, String> {
}
