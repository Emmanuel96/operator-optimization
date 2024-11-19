package com.wailo.domain.planner;

import com.wailo.domain.Location;
import com.wailo.domain.OperationalItem;
import lombok.Data;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@Data
@PlanningSolution
public class Schedule {
    @ValueRangeProvider
    @ProblemFactCollectionProperty
    private List<Location> locations;

    @ValueRangeProvider
    @ProblemFactCollectionProperty
    private List<OperationalItem> operations;

    @PlanningEntityCollectionProperty
    private List<Entity> entities;

    @PlanningScore
    private HardSoftScore score;
}
