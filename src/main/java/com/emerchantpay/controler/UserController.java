package com.emerchantpay.controler;

import com.emerchantpay.model.dto.request.LoginRequestDto;
import com.emerchantpay.model.dto.response.LoginResponseDto;
import com.emerchantpay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //TODO: move it from controller
    @PostMapping(value = "/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginDto) {

        return userService.login(loginDto);
    }
}
