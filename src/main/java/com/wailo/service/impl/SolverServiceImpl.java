package com.wailo.service.impl;

import com.wailo.domain.planner.Schedule;
import com.wailo.repository.LocationRepository;
import com.wailo.service.SolverService;
import com.wailo.service.dto.ScheduleDTO;
import com.wailo.service.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class SolverServiceImpl implements SolverService {
    private final SolverManager<Schedule, UUID> solverManager;
    private final ScheduleMapper scheduleMapper;
    private final LocationRepository locationRepository;

    @Override
    public ScheduleDTO solve() {
        Schedule schedule = new Schedule();
        UUID problemId = UUID.randomUUID();
        SolverJob<Schedule, UUID> solverJob = solverManager.solve(problemId, schedule);

        Schedule solution;
        try {
            // Wait until the solving ends
            solution = solverJob.getFinalBestSolution();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Solving failed.", e);
        }
        return scheduleMapper.toDto(solution);
    }


}
