package dtu.k30.msc.whm.service.mapper;

import dtu.k30.msc.whm.domain.ChiTietNhapXuat;
import dtu.k30.msc.whm.domain.DanhMucHang;
import dtu.k30.msc.whm.domain.DanhMucHangEmbedded;
import dtu.k30.msc.whm.domain.PhieuNhapXuat;
import dtu.k30.msc.whm.domain.PhieuNhapXuatEmbedded;
import dtu.k30.msc.whm.service.dto.ChiTietNhapXuatDTO;
import dtu.k30.msc.whm.service.dto.DanhMucHangDTO;
import dtu.k30.msc.whm.service.dto.PhieuNhapXuatDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChiTietNhapXuat} and its DTO {@link ChiTietNhapXuatDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChiTietNhapXuatMapper extends EntityMapper<ChiTietNhapXuatDTO, ChiTietNhapXuat> {
    @Mapping(target = "phieuNhapXuat", source = "phieuNhapXuat", qualifiedByName = "phieuNhapXuatEmbeddedToDto")
    @Mapping(target = "maHang", source = "maHang", qualifiedByName = "danhMucHangEmbeddedToDto")
    ChiTietNhapXuatDTO toDto(ChiTietNhapXuat s);

    @Named("phieuNhapXuatId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PhieuNhapXuatDTO toDtoPhieuNhapXuatId(PhieuNhapXuat phieuNhapXuat);

    @Named("phieuNhapXuatEmbeddedToDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "maPhieu", source = "maPhieu")
    @Mapping(target = "ngayLapPhieu", source = "ngayLapPhieu")
    @Mapping(target = "loaiPhieu", source = "loaiPhieu")
    PhieuNhapXuatDTO toDtoPhieuNhapXuatId(PhieuNhapXuatEmbedded phieuNhapXuatEmbedded);

    @Named("danhMucHangId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DanhMucHangDTO toDtoDanhMucHangId(DanhMucHang danhMucHang);

    @Named("danhMucHangEmbeddedToDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "maHang", source = "maHang")
    @Mapping(target = "tenHang", source = "tenHang")
    @Mapping(target = "donVitinh", source = "donVitinh")
    @Mapping(target = "noiSanXuat", source = "noiSanXuat")
    @Mapping(target = "ngaySanXuat", source = "ngaySanXuat")
    @Mapping(target = "hanSuDung", source = "hanSuDung")
    DanhMucHangDTO toDtoDanhMucHangId(DanhMucHangEmbedded danhMucHangEmbedded);
}
