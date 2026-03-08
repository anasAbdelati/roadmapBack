package org.roadmapBack.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.roadmapBack.data.RoadMap;
import org.roadmapBack.data.User;
import org.roadmapBack.exceptions.RoadmapNotFoundException;
import org.roadmapBack.repository.RoadMapRepository;
import org.springframework.ai.chat.client.ChatClient;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoadMapServiceTest {

    private static final String EXISTING_ID = "existingId";
    private static final String INEXISTING_ID = "inexistingId";

    @Mock
    private RoadMapRepository roadMapRepository;
    @Mock
    private UserService userService;
    @Mock
    private ChatClient.Builder builder;

    @InjectMocks
    private RoadMapService roadMapService;

    private User currentUser;
    private RoadMap roadMap;

    @BeforeEach
    void setUp() {
        roadMap = RoadMap.builder()
                .id(EXISTING_ID)
                .topic("Java")
                .build();

        currentUser = User.builder()
                .roadMapSummaries(new ArrayList<>())
                .build();
        currentUser.addRoadMapId(EXISTING_ID, "Java");
    }

    @Test
    public void getRoadMapTest() {
        when(roadMapRepository.findRoadMapById(EXISTING_ID)).thenReturn(roadMap);

        assertThatThrownBy(() -> roadMapService.getRoadMap(currentUser, INEXISTING_ID))
                .isInstanceOf(RoadmapNotFoundException.class);

        final var result = roadMapService.getRoadMap(currentUser, EXISTING_ID);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(EXISTING_ID);
    }

    @Test
    public void deleteRoadMapTest() {
        assertThatThrownBy(() -> roadMapService.deleteRoadMap(currentUser, INEXISTING_ID))
                .isInstanceOf(RoadmapNotFoundException.class);

        roadMapService.deleteRoadMap(currentUser, EXISTING_ID);
        verify(roadMapRepository).deleteRoadMapById(EXISTING_ID);
        verify(userService).saveUser(currentUser);
        assertThat(currentUser.getRoadMapSummaries()).isEmpty();
    }
}