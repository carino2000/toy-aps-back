package org.example.toyaps.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.toyaps.domain.entity.*;
import org.example.toyaps.dto.api_response.SolveApiResult;
import org.example.toyaps.dto.request.ScenarioCreateRequest;
import org.example.toyaps.dto.response.ScenarioCreateResponse;
import org.example.toyaps.dto.response.ScenarioListResponse;
import org.example.toyaps.dto.response.SimulationListResponse;
import org.example.toyaps.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/scenarios")
public class ScenarioController {
    final JobRepository jobRepository;
    final ScenarioRepository scenarioRepository;
    final ScenarioJobRepository scenarioJobRepository;
    final TaskRepository taskRepository;
    final ScenarioScheduleRepository scenarioScheduleRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<?> handlePostScenarioJob(@RequestBody ScenarioCreateRequest scr) {

        Scenario scenario = scr.toScenario();
        scenarioRepository.save(scenario);
        List<Job> jobs = new ArrayList<>();
        List<ScenarioJob> scenarioJobs = jobRepository.findAll().stream()
                .filter(f -> scr.getJobIds().contains(f.getId()))
                .map(m -> {
                    jobs.add(m);
                    return ScenarioJob.builder().scenario(scenario).job(m).build();
                }).toList();
        scenarioJobRepository.saveAll(scenarioJobs);

        ScenarioCreateResponse resp = new ScenarioCreateResponse();
        resp.setScenario(scenario);
        resp.setJobs(jobs);

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping("/{scenarioId}")
    public ResponseEntity<?> getScenarioJob(@PathVariable("scenarioId") String scenarioId) {
        return ResponseEntity.ok().body(scenarioRepository.findById(scenarioId));
    }

    @PostMapping("/{scenarioId}/simulate")
    public ResponseEntity<?> handlePostSimulateScenario(@PathVariable("scenarioId") String scenarioId) {

        Optional<Scenario> target = scenarioRepository.findById(scenarioId);
        if (target.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Scenario scenario = target.get();
        RestClient restClient = RestClient.create();
        SolveApiResult result = restClient.post()
                .uri("http://127.0.0.1:5000/api/solve")
                .body(scenario).retrieve()
                .body(SolveApiResult.class);

        // 객체로 바꾸고 가공해서 보내주는 코드 작성란
        scenario.setStatus(result.getStatus());
        scenario.setMakespan(result.getMakespan());
        scenarioRepository.save(scenario);

        List<ScenarioSchedule> ss = result.getTimeline().stream().map(m -> {
            return ScenarioSchedule.builder()
                    .scenario(scenario)
                    .task(taskRepository.findById(m.getTaskId()).orElse(null))
                    .startAt(scenario.getScheduleAt().plusHours(m.getStart()))
                    .endAt(scenario.getScheduleAt().plusHours(m.getEnd()))
                    .build();
        }).toList();

        scenarioScheduleRepository.saveAll(ss);

        Map<String, Object> resp = Map.of("status", scenario.getStatus());

        return ResponseEntity.ok().body(resp);
    }

    @GetMapping("/{scenarioId}/json")
    public ResponseEntity<?> getScenarioJobJson(@PathVariable("scenarioId") String scenarioId) {
        return ResponseEntity.ok(scenarioRepository.findById(scenarioId).orElseThrow());
    }

    @GetMapping
    public ResponseEntity<?> handleGetAllScenarios() {
        List<Scenario> scenarios = scenarioRepository.findAll();
        //Map<String, Object> resp = Map.of("scenarios", scenarios);

        List<ScenarioListResponse.Item> items = scenarios.stream().map(m -> {
            return ScenarioListResponse.Item.builder()
                    .id(m.getId())
                    .description(m.getDescription())
                    .status(m.getStatus())
                    .jobCount(m.getScenarioJobs().size())
                    .build();
        }).toList();

        return ResponseEntity.ok().body(ScenarioListResponse.builder().scenarios(items).build());
    }

    @GetMapping("/{scenarioId}/simulate")
    public ResponseEntity<?> handleGetScenarioSimulate(@PathVariable("scenarioId") String scenarioId) throws IllegalAccessException {
        Scenario scenario = scenarioRepository.findById(scenarioId).orElseThrow(() -> new NoSuchElementException()); //기본이 NoSearchElementException 인데, 이렇게 바꿀수도 있음
        if (scenario.getStatus().equals("READY")) {
            throw new IllegalAccessException();
        }

        List<ScenarioSchedule> allSchedules = scenarioScheduleRepository.findAll();

        List<ScenarioSchedule> selectedSchedules = allSchedules.stream().filter(f -> f.getScenario().equals(scenario)).toList();

        List<SimulationListResponse.Item> items =
                selectedSchedules.stream().map(SimulationListResponse.Item::fromEntity).toList();

        return ResponseEntity.ok().body(SimulationListResponse.builder().scenarioItems(items).build());
    }
}
