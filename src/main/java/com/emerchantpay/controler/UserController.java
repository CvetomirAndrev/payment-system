package com.emerchantpay.controler;

import com.emerchantpay.model.dto.request.LoginRequestDto;
import com.emerchantpay.model.dto.response.LoginResponseDto;
import com.emerchantpay.model.entities.Merchant;
import com.emerchantpay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginDto) {

        return userService.login(loginDto);
    }

    @GetMapping
    private Merchant getMerchant(@RequestParam Long id) {
        return userService.getMerchantById(id);
    }

}
