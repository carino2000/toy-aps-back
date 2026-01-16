package org.example.toyaps.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobBulkUpsertRequest {

    private List<JobUpsertItem> jobs;


    @Getter
    @Setter
    public static class JobUpsertItem{
        String id;
        String name;
        String description;
        boolean active;
    }
}
