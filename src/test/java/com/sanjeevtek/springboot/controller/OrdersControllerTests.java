package com.sanjeevtek.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanjeevtek.springboot.model.Orders;
import com.sanjeevtek.springboot.service.OrderService;
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

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc
public class OrdersControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenOrderObject_whenCreateOrder_thenReturnSavedOrder() throws Exception{

        // given - precondition or setup
        Orders orders = Orders.builder()
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail.com")
                .build();
        given(orderService.saveOrder(any(Orders.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/order")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(orders)));

        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(orders.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(orders.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(orders.getEmail())));

    }

    // JUnit test for Get All Orders REST API
    @Test
    public void givenListOfOrders_whenGetAllOrders_thenReturnOrdersList() throws Exception{
        // given - precondition or setup
        List<Orders> listOfOrders = new ArrayList<>();
        listOfOrders.add(Orders.builder().firstName("Sanjeev").lastName("Kumar S").email("Sanjeev@gmail.com").build());
        listOfOrders.add(Orders.builder().firstName("Tony").lastName("Stark").email("tony@gmail.com").build());
        given(orderService.getAllOrders()).willReturn(listOfOrders);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/order"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfOrders.size())));

    }

    // positive scenario - valid Order id
    // JUnit test for GET Order by id REST API
    @Test
    public void givenOrderId_whenGetOrderById_thenReturnOrderObject() throws Exception{
        // given - precondition or setup
        long OrderId = 1L;
        Orders orders = Orders.builder()
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail.com")
                .build();
        given(orderService.getOrderById(OrderId)).willReturn(Optional.of(orders));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/order/{id}", OrderId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(orders.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(orders.getLastName())))
                .andExpect(jsonPath("$.email", is(orders.getEmail())));

    }

    // negative scenario - valid Order id
    // JUnit test for GET Order by id REST API
    @Test
    public void givenInvalidOrderId_whenGetOrderById_thenReturnEmpty() throws Exception{
        // given - precondition or setup
        long OrderId = 1L;
        Orders orders = Orders.builder()
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail.com")
                .build();
        given(orderService.getOrderById(OrderId)).willReturn(Optional.empty());

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/order/{id}", OrderId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }
    // JUnit test for update Order REST API - positive scenario
        @Test
        public void givenUpdatedOrder_whenUpdateOrder_thenReturnUpdateOrderObject() throws Exception{
            // given - precondition or setup
            long orderId = 1L;
            Orders savedOrders = Orders.builder()
                    .firstName("Sanjeev")
                    .lastName("Kumar S")
                    .email("Sanjeev@gmail.com")
                    .build();

            Orders updatedOrders = Orders.builder()
                    .firstName("Ayman")
                    .lastName("Tiraqi")
                    .email("ram@gmail.com")
                    .build();

            given(orderService.getOrderById(orderId)).willReturn(Optional.of(savedOrders));
            given(orderService.updateOrder(any(Orders.class)))
                    .willAnswer((invocation)-> invocation.getArgument(0));

            // when -  action or the behaviour that we are going test
            ResultActions response = mockMvc.perform(put("/api/order/{id}", orderId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(updatedOrders)));


            // then - verify the output
            response.andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("$.firstName", is(updatedOrders.getFirstName())))
                    .andExpect(jsonPath("$.lastName", is(updatedOrders.getLastName())))
                    .andExpect(jsonPath("$.email", is(updatedOrders.getEmail())));
        }

    // JUnit test for update Order REST API - negative scenario
    @Test
    public void givenUpdatedOrder_whenUpdateOrder_thenReturn404() throws Exception{
        // given - precondition or setup
        long orderId = 1L;
        Orders savedOrders = Orders.builder()
                .firstName("Sanjeev")
                .lastName("Kumar S")
                .email("Sanjeev@gmail.com")
                .build();

        Orders updatedOrders = Orders.builder()
                .firstName("Ayman")
                .lastName("Tiraqi")
                .email("ram@gmail.com")
                .build();
        given(orderService.getOrderById(orderId)).willReturn(Optional.empty());
        given(orderService.updateOrder(any(Orders.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/order/{id}", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedOrders)));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

// JUnit test for delete Order REST API
    @Test
    public void givenOrderId_whenDeleteOrder_thenReturn200() throws Exception{
        // given - precondition or setup
        long orderId = 1L;
        willDoNothing().given(orderService).deleteOrder(orderId);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/order/{id}", orderId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
