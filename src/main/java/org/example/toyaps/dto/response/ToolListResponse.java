package org.example.toyaps.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.toyaps.domain.entity.Tool;

import java.util.List;

@Getter
@Setter
@Builder
public class ToolListResponse {
    private long total;
    private List<Tool> tools;
}
