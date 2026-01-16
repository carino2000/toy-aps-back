package org.example.toyaps.controller;

import lombok.RequiredArgsConstructor;
import org.example.toyaps.domain.entity.Tool;
import org.example.toyaps.dto.request.ToolBulkUpsertRequest;
import org.example.toyaps.dto.response.ToolListResponse;
import org.example.toyaps.repository.ToolRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/tools")
public class ToolController {
    final ToolRepository toolRepository;

    @GetMapping
    public ResponseEntity<?> handleGetTools() {
        List<Tool> tools = toolRepository.findAll();
        long total = toolRepository.count();
        ToolListResponse resp = ToolListResponse.builder()
                .total(total)
                .tools(tools)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PostMapping
    public ResponseEntity<?> handlePostTool(@RequestBody ToolBulkUpsertRequest tbr) {
        // 아이디 겹치지 않는지 검증, 삭제 기능 추가해보기 + 몇 건 추가, 몇건 수정 인지 response 도 만들어보기?
        List<Tool> tools = tbr.getTools().stream()
                .map(m -> Tool.builder()
                        .id(m.getId())
                        .name(m.getName())
                        .description(m.getDescription())
                        .build())
                .toList();
        toolRepository.saveAll(tools);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }


}
