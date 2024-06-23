/**
 * @author RoseDao
 * @email [huongtk35@gmail.com]
 * @create date 2024-06-23 20:56:00
 * @modify date 2024-06-23 20:56:00
 * @desc [description]
 */
package com.example.demo.model.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.persistence.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
	List<Item> findByName(String name);

}
