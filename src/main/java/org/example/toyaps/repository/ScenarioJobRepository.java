package org.example.toyaps.repository;

import org.example.toyaps.domain.entity.ScenarioJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenarioJobRepository extends JpaRepository<ScenarioJob, Long> {
}
