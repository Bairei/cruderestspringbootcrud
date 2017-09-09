package com.bairei.restspringboot.services;

import com.bairei.restspringboot.domain.Role;
import com.bairei.restspringboot.domain.User;

import java.util.List;

public interface UserService {
    User findUserByEmail (String email);
    User save(User user) throws Exception;
    List<User> findAll();
    User findOne(Integer id);
    List<User> findUsersByRolesContaining(Role role);
    void delete(Integer id);
}
