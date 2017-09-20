package com.bairei.restspringboot.services;

import com.bairei.restspringboot.models.Role;
import com.bairei.restspringboot.models.User;
import com.bairei.restspringboot.exceptions.InternalServerException;
import com.bairei.restspringboot.exceptions.UserNotFoundException;
import com.bairei.restspringboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Cacheable("cache")
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User save(User user) throws Exception {
        if(user.getSecret() == null) user.setSecret("");
        if(user.getPassword() == null || user.getPassword().equals("")
               || user.getEmail().equals("") || user.getEmail() == null || user.getRoles().size() < 1){
           throw new Exception("You can't save an user with no password, email or without any roles!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
        if (!user.getSecret().equals("")){
            user.setSecret(passwordEncoder.encode(user.getSecret()));
        }
        return userRepository.save(user);
    }

    @Override
    public User put(User user) throws UserNotFoundException, InternalServerException, Exception {

        if (user.getId() == null) return this.save(user);

        User detachedUser = userRepository.getOne(user.getId());
        if(detachedUser == null) throw new UserNotFoundException(user.getId());
        if(user.getEmail() != null) detachedUser.setEmail(user.getEmail());
        if(user.getName() != null) detachedUser.setName(user.getName());
        if(user.getSurname() != null) detachedUser.setSurname(user.getSurname());
        if(!user.getRoles().equals(detachedUser.getRoles())) detachedUser.setRoles(user.getRoles());
        if(user.getPassword() != null && user.getConfirmPassword() != null && user.getPassword().equals(user.getConfirmPassword())){
            detachedUser.setPassword(passwordEncoder.encode(user.getPassword()));
            detachedUser.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
        }
        if (user.getPassword() != null && !user.getPassword().equals(user.getConfirmPassword())) {
            throw new InternalServerException(new Exception("Please provide correct password and / or confirmation"));
        }
        return userRepository.save(detachedUser);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Cacheable("cache")
    @Override
    public User findOne(Integer id) {
        return userRepository.getOne(id);
    }

    @Override
    public List<User> findUsersByRolesContaining(Role role) {
        return userRepository.findUsersByRolesContaining(role);
    }

    @Override
    public void delete(Integer id) throws Exception, UserNotFoundException {
        if(this.findOne(id) == null) throw new UserNotFoundException(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getName().equalsIgnoreCase(this.findOne(id).getEmail())) throw new Exception("You can't remove your own account!");
        userRepository.deleteById(id);
    }

}
