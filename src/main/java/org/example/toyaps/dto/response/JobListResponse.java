package org.example.toyaps.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.toyaps.domain.entity.Job;

import java.util.List;

@Getter
@Setter
@Builder
public class JobListResponse {
    private List<Job> jobs;
}
