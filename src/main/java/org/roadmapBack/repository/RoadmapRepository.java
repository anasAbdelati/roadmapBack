package org.roadmapBack.repository;

import org.roadmapBack.data.RoadMap;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadmapRepository extends MongoRepository<RoadMap,String> {
}
