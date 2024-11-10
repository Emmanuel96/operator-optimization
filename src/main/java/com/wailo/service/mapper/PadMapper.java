package com.wailo.service.mapper;

import com.wailo.domain.Pad;
import com.wailo.service.dto.PadDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pad} and its DTO {@link PadDTO}.
 */
@Mapper(componentModel = "spring")
public interface PadMapper extends EntityMapper<PadDTO, Pad> {}
