package dtu.k30.msc.whm.service.mapper;

import dtu.k30.msc.whm.domain.KhachHang;
import dtu.k30.msc.whm.service.dto.KhachHangDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link KhachHang} and its DTO {@link KhachHangDTO}.
 */
@Mapper(componentModel = "spring")
public interface KhachHangMapper extends EntityMapper<KhachHangDTO, KhachHang> {}
