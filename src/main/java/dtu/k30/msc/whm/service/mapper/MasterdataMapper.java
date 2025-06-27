package dtu.k30.msc.whm.service.mapper;

import dtu.k30.msc.whm.domain.Masterdata;
import dtu.k30.msc.whm.service.dto.MasterdataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Masterdata} and its DTO {@link MasterdataDTO}.
 */
@Mapper(componentModel = "spring")
public interface MasterdataMapper extends EntityMapper<MasterdataDTO, Masterdata> {}
