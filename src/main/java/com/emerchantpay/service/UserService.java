package com.emerchantpay.service;

import com.emerchantpay.model.dto.request.LoginRequestDto;
import com.emerchantpay.model.dto.response.LoginResponseDto;
import com.emerchantpay.model.entities.MyUserDetails;
import com.emerchantpay.model.entities.User;
import com.emerchantpay.repository.UserRepository;
import com.emerchantpay.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponseDto login(LoginRequestDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getName(), loginDto.getPassword())
        );
        MyUserDetails userDetails = (MyUserDetails) loadUserByUsername(loginDto.getName());

        return LoginResponseDto.builder()
            .id(userDetails.getId())
            .username(userDetails.getName())
            .jwt(jwtUtil.generateToken(userDetails))
            .roles(userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
            )
            .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByName(username);
        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
        return user.map(MyUserDetails::new).get();
    }

}