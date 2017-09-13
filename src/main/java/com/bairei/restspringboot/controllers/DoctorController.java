package com.bairei.restspringboot.controllers;

import com.bairei.restspringboot.domain.Error;
import com.bairei.restspringboot.domain.User;
import com.bairei.restspringboot.exceptions.InternalServerException;
import com.bairei.restspringboot.exceptions.UserNotFoundException;
import com.bairei.restspringboot.services.RoleService;
import com.bairei.restspringboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class DoctorController {

    private final static Logger log = Logger.getLogger(DoctorController.class.toString());

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;


    @RequestMapping(value = "/doctors",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<User>> list(){
        return new ResponseEntity<>(userService.findUsersByRolesContaining(roleService.getAdminRole()), HttpStatus.OK);
    }


    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @RequestMapping (value = "/doctor", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<User> saveDoctor(@RequestBody User doctor) throws InternalServerException {
        try {
            if(doctor.getRoles().size() < 1) doctor.setRoles(roleService.createAdminRole());
            User savedDoctor = userService.save(doctor);
            return new ResponseEntity<>(savedDoctor,HttpStatus.OK);
        } catch (Exception e) {
            throw new InternalServerException(e);
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @RequestMapping (value = "/doctor", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<User> saveOrUpdateDoctor(@RequestBody User doctor) throws InternalServerException, UserNotFoundException {
        try {
            User doctorToPut = userService.put(doctor);
            return new ResponseEntity<>(doctorToPut,HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(doctor.getId());
        } catch (Exception e) {
            throw new InternalServerException(e);
        }
    }

    @RequestMapping (value = "/doctor/{id}", produces = "application/json")
    public ResponseEntity<User> showDoctor (@PathVariable Integer id) throws UserNotFoundException {
        User user = userService.findOne(id);
        if (user == null || !user.getRoles().contains(roleService.getAdminRole())){
            throw new UserNotFoundException(id);
        }
        return new ResponseEntity<>(userService.findOne(id), HttpStatus.OK);
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "doctor/edit/{id}")
    public ResponseEntity<User> editDoctor(@PathVariable Integer id) throws UserNotFoundException {
        User user = userService.findOne(id);
        if (user == null || !user.getRoles().contains(roleService.getAdminRole())){
            throw new UserNotFoundException(id);
        }
        return new ResponseEntity<>(userService.findOne(id), HttpStatus.OK);
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "doctor/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteDoctor(@PathVariable Integer id) throws UserNotFoundException, InternalServerException {
        User deleted = userService.findOne(id);
        if (deleted == null || !deleted.getRoles().contains(roleService.getAdminRole())){
            throw new UserNotFoundException(id);
        }
        try {
            userService.delete(id);
        } catch (UserNotFoundException unfe) {
            throw new UserNotFoundException(id);
        } catch (Exception e) {
            throw new InternalServerException(e);
        }
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Error> userNotFound(UserNotFoundException e){
        Integer id = e.getId();
        Error error = new Error (5, "Doctor with id " + id + " has not been found!");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<Error> internalServerException(InternalServerException e){
        Error error = new Error(6, "Internal Error:\n" + e.getException());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}