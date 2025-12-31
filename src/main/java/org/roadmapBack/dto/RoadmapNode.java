package org.roadmapBack.dto;

import lombok.Data;
import java.util.List;

@Data
class RoadmapNode {
    private String id;
    private String name;
    private String description;
    private List<String> childrenIds;
}
