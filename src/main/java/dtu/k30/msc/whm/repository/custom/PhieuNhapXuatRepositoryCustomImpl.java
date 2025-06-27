package dtu.k30.msc.whm.repository.custom;

import dtu.k30.msc.whm.domain.KhachHang;
import dtu.k30.msc.whm.domain.PhieuNhapXuat;
import dtu.k30.msc.whm.domain.enumeration.VoucherType;
import dtu.k30.msc.whm.service.dto.PhieuNhapXuatDTO;
import dtu.k30.msc.whm.service.mapper.PhieuNhapXuatMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;


public class PhieuNhapXuatRepositoryCustomImpl implements PhieuNhapXuatRepositoryCustom {
    private final MongoTemplate mongoTemplate;
    private final PhieuNhapXuatMapper phieuNhapXuatMapper;

    public PhieuNhapXuatRepositoryCustomImpl(MongoTemplate mongoTemplate, PhieuNhapXuatMapper phieuNhapXuatMapper) {
        this.mongoTemplate = mongoTemplate;
        this.phieuNhapXuatMapper = phieuNhapXuatMapper;
    }

    @Override
    public Page<PhieuNhapXuatDTO> findAllWithName(Pageable pageable) {
       return null;
    }
}
