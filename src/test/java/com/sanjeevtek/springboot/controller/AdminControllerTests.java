package com.sanjeevtek.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanjeevtek.springboot.model.Admin;
import com.sanjeevtek.springboot.service.AdminService;
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

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc
public class AdminControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenAdminObject_whenCreateAdmin_thenReturnSavedAdmin() throws Exception{

        // given - precondition or setup
        Admin admin = Admin.builder()
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail.com")
                .build();
        given(adminService.saveAdmin(any(Admin.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/admin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(admin)));

        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(admin.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(admin.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(admin.getEmail())));

    }

    // JUnit test for Get All Admins REST API
    @Test
    public void givenListOfAdmins_whenGetAllAdmins_thenReturnAdminsList() throws Exception{
        // given - precondition or setup
        List<Admin> listOfAdmin = new ArrayList<>();
        listOfAdmin.add(Admin.builder().firstName("Sanjeev").lastName("Kumar S").email("Sanjeev@gmail.com").build());
        listOfAdmin.add(Admin.builder().firstName("Tony").lastName("Stark").email("tony@gmail.com").build());
        given(adminService.getAllAdmins()).willReturn(listOfAdmin);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/admin"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfAdmin.size())));

    }

    // positive scenario - valid Admin id
    // JUnit test for GET Admin by id REST API
    @Test
    public void givenAdminId_whenGetAdminById_thenReturnAdminObject() throws Exception{
        // given - precondition or setup
        long adminId = 1L;
        Admin admin = Admin.builder()
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail.com")
                .build();
        given(adminService.getAdminById(adminId)).willReturn(Optional.of(admin));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/admin/{id}", adminId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(admin.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(admin.getLastName())))
                .andExpect(jsonPath("$.email", is(admin.getEmail())));

    }

    // negative scenario - valid Admin id
    // JUnit test for GET Admin by id REST API
    @Test
    public void givenInvalidAdminId_whenGetAdminById_thenReturnEmpty() throws Exception{
        // given - precondition or setup
        long adminId = 1L;
        Admin admin = Admin.builder()
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail.com")
                .build();
        given(adminService.getAdminById(adminId)).willReturn(Optional.empty());

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/admin/{id}", adminId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }
    // JUnit test for update Admin REST API - positive scenario
        @Test
        public void givenUpdatedAdmin_whenUpdateAdmin_thenReturnUpdateAdminObject() throws Exception{
            // given - precondition or setup
            long adminId = 1L;
            Admin savedAdmin = Admin.builder()
                    .firstName("Sanjeev")
                    .lastName("Kumar S")
                    .email("Sanjeev@gmail.com")
                    .build();

            Admin updatedAdmin = Admin.builder()
                    .firstName("Ayman")
                    .lastName("Tiraqi")
                    .email("ram@gmail.com")
                    .build();
            given(adminService.getAdminById(adminId)).willReturn(Optional.of(savedAdmin));
            given(adminService.updateAdmin(any(Admin.class)))
                    .willAnswer((invocation)-> invocation.getArgument(0));

            // when -  action or the behaviour that we are going test
            ResultActions response = mockMvc.perform(put("/api/admin/{id}", adminId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(updatedAdmin)));


            // then - verify the output
            response.andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.firstName", is(updatedAdmin.getFirstName())))
                    .andExpect(jsonPath("$.lastName", is(updatedAdmin.getLastName())))
                    .andExpect(jsonPath("$.email", is(updatedAdmin.getEmail())));
        }

    // JUnit test for update Admin REST API - negative scenario
    @Test
    public void givenUpdatedAdmin_whenUpdateAdmin_thenReturn404() throws Exception{
        // given - precondition or setup
        long adminId = 1L;
        Admin savedAdmin = Admin.builder()
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail.com")
                .build();

        Admin updatedAdmin = Admin.builder()
                .firstName("Ayman")
                .lastName("Tiraqi")
                .email("ram@gmail.com")
                .build();
        given(adminService.getAdminById(adminId)).willReturn(Optional.empty());
        given(adminService.updateAdmin(any(Admin.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/admin/{id}", adminId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAdmin)));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

// JUnit test for delete Admin REST API
    @Test
    public void givenAdminId_whenDeleteAdmin_thenReturn200() throws Exception{
        // given - precondition or setup
        long adminId = 1L;
        willDoNothing().given(adminService).deleteAdmin(adminId);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/admin/{id}", adminId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
