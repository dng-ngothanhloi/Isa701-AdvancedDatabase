package dtu.k30.msc.whm.service.mapper;

import dtu.k30.msc.whm.domain.KhachHang;
import dtu.k30.msc.whm.domain.KhachHangEmbedded;
import dtu.k30.msc.whm.domain.PhieuNhapXuat;
import dtu.k30.msc.whm.service.dto.KhachHangDTO;
import dtu.k30.msc.whm.service.dto.PhieuNhapXuatDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PhieuNhapXuat} and its DTO {@link PhieuNhapXuatDTO}.
 * Selective embedding: Only essential fields are mapped for performance optimization.
 */
@Mapper(componentModel = "spring")
public interface PhieuNhapXuatMapper extends EntityMapper<PhieuNhapXuatDTO, PhieuNhapXuat> {
    @Mapping(target = "khachHang", source = "khachHang", qualifiedByName = "khachHangEmbeddedToDto")
    @Mapping(target = "tenKhachHang", ignore = true) // Will be set manually in service
    @Mapping(target = "chiTietNhapXuatDTOList", ignore = true) // Will be set manually if needed
    PhieuNhapXuatDTO toDto(PhieuNhapXuat s);

    @Named("khachHangId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    KhachHangDTO toDtoKhachHangId(KhachHang khachHang);

    @Named("khachHangEmbeddedToDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "maKH", source = "maKH")
    @Mapping(target = "tenKH", source = "tenKH")
    // Removed: goiTinh, dateOfBirth, diaChi for selective embedding
    KhachHangDTO toDtoKhachHangId(KhachHangEmbedded khachHangEmbedded);
}
