package com.example.monolith_dpt.repository;

import java.util.Optional;

import com.example.monolith_dpt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

}
