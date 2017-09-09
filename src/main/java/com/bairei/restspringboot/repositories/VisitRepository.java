package com.bairei.restspringboot.repositories;

import com.bairei.restspringboot.domain.User;
import com.bairei.restspringboot.domain.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit,Integer> {
    List<Visit> findAllByPatient(User patient);
}
