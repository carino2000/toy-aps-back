package org.example.toyaps.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TaskUpsertResponse {
    int created;
    int updated;
    int deleted;
}
