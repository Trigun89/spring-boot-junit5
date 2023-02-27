package com.sanjeevtek.springboot.service.impl;

import com.sanjeevtek.springboot.exception.ResourceNotFoundException;
import com.sanjeevtek.springboot.model.Users;
import com.sanjeevtek.springboot.repository.UserRepository;
import com.sanjeevtek.springboot.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Users saveUser(Users users) {

        Optional<Users> savedUser = userRepository.findByEmail(users.getEmail());
        if(savedUser.isPresent()){
            throw new ResourceNotFoundException("Stores already exist with given email:" + users.getEmail());
        }
        return userRepository.save(users);
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<Users> getUserById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public Users updateUser(Users updatedUsers) {
        return userRepository.save(updatedUsers);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}
