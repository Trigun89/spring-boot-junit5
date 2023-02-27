package com.sanjeevtek.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanjeevtek.springboot.model.Stores;
import com.sanjeevtek.springboot.service.StoreService;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(StoreController.class)
@AutoConfigureMockMvc
public class StoreControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoreService storeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenStoreObject_whenCreateStore_thenReturnSavedStore() throws Exception{

        // given - precondition or setup
        Stores stores = Stores.builder()
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail.com")
                .build();
        given(storeService.saveStore(any(Stores.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/store")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(stores)));

        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(stores.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(stores.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(stores.getEmail())));

    }

    // JUnit test for Get All Stores REST API
    @Test
    public void givenListOfStores_whenGetAllStores_thenReturnStoresList() throws Exception{
        // given - precondition or setup
        List<Stores> listOfStores = new ArrayList<>();
        listOfStores.add(Stores.builder().firstName("Sanjeev").lastName("Kumar S").email("Sanjeev@gmail.com").build());
        listOfStores.add(Stores.builder().firstName("Tony").lastName("Stark").email("tony@gmail.com").build());
        given(storeService.getAllStores()).willReturn(listOfStores);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/store"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfStores.size())));

    }

    // positive scenario - valid Store id
    // JUnit test for GET Store by id REST API
    @Test
    public void givenStoreId_whenGetStoreById_thenReturnStoreObject() throws Exception{
        // given - precondition or setup
        long StoreId = 1L;
        Stores stores = Stores.builder()
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail.com")
                .build();
        given(storeService.getStoreById(StoreId)).willReturn(Optional.of(stores));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/store/{id}", StoreId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(stores.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(stores.getLastName())))
                .andExpect(jsonPath("$.email", is(stores.getEmail())));

    }

    // negative scenario - valid Store id
    // JUnit test for GET Store by id REST API
    @Test
    public void givenInvalidStoreId_whenGetStoreById_thenReturnEmpty() throws Exception{
        // given - precondition or setup
        long StoreId = 1L;
        Stores stores = Stores.builder()
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail.com")
                .build();
        given(storeService.getStoreById(StoreId)).willReturn(Optional.empty());

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/store/{id}", StoreId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }
    // JUnit test for update Store REST API - positive scenario
        @Test
        public void givenUpdatedStore_whenUpdateStore_thenReturnUpdateStoreObject() throws Exception{
            // given - precondition or setup
            long StoreId = 1L;
            Stores savedStores = Stores.builder()
                    .firstName("Sanjeev")
                    .lastName("Kumar S")
                    .email("Sanjeev@gmail.com")
                    .build();

            Stores updatedStores = Stores.builder()
                    .firstName("Tyler")
                    .lastName("Allison")
                    .email("tylall@gmail.com")
                    .build();
            given(storeService.getStoreById(StoreId)).willReturn(Optional.of(savedStores));
            given(storeService.updateStore(any(Stores.class)))
                    .willAnswer((invocation)-> invocation.getArgument(0));

            // when -  action or the behaviour that we are going test
            ResultActions response = mockMvc.perform(put("/api/store/{id}", StoreId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(updatedStores)));


            // then - verify the output
            response.andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.firstName", is(updatedStores.getFirstName())))
                    .andExpect(jsonPath("$.lastName", is(updatedStores.getLastName())))
                    .andExpect(jsonPath("$.email", is(updatedStores.getEmail())));
        }

    // JUnit test for update Store REST API - negative scenario
    @Test
    public void givenUpdatedStore_whenUpdateStore_thenReturn404() throws Exception{
        // given - precondition or setup
        long StoreId = 1L;
        Stores savedStores = Stores.builder()
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail.com")
                .build();

        Stores updatedStores = Stores.builder()
                .firstName("Tyler")
                .lastName("Allison")
                .email("tylall@gmail.com")
                .build();
        given(storeService.getStoreById(StoreId)).willReturn(Optional.empty());
        given(storeService.updateStore(any(Stores.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/store/{id}", StoreId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStores)));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

// JUnit test for delete Store REST API
    @Test
    public void givenStoreId_whenDeleteStore_thenReturn200() throws Exception{
        // given - precondition or setup
        long StoreId = 1L;
        willDoNothing().given(storeService).deleteStore(StoreId);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/store/{id}", StoreId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
