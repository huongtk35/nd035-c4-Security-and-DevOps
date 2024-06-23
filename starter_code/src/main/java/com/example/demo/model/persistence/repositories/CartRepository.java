/**
 * @author RoseDao
 * @email [huongtk35@gmail.com]
 * @create date 2024-06-23 20:55:57
 * @modify date 2024-06-23 20:55:57
 * @desc [description]
 */
package com.example.demo.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByUser(User user);
}
