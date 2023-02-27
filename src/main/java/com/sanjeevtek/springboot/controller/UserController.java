package com.sanjeevtek.springboot.controller;

import com.sanjeevtek.springboot.model.Stores;
import com.sanjeevtek.springboot.model.Users;
import com.sanjeevtek.springboot.service.StoreService;
import com.sanjeevtek.springboot.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Users createUser(@RequestBody Users users){
        return userService.saveUser(users);
    }

    @GetMapping
    public List<Users> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("{id}")
    public ResponseEntity<Users> getUserById(@PathVariable("id") long userId){
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Users> updateUser(@PathVariable("id") long UserId,
                                                 @RequestBody Users users){
        return userService.getUserById(UserId)
                .map(savedUser -> {

                    savedUser.setFirstName(users.getFirstName());
                    savedUser.setLastName(users.getLastName());
                    savedUser.setEmail(users.getEmail());

                    Users updatedUsers = userService.updateUser(savedUser);
                    return new ResponseEntity<>(updatedUsers, HttpStatus.OK);

                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long userId){

        userService.deleteUser(userId);

        return new ResponseEntity<String>("Users deleted successfully!.", HttpStatus.OK);

    }

}
