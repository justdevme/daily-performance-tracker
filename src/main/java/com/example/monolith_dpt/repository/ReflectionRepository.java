package com.example.monolith_dpt.repository;

import com.example.monolith_dpt.entity.Reflection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReflectionRepository extends JpaRepository <Reflection, Long> {
    List<Reflection> findByWeekId (Long weekId);
}
