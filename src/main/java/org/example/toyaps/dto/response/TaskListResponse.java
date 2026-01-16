package org.example.toyaps.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.toyaps.domain.entity.Task;

import java.util.List;

@Getter
@Setter
@Builder
public class TaskListResponse {
    List<Task> tasks;
}
