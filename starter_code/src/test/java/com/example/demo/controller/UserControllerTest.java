package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserControllerTest {
    private UserController userController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);


    @Before
    public void setUp(){
        userController = new UserController();

        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUser_HappyPath(){
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("userTest");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");

        ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);

        assertEquals(0, user.getId());
        assertEquals("userTest", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());

    }
    @Test
    public void createUser_UserExists(){
        long id = 1L;
        User user = new User();
        user.setUsername("userTest");
        user.setPassword("testPassword");
        user.setId(id);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("userTest");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");

        ResponseEntity<User> response = userController.createUser(request);
        HttpStatus statusCode = response.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, statusCode);


    }

    @Test
    public void createUser_InvalidPassword(){
        long id = 1L;
        User user = new User();
        user.setUsername("userTest");
        user.setPassword("testPassword");
        user.setId(id);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("userTest");
        request.setPassword("test");
        request.setConfirmPassword("test");

        ResponseEntity<User> response = userController.createUser(request);
        HttpStatus statusCode = response.getStatusCode();

        assertEquals(HttpStatus.BAD_REQUEST, statusCode);


    }


    @Test
    public void getUserById(){
        long id = 1L;
        User user = new User();
        user.setUsername("userTest");
        user.setPassword("testPassword");
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        ResponseEntity<User> response = userController.findById(1L);
        User actualUser = response.getBody();
        assertNotNull(actualUser);

        assertEquals(1L,actualUser.getId());
        assertEquals("userTest", actualUser.getUsername());
    }

    @Test
    public void getUserById_UserNotFound(){

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<User> response = userController.findById(1L);
        HttpStatus statusCode = response.getStatusCode();

        assertEquals(HttpStatus.NOT_FOUND, statusCode);
    }

    @Test
    public void findByUserName(){
        long id = 1L;
        User user = new User();
        user.setUsername("userTest");
        user.setPassword("testPassword");
        user.setId(id);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName(user.getUsername());
        User actualUser = response.getBody();
        assertNotNull(actualUser);
        assertEquals(1L,actualUser.getId());
        assertEquals("userTest", actualUser.getUsername());
    }

    @Test
    public void findByUserName_UserNotFound(){

        when(userRepository.findByUsername("test")).thenReturn(null);
        ResponseEntity<User> response = userController.findByUserName("test");
        HttpStatus statusCode = response.getStatusCode();

        assertEquals(HttpStatus.NOT_FOUND, statusCode);
    }



}
