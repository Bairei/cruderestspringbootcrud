package com.bairei.restspringboot.services;

import com.bairei.restspringboot.models.Role;
import com.bairei.restspringboot.models.User;
import com.bairei.restspringboot.exceptions.InternalServerException;
import com.bairei.restspringboot.exceptions.UserNotFoundException;

import java.util.List;

public interface UserService {
    User findUserByEmail (String email);
    User save(User user) throws Exception;
    User put(User user) throws Exception, UserNotFoundException, InternalServerException;
    List<User> findAll();
    User findOne(Integer id);
    List<User> findUsersByRolesContaining(Role role);
    void delete(Integer id) throws Exception, UserNotFoundException;
}
