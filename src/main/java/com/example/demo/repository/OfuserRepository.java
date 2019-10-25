package com.example.demo.repository;


import com.example.demo.entity.Ofuser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfuserRepository extends JpaRepository<Ofuser, String> {
    @Query(value = "select * from ofuser",nativeQuery = true)
    List<Ofuser> findAll();
}
