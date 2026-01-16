package org.example.toyaps.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.example.toyaps.domain.entity.Job;
import org.example.toyaps.domain.entity.Task;
import org.example.toyaps.dto.request.JobBulkUpsertRequest;
import org.example.toyaps.dto.request.TaskBulkUpsertRequest;
import org.example.toyaps.dto.response.*;
import org.example.toyaps.repository.JobRepository;
import org.example.toyaps.repository.TaskRepository;
import org.example.toyaps.repository.ToolRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/tasks")
public class TaskController {
    final TaskRepository taskRepository;
    final JobRepository jobRepository;
    final ToolRepository toolRepository;

    @PostMapping("/parse/xls")
    public ResponseEntity<?> handlePostParseXls(@RequestParam MultipartFile file) throws IOException {
        if (file.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");

        // 자바의 엑셀 객체
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> iterator = sheet.rowIterator();
        Row header = iterator.next();
        DataFormatter formatter = new DataFormatter();
        List<TaskParseResponse.Item> items = new ArrayList<>();
        while (iterator.hasNext()) {
            Row row = iterator.next();
            TaskParseResponse.Item item = TaskParseResponse.Item.builder()
                    .id(formatter.formatCellValue(row.getCell(0)))
                    .jobId(formatter.formatCellValue(row.getCell(1)))
                    .seq(Integer.parseInt(formatter.formatCellValue(row.getCell(2))))
                    .name(formatter.formatCellValue(row.getCell(3)))
                    .description(formatter.formatCellValue(row.getCell(4)))
                    .toolId(formatter.formatCellValue(row.getCell(5)))
                    .duration(Integer.parseInt(formatter.formatCellValue(row.getCell(6))))
                    .build();
            items.add(item);
        }
        TaskParseResponse resp = TaskParseResponse.builder().items(items).build();
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> handlePostJob(@RequestBody TaskBulkUpsertRequest tbr) {
        List<Task> myTasks = taskRepository.findAll();
        List<String> taskIds = tbr.getTasks().stream().map(m -> m.getId()).toList();
        List<Task> notContainsTasks = myTasks.stream().filter(f -> {
            return !taskIds.contains(f.getId());
        }).toList();

        List<Task> upsertTasks = new ArrayList<>();
        try{
            upsertTasks = tbr.getTasks().stream()
                    .map(m -> Task.builder()
                            .id(m.getId())
                            .job(jobRepository.findById(m.getJobId()).orElseThrow(() -> new IllegalStateException("Job not found : " + m.getJobId())))
                            .tool(toolRepository.findById(m.getToolId()).orElse(null))
                            .seq(m.getSeq())
                            .name(m.getName())
                            .description(m.getDescription())
                            .duration(m.getDuration()).build()).toList();

            taskRepository.deleteAll(notContainsTasks);
            taskRepository.saveAll(upsertTasks);
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        int delete = notContainsTasks.size();
        int update = myTasks.size() - delete;
        int created = upsertTasks.size() - update;

        TaskUpsertResponse resp = TaskUpsertResponse.builder().created(created).deleted(delete).updated(update).build();
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @GetMapping
    public ResponseEntity<?> handleGetAllJobs() {
        List<Task> Tasks = taskRepository.findAll();
        List<TaskResponse.Item> items = Tasks.stream().map(m -> {
            return TaskResponse.Item.builder()
                    .id(m.getId())
                    .jobId(m.getJob().getId())
                    .tool(m.getTool())
                    .seq(m.getSeq())
                    .name(m.getName())
                    .description(m.getDescription())
                    .duration(m.getDuration()).build();
        }).toList();
        TaskResponse resp = TaskResponse.builder().tasks(items).build();
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<?> handleGetJobById(@PathVariable String jobId) {
        List<Task> tasks = taskRepository.findAll();
        List<Task> target = tasks.stream().filter(f -> f.getJob().getId().equals(jobId)).toList();
        return ResponseEntity.status(HttpStatus.OK).body(TaskListResponse.builder().tasks(target).build());
    }

}
