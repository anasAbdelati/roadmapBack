package org.roadmapBack.data;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private List<RoadMapSummary> roadMapSummaries;

    public void addRoadMapId(String roadMapId,String topic){
        roadMapSummaries.add(RoadMapSummary.builder()
                .id(roadMapId)
                .topic(topic)
                .build());
    }

    public boolean hasRoadmap(String id){
        return getRoadMapSummaries().stream().anyMatch(roadmapSummary -> roadmapSummary.getId().equals(id));
    }

    public void deleteRoadMap(String id) {
        roadMapSummaries.removeIf(summary -> summary.getId().equals(id));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // no roles for now
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
