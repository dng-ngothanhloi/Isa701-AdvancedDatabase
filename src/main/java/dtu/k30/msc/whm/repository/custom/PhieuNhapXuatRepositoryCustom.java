package dtu.k30.msc.whm.repository.custom;

import dtu.k30.msc.whm.service.dto.PhieuNhapXuatDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PhieuNhapXuatRepositoryCustom {
    Page<PhieuNhapXuatDTO> findAllWithName(Pageable pageable);
}
