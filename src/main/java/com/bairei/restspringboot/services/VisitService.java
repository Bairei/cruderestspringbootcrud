package com.bairei.restspringboot.services;

import com.bairei.restspringboot.models.User;
import com.bairei.restspringboot.models.Visit;

import java.util.List;

public interface VisitService {
//    void appointmentReminder();
    Visit save(Visit visit);
    Visit saveOrUpdate(Visit visit);
    List<Visit> findAll();
    Visit findOne(Integer id);
    void delete(Integer id);
    List<Visit> findAllByPatient(User patient);
}
