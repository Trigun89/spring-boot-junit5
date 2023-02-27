package com.sanjeevtek.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanjeevtek.springboot.model.Users;
import com.sanjeevtek.springboot.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UsersControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenUserObject_whenCreateUser_thenReturnSavedUser() throws Exception{

        // given - precondition or setup
        Users users = Users.builder()
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail.com")
                .build();
        given(userService.saveUser(any(Users.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(users)));

        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(users.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(users.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(users.getEmail())));

    }

    // JUnit test for Get All Users REST API
    @Test
    public void givenListOfUsers_whenGetAllUsers_thenReturnUsersList() throws Exception{
        // given - precondition or setup
        List<Users> listOfUsers = new ArrayList<>();
        listOfUsers.add(Users.builder().firstName("Sanjeev").lastName("Kumar S").email("Sanjeev@gmail.com").build());
        listOfUsers.add(Users.builder().firstName("Tony").lastName("Stark").email("tony@gmail.com").build());
        given(userService.getAllUsers()).willReturn(listOfUsers);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/user"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfUsers.size())));

    }

    // positive scenario - valid User id
    // JUnit test for GET User by id REST API
    @Test
    public void givenUserId_whenGetUserById_thenReturnUserObject() throws Exception{
        // given - precondition or setup
        long userId = 1L;
        Users users = Users.builder()
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail.com")
                .build();
        given(userService.getUserById(userId)).willReturn(Optional.of(users));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/user/{id}", userId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(users.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(users.getLastName())))
                .andExpect(jsonPath("$.email", is(users.getEmail())));

    }

    // negative scenario - valid User id
    // JUnit test for GET User by id REST API
    @Test
    public void givenInvalidUserId_whenGetUserById_thenReturnEmpty() throws Exception{
        // given - precondition or setup
        long userId = 1L;
        Users users = Users.builder()
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail.com")
                .build();
        given(userService.getUserById(userId)).willReturn(Optional.empty());

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/user/{id}", userId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }
    // JUnit test for update User REST API - positive scenario
        @Test
        public void givenUpdatedUser_whenUpdateUser_thenReturnUpdateUserObject() throws Exception{
            // given - precondition or setup
            long userId = 1L;
            Users savedUsers = Users.builder()
                    .firstName("Sanjeev")
                    .lastName("Kumar S")
                    .email("Sanjeev@gmail.com")
                    .build();

            Users updatedUsers = Users.builder()
                    .firstName("Ayman")
                    .lastName("Tiraqi")
                    .email("ram@gmail.com")
                    .build();
            given(userService.getUserById(userId)).willReturn(Optional.of(savedUsers));
            given(userService.updateUser(any(Users.class)))
                    .willAnswer((invocation)-> invocation.getArgument(0));

            // when -  action or the behaviour that we are going test
            ResultActions response = mockMvc.perform(put("/api/user/{id}", userId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(updatedUsers)));


            // then - verify the output
            response.andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.firstName", is(updatedUsers.getFirstName())))
                    .andExpect(jsonPath("$.lastName", is(updatedUsers.getLastName())))
                    .andExpect(jsonPath("$.email", is(updatedUsers.getEmail())));
        }

    // JUnit test for update User REST API - negative scenario
    @Test
    public void givenUpdatedUser_whenUpdateUser_thenReturn404() throws Exception{
        // given - precondition or setup
        long userId = 1L;
        Users savedUsers = Users.builder()
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail.com")
                .build();

        Users updatedUsers = Users.builder()
                .firstName("Ayman")
                .lastName("Tiraqi")
                .email("ram@gmail.com")
                .build();
        given(userService.getUserById(userId)).willReturn(Optional.empty());
        given(userService.updateUser(any(Users.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUsers)));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

// JUnit test for delete User REST API
    @Test
    public void givenUserId_whenDeleteUser_thenReturn200() throws Exception{
        // given - precondition or setup
        long userId = 1L;
        willDoNothing().given(userService).deleteUser(userId);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/user/{id}", userId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
