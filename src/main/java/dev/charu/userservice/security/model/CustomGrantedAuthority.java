package dev.charu.userservice.security.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.charu.userservice.model.Role;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;



@JsonDeserialize
@NoArgsConstructor
public class CustomGrantedAuthority implements GrantedAuthority {
    //    private Role role;
    private String authority;

    public CustomGrantedAuthority(Role role) {
        this.authority = role.getName();
    }


    @Override
    public String getAuthority() {
        return this.authority;
    }
}

