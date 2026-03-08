package org.roadmapBack.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoadMapSummary {
    private String id;
    private String topic;
}
