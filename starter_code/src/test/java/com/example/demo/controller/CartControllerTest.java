package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CartControllerTest {
    CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        cartController = new CartController();

        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addTocart_UserNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(3);
        request.setItemId(1);
        request.setUsername("huongtk35");
        when(userRepository.findByUsername("")).thenReturn(null);
        ResponseEntity<Cart> response = cartController.addTocart(request);
        HttpStatus actualStatus = response.getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, actualStatus);

    }

    @Test
    public void addTocart_ItemNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(3);
        request.setItemId(1);
        request.setUsername("huongtk35");
        User user = new User();
        user.setUsername("huongtk35");
        user.setId(1L);
        when(userRepository.findByUsername("huongtk35")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Cart> response = cartController.addTocart(request);
        HttpStatus actualStatus = response.getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, actualStatus);

    }

    @Test
    public void addTocart_HappyPath() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(3);
        request.setItemId(1);
        request.setUsername("test_username");
        User user = createUser();
        Item item = createItem(1L);
        Cart expectCart = createCart(user);
        when(userRepository.findByUsername(any())).thenReturn(user);
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));
        ResponseEntity<Cart> response = cartController.addTocart(request);

        Cart actualCart = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("test_username", actualCart.getUser().getUsername());
        assertEquals(item, actualCart.getItems().get(0));
    }

    @Test
    public void removeFromcart_HappyPath() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(3);
        request.setItemId(1);
        request.setUsername("test_username");
        User user = createUser();
        Item item = createItem(1L);
        when(userRepository.findByUsername(any())).thenReturn(user);
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));
        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        Cart actualCart = response.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("test_username", actualCart.getUser().getUsername());
    }

    @Test
    public void removeFromcart_UserNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(3);
        request.setItemId(1);
        request.setUsername("huongtk35");
        when(userRepository.findByUsername("")).thenReturn(null);
        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        HttpStatus actualStatus = response.getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, actualStatus);

    }

    @Test
    public void removeFromcart_ItemNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(3);
        request.setItemId(1);
        request.setUsername("huongtk35");
        User user = new User();
        user.setUsername("huongtk35");
        user.setId(1L);
        when(userRepository.findByUsername("huongtk35")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        HttpStatus actualStatus = response.getStatusCode();
        assertEquals(HttpStatus.NOT_FOUND, actualStatus);

    }



    public static Item createItem(long id){
        Item item = new Item();
        item.setId(id);
        item.setPrice(BigDecimal.valueOf(id * 1.2));
        item.setName("Item " + item.getId());
        item.setDescription("Description ");
        return item;
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
}
