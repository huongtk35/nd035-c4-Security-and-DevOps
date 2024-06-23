package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderControllerTest {
    OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void Setup() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submit_UserNotFound() {
        when(userRepository.findByUsername(any())).thenReturn(null);
        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void submit_HappyPath() {
        User user = createUser();
        when(userRepository.findByUsername(any())).thenReturn(user);
        ResponseEntity<UserOrder> response = orderController.submit("test");
        UserOrder order = response.getBody();
        verify(userRepository, times(1)).findByUsername("test");
        verify(orderRepository, times(1)).save(order);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createItem(1L), order.getItems().get(0));
        assertEquals(createUser().getId(), order.getUser().getId());


    }

    @Test
    public void getOrdersForUser_UserNotFound() {
        when(userRepository.findByUsername(any())).thenReturn(null);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getOrdersForUser_HappyPath() {
        User user = createUser();
        UserOrder order = UserOrder.createFromCart(user.getCart());
        List<UserOrder> orders = new ArrayList<>();
        orders.add(order);
        when(userRepository.findByUsername(any())).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(orders);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        List<UserOrder> actualOrders = response.getBody();
        verify(userRepository, times(1)).findByUsername("test");
        verify(orderRepository, times(1)).findByUser(user);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createItem(1L), order.getItems().get(0));
        assertEquals(createUser().getId(), order.getUser().getId());


    }

    private  static User createUser() {
        User user = new User();
        user.setUsername("test_username");
        user.setId(1L);
        user.setCart(createCart(user));
        return user;
    }
    private  static Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setId(1L);
        Item item = createItem(1L);
        List<Item> items = new ArrayList<>();
        items.add(item);
        cart.setItems(items);
        cart.setTotal(items.stream().map(itemtemp -> itemtemp.getPrice()).reduce(BigDecimal::add).get());
        cart.setUser(user);
        return cart;
    }
    public static Item createItem(long id){
        Item item = new Item();
        item.setId(id);
        item.setPrice(BigDecimal.valueOf(id * 1.2));
        item.setName("Item " + item.getId());
        item.setDescription("Description ");
        return item;
    }

}
