/**
 * @author RoseDao
 * @email [huongtk35@gmail.com]
 * @create date 2024-06-23 20:56:03
 * @modify date 2024-06-23 20:56:03
 * @desc [description]
 */
package com.example.demo.model.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;

@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Long> {
	List<UserOrder> findByUser(User user);
}
