package dtu.k30.msc.whm.service.mapper;

import dtu.k30.msc.whm.domain.DanhMucHang;
import dtu.k30.msc.whm.service.dto.DanhMucHangDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DanhMucHang} and its DTO {@link DanhMucHangDTO}.
 */
@Mapper(componentModel = "spring")
public interface DanhMucHangMapper extends EntityMapper<DanhMucHangDTO, DanhMucHang> {
}
