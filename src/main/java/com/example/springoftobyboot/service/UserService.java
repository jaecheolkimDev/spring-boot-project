package com.example.springoftobyboot.service;

import com.example.springoftobyboot.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.springoftobyboot.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity saveUser(String name, String password) {
        UserEntity user = UserEntity.createUser(name, password);
        return userRepository.save(user);
    }
}
