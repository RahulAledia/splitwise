package com.aledia.splitwise.Services;

import com.aledia.splitwise.Exceptions.UserAlreadyExistsException;
import com.aledia.splitwise.Repository.UserRepository;
import com.aledia.splitwise.models.User;
import com.aledia.splitwise.models.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User registerUser(String userName, String phoneNumber, String password){

        Optional<User> userOptional = userRepository.findByPhone(phoneNumber);

        if(userOptional.isPresent()){
            if(userOptional.get().getUserStatus().equals(UserStatus.ACTIVE)){
                throw new UserAlreadyExistsException("A user with same phone number already exists!");
            } else {
                User user = userOptional.get();
                user.setUserStatus(UserStatus.ACTIVE);
                user.setName(userName);
                user.setPassword(password);

                return userRepository.save(user);
            }
        }
        User user = new User();
        user.setPhone(phoneNumber);
        user.setName(userName);
        user.setPassword(password);
        user.setUserStatus(UserStatus.ACTIVE);

        return userRepository.save(user);
    }
}
