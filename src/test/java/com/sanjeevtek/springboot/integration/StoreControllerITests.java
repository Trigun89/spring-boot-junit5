package com.sanjeevtek.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanjeevtek.springboot.model.Stores;
import com.sanjeevtek.springboot.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StoreControllerITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        storeRepository.deleteAll();
    }

    @Test
    public void givenStoreObject_whenCreateStore_thenReturnSavedStore() throws Exception{

        // given - precondition or setup
        Stores stores = Stores.builder()
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail.com")
                .build();

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
        storeRepository.saveAll(listOfStores);
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
        Stores stores = Stores.builder()
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail.com")
                .build();
        storeRepository.save(stores);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/store/{id}", stores.getId()));

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
        storeRepository.save(stores);

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
        Stores savedStores = Stores.builder()
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail.com")
                .build();
        storeRepository.save(savedStores);

        Stores updatedStores = Stores.builder()
                .firstName("Ayman")
                .lastName("Tiraqi")
                .email("ram@gmail.com")
                .build();

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/store/{id}", savedStores.getId())
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
        storeRepository.save(savedStores);

        Stores updatedStores = Stores.builder()
                .firstName("Ayman")
                .lastName("Tiraqi")
                .email("ram@gmail.com")
                .build();

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
        Stores savedStores = Stores.builder()
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail.com")
                .build();
        storeRepository.save(savedStores);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/store/{id}", savedStores.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
