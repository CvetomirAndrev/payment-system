package com.emerchantpay.model.entities;

import com.google.common.collect.ImmutableList;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MyUserDetails implements UserDetails {

    private static final long serialVersionUID = 275347623L;
    private Long id;
    private String name;
    private String password;
    private List<GrantedAuthority> authorities;
    //new fields for the info
    private String company;
    private String department;

    public MyUserDetails(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.password = user.getPassword();

        this.authorities = ImmutableList.of(user.getDescription())
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
