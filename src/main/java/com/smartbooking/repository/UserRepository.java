package com.smartbooking.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.smartbooking.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {
}
