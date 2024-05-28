package com.example.rear.mapper;

import com.example.rear.mapper.po.EnginData;
import jakarta.persistence.Table;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.PageRequest;
import java.util.Collection;


import java.util.Collection;
import java.util.List;

public interface EnginDataRepository extends JpaRepository<EnginData, Long> {
    List<EnginData> findAllByOrderByIdDesc(Pageable pageable);
}
