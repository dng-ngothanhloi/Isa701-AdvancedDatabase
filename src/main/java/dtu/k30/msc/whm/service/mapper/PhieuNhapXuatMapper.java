package dtu.k30.msc.whm.service.mapper;

import dtu.k30.msc.whm.domain.KhachHang;
import dtu.k30.msc.whm.domain.PhieuNhapXuat;
import dtu.k30.msc.whm.service.dto.KhachHangDTO;
import dtu.k30.msc.whm.service.dto.PhieuNhapXuatDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PhieuNhapXuat} and its DTO {@link PhieuNhapXuatDTO}.
 */
@Mapper(componentModel = "spring")
public interface PhieuNhapXuatMapper extends EntityMapper<PhieuNhapXuatDTO, PhieuNhapXuat> {
    @Mapping(target = "khachHang", source = "khachHang", qualifiedByName = "khachHangId")
    PhieuNhapXuatDTO toDto(PhieuNhapXuat s);

    @Named("khachHangId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    KhachHangDTO toDtoKhachHangId(KhachHang khachHang);
}
