package org.example.toyaps.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.example.toyaps.domain.entity.Job;

import java.util.List;

@Getter
@Setter
public class JobParseResponse {
    List<Item> items;

    @Getter
    @Setter
    public static class Item{
        private String id;
        private String name;
        private String description;
        private boolean active;

        public Item(String id, String name, String description, boolean active) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.active = active;
        }
    }
}
