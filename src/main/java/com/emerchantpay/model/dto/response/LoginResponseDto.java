package com.emerchantpay.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class LoginResponseDto implements Serializable {

    private static final long serialVersionUID = 345L;

    private final String jwt;
    private Long id;
    private String username;
    private List<String> roles;


}
