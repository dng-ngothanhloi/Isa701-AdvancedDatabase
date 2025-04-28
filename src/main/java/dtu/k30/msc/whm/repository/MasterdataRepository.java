package dtu.k30.msc.whm.repository;

import dtu.k30.msc.whm.domain.Masterdata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Masterdata entity.
 */
@Repository
public interface MasterdataRepository extends MongoRepository<Masterdata, String> {
}
