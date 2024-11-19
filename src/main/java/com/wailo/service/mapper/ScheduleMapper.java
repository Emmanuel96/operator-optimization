package com.wailo.service.mapper;

import com.wailo.domain.planner.Schedule;
import com.wailo.service.dto.ScheduleDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScheduleMapper extends EntityMapper<ScheduleDTO, Schedule> {
}
