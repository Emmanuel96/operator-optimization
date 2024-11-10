package com.wailo.service.mapper;

import com.wailo.domain.Location;
import com.wailo.domain.OperationalItem;
import com.wailo.service.dto.LocationDTO;
import com.wailo.service.dto.OperationalItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OperationalItem} and its DTO {@link OperationalItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface OperationalItemMapper extends EntityMapper<OperationalItemDTO, OperationalItem> {
    @Mapping(target = "location", source = "location", qualifiedByName = "locationId")
    OperationalItemDTO toDto(OperationalItem s);

    @Named("locationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LocationDTO toDtoLocationId(Location location);
}
