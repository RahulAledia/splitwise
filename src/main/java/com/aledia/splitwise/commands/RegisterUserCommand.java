package com.aledia.splitwise.commands;

import com.aledia.splitwise.controllers.UserController;
import com.aledia.splitwise.dtos.RegisterUserRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RegisterUserCommand implements Command{
// Register Rahul@1996 9354317570 rahulaledia1996@gmail.com

    private UserController userController;

    @Autowired
    public RegisterUserCommand(UserController userController){
        this.userController = userController;
    }

    @Override
    public boolean matches(String input) {
        List<String> inpWords = Arrays.asList(input.split(" "));

        if(inpWords.size() == 4 && inpWords.get(0).equals(CommandKeywords.RegisterUser)){
            return true;
        }
        return false;
    }

    @Override
    public void executes(String input) {
        List<String> inpWords = Arrays.asList(input.split(" "));

        String password = inpWords.get(1);
        String phoneNumber = inpWords.get(2);
        String username = inpWords.get(3);

        RegisterUserRequestDto request = new RegisterUserRequestDto();
        request.setPassword(password);
        request.setUserName(username);
        request.setPassword(password);

        userController.registerUser(request);

        // user controller and get our action done.

    }
}
