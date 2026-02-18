package org.roadmapBack.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "roadmaps")
public class RoadMap {
    @Id
    private String id;
    private String topic;
    private List<RoadmapNode> nodes;
}
