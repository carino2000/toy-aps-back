package org.example.toyaps.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.example.toyaps.domain.entity.Job;
import org.example.toyaps.dto.request.JobBulkUpsertRequest;
import org.example.toyaps.dto.response.JobListResponse;
import org.example.toyaps.dto.response.JobParseResponse;
import org.example.toyaps.dto.response.JobUpsertResponse;
import org.example.toyaps.repository.JobRepository;
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
@RequestMapping("/api/jobs")
public class JobController {
    final JobRepository jobRepository;

    @PostMapping("/parse/xls")
    public ResponseEntity<?> handlePostParseXls(@RequestParam MultipartFile file) throws IOException {
        if (file.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");

        // 자바의 엑셀 객체
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> iterator = sheet.rowIterator();
        Row header = iterator.next();
//        System.out.println(header.getCell(0).getStringCellValue());
//        System.out.println(header.getCell(1).getStringCellValue());
//        System.out.println(header.getCell(2).getStringCellValue());
//        System.out.println(header.getCell(3).getStringCellValue());
        DataFormatter formatter = new DataFormatter(); // 무조건 다 String 으로 받게 해주는 포멧터

        List<JobParseResponse.Item> items = new ArrayList<>();
        while (iterator.hasNext()) {
            Row row = iterator.next();
//            System.out.println(formatter.formatCellValue(row.getCell(0)));
//            System.out.println(formatter.formatCellValue(row.getCell(1)));
//            System.out.println(formatter.formatCellValue(row.getCell(2)));
//            System.out.println(formatter.formatCellValue(row.getCell(3)));
            JobParseResponse.Item item = new JobParseResponse
                    .Item(formatter.formatCellValue(row.getCell(0)),
                    formatter.formatCellValue(row.getCell(1)),
                    formatter.formatCellValue(row.getCell(2)),
                    Boolean.parseBoolean(formatter.formatCellValue(row.getCell(3))));
            items.add(item);
        }
        JobParseResponse resp = new JobParseResponse();
        resp.setItems(items);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PostMapping
    public ResponseEntity<?> handlePostJob(@RequestBody JobBulkUpsertRequest jbr) {
        List<Job> myJobs = jobRepository.findAll();
        List<String> jobIds = jbr.getJobs().stream().map(m -> m.getId()).toList();
        List<Job> notContainsJobs = myJobs.stream().filter(f -> {
            return !jobIds.contains(f.getId());
        }).toList();

        List<Job> upsertJobs = jbr.getJobs().stream()
                .map(m -> Job.builder()
                        .id(m.getId())
                        .name(m.getName())
                        .description(m.getDescription())
                        .active(m.isActive()).build()).toList();
        jobRepository.deleteAll(notContainsJobs);
        jobRepository.saveAll(upsertJobs);

        int delete = notContainsJobs.size();
        int update = myJobs.size() - delete;
        int created = upsertJobs.size() - update;

        JobUpsertResponse resp = JobUpsertResponse.builder().created(created).deleted(delete).updated(update).build();
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @GetMapping
    public ResponseEntity<?> handleGetAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(JobListResponse.builder().jobs(jobs).build());
    }
}
