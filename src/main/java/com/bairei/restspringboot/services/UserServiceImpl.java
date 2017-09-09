package com.bairei.restspringboot.services;

import com.bairei.restspringboot.domain.Role;
import com.bairei.restspringboot.domain.User;
import com.bairei.restspringboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User save(User user) {
        if(user.getSecret() == null) user.setSecret("");
//        if(user.getPassword() == null || user.getPassword().equals("")
//                || user.getEmail().equals("") || user.getEmail() == null || user.getRoles().size() < 1){
//            throw new Exception("You can't save an user with no password, email or without any roles!");
//        }
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findOne(Integer id) {
        return userRepository.getOne(id);
    }

    @Override
    public List<User> findUsersByRolesContaining(Role role) {
        return userRepository.findUsersByRolesContaining(role);
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

}
