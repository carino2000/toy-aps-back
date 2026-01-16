package org.example.toyaps.repository;

import org.example.toyaps.domain.entity.ScenarioSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenarioScheduleRepository extends JpaRepository<ScenarioSchedule,Long> {
}
