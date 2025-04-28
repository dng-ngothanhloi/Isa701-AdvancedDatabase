package dtu.k30.msc.whm.service.mapper;

import dtu.k30.msc.whm.domain.ChiTietNhapXuat;
import dtu.k30.msc.whm.domain.DanhMucHang;
import dtu.k30.msc.whm.domain.PhieuNhapXuat;
import dtu.k30.msc.whm.service.dto.ChiTietNhapXuatDTO;
import dtu.k30.msc.whm.service.dto.DanhMucHangDTO;
import dtu.k30.msc.whm.service.dto.PhieuNhapXuatDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChiTietNhapXuat} and its DTO {@link ChiTietNhapXuatDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChiTietNhapXuatMapper extends EntityMapper<ChiTietNhapXuatDTO, ChiTietNhapXuat> {
    @Mapping(target = "phieuNhapXuat", source = "phieuNhapXuat", qualifiedByName = "phieuNhapXuatId")
    @Mapping(target = "maHang", source = "maHang", qualifiedByName = "danhMucHangId")
    ChiTietNhapXuatDTO toDto(ChiTietNhapXuat s);

    @Named("phieuNhapXuatId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PhieuNhapXuatDTO toDtoPhieuNhapXuatId(PhieuNhapXuat phieuNhapXuat);

    @Named("danhMucHangId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DanhMucHangDTO toDtoDanhMucHangId(DanhMucHang danhMucHang);
}
