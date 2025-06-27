package dtu.k30.msc.whm.service.mapper;

import dtu.k30.msc.whm.domain.DanhMucHang;
import dtu.k30.msc.whm.domain.DanhMucHangEmbedded;
import dtu.k30.msc.whm.service.dto.DanhMucHangDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DanhMucHang} and its DTO {@link DanhMucHangDTO}.
 */
@Mapper(componentModel = "spring")
public interface DanhMucHangMapper extends EntityMapper<DanhMucHangDTO, DanhMucHang> {
    
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "maHang", source = "maHang")
    @Mapping(target = "tenHang", source = "tenHang")
    @Mapping(target = "donVitinh", source = "donVitinh")
    @Mapping(target = "noiSanXuat", source = "noiSanXuat")
    @Mapping(target = "ngaySanXuat", source = "ngaySanXuat")
    @Mapping(target = "hanSuDung", source = "hanSuDung")
    DanhMucHangDTO toDto(DanhMucHangEmbedded danhMucHangEmbedded);
}
