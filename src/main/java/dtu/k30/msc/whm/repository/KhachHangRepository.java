package dtu.k30.msc.whm.repository;

import dtu.k30.msc.whm.domain.KhachHang;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the KhachHang entity.
 */
@Repository
public interface KhachHangRepository extends MongoRepository<KhachHang, String> {}
