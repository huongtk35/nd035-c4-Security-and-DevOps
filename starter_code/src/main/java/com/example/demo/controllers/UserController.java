/**
 * @author RoseDao
 * @email [huongtk35@gmail.com]
 * @create date 2024-06-23 20:55:47
 * @modify date 2024-06-23 20:55:47
 * @desc [description]
 */
package com.example.demo.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        log.error("[UserController] [findById] api request" );
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        log.error("[UserController] [findByUserName] api call start" );
        User user = userRepository.findByUsername(username);
        if(user == null) {
            log.error("[UserController] [findByUserName] [Fail] for user : " + username +", REASON : User not found" );
            return ResponseEntity.notFound().build();
        }
        log.error("[UserController] [findByUserName] api call end" );
        return ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        log.info("[UserController] [createUser] start");
        User user = userRepository.findByUsername(createUserRequest.getUsername());
        if(user != null) {
            log.info(String.format("[UserController] [createUser] [Fail] for user: %s, REASON: user exists",createUserRequest.getUsername()));
            return ResponseEntity.badRequest().build();
        }

        user = new User();
        user.setUsername(createUserRequest.getUsername());
        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);
        if (createUserRequest.getPassword().length() < 7 ||
                !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
            log.info(String.format("[UserController] [createUser] [Fail] for user: %s, REASON: password is not valid",createUserRequest.getUsername()));
            return ResponseEntity.badRequest().build();
        }

        user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
        userRepository.save(user);
        log.info("[UserController] [createUser] api call end");
        return ResponseEntity.ok(user);
    }

}
