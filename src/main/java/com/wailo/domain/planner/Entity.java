package com.wailo.domain.planner;

import com.wailo.domain.Location;
import com.wailo.domain.OperationalItem;
import lombok.Data;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@Data
@PlanningEntity
public class Entity {

    @PlanningId
    private Long id;

    @PlanningVariable
    private Location location;

    @PlanningVariable
    private OperationalItem operation;

//    @PlanningVariable
//    private Timeslot timeslot;
}
