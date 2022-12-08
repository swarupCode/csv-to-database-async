package com.swarudas.csvtodatabase.repository;

import com.swarudas.csvtodatabase.entity.UserDemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDemoRepository extends JpaRepository<UserDemo, Integer> {
}
