package com.wailo.service.mapper;

import com.wailo.domain.Location;
import com.wailo.domain.Pad;
import com.wailo.service.dto.LocationDTO;
import com.wailo.service.dto.PadDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Location} and its DTO {@link LocationDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {
    @Mapping(target = "pad", source = "pad", qualifiedByName = "padId")
    LocationDTO toDto(Location s);

    @Named("padId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PadDTO toDtoPadId(Pad pad);
}
