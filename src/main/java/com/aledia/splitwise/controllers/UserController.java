package com.aledia.splitwise.controllers;

import com.aledia.splitwise.Exceptions.UserAlreadyExistsException;
import com.aledia.splitwise.Services.UserService;
import com.aledia.splitwise.dtos.RegisterUserRequestDto;
import com.aledia.splitwise.dtos.RegisterUserResponseDto;
import com.aledia.splitwise.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    public RegisterUserResponseDto registerUser(RegisterUserRequestDto request){

        RegisterUserResponseDto response = new RegisterUserResponseDto();
        User user;

        try{
            user = userService.registerUser(request.getUserName(), request.getPhoneNumber(), request.getPassword());
            response.setUserId(user.getId());
            response.setStatus("SUCCESS");
        }
        catch(UserAlreadyExistsException e){
            response.setStatus("FAILURE");
            response.setMessage(e.getMessage());
        }
        return null;
    }
}
