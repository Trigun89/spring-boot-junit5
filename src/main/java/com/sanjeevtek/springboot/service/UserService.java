package com.sanjeevtek.springboot.service;

import com.sanjeevtek.springboot.model.Stores;
import com.sanjeevtek.springboot.model.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Users saveUser(Users stores);
    List<Users> getAllUsers();
    Optional<Users> getUserById(long id);
    Users updateUser(Users updatedStores);
    void deleteUser(long id);
}
