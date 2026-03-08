package org.roadmapBack.data;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    public void addRoadMapIdTest(){
        final var user = User.builder().roadMapSummaries(new ArrayList<>()).build();
        user.addRoadMapId("123", "Java");
        assertThat(user.getRoadMapSummaries()).hasSize(1);
        assertThat(user.getRoadMapSummaries().getFirst().getId()).isEqualTo("123");
    }

    @Test
    public void hasRoadmapTest(){
        final var user = User.builder().roadMapSummaries(new ArrayList<>()).build();
        user.addRoadMapId("123", "Java");
        assertThat(user.hasRoadmap("123")).isTrue();
        assertThat(user.hasRoadmap("999")).isFalse();
    }

    @Test
    public void deleteRoadMapTest(){
        final var user = User.builder().roadMapSummaries(new ArrayList<>()).build();
        user.addRoadMapId("123", "Java");
        user.deleteRoadMap("123");
        assertThat(user.getRoadMapSummaries()).isEmpty();
    }
}
