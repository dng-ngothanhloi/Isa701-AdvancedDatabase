package dtu.k30.msc.whm.service.mapper;

import dtu.k30.msc.whm.domain.DanhMucHang;
import dtu.k30.msc.whm.domain.DanhMucHangEmbedded;
import dtu.k30.msc.whm.service.dto.DanhMucHangDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DanhMucHang} and its DTO {@link DanhMucHangDTO}.
 * Selective embedding: Only essential fields are mapped for performance optimization.
 */
@Mapper(componentModel = "spring")
public interface DanhMucHangMapper extends EntityMapper<DanhMucHangDTO, DanhMucHang> {
    
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "maHang", source = "maHang")
    @Mapping(target = "tenHang", source = "tenHang")
    @Mapping(target = "donviTinh", source = "donviTinh")
    DanhMucHangDTO toDto(DanhMucHangEmbedded danhMucHangEmbedded);
}
