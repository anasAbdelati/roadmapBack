package org.roadmapBack.data;

import lombok.Data;
import java.util.List;

@Data
class RoadMapNode {
    private String id;
    private String name;
    private String description;
    private List<String> childrenIds;
}
