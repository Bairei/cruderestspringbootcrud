package com.bairei.restspringboot.repositories;

import com.bairei.restspringboot.domain.Role;
import com.bairei.restspringboot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findUserByEmail(String email);
    List<User> findUsersByRolesContaining(Role role);
}
