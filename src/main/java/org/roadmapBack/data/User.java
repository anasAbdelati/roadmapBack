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
    private List<String> roadmapIds;

    //TODO check if list good practice that we can get it directly or nah
    public void addRoadmapId(String roadmapId){
        roadmapIds.add(roadmapId);
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
