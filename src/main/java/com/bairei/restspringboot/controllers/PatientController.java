package com.bairei.restspringboot.controllers;

import com.bairei.restspringboot.domain.User;
import com.bairei.restspringboot.domain.Error;
import com.bairei.restspringboot.exceptions.InternalServerException;
import com.bairei.restspringboot.exceptions.UserNotFoundException;
import com.bairei.restspringboot.services.RoleService;
import com.bairei.restspringboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class PatientController {

    private final static Logger log = Logger.getLogger(PatientController.class.toString());

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;


    @RequestMapping(value = "/patients",method = RequestMethod.GET)
    public ResponseEntity<List<User>> list(){
        return new ResponseEntity<>(userService.findUsersByRolesContaining(roleService.getUserRole()), HttpStatus.OK);
    }

    @RequestMapping("/patient/new")
    public ResponseEntity<User> newPatient(){
        return new ResponseEntity<>((User) null, HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping (value = "patient", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<User> savePatient(@RequestBody User patient) throws InternalServerException {
        try {
            User savedPatient = userService.save(patient);
            return new ResponseEntity<>(savedPatient, HttpStatus.OK);
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    @RequestMapping (value = "patient/{id}")
    public ResponseEntity<User> showPatient (@PathVariable Integer id) throws UserNotFoundException {
        User user = userService.findOne(id);
        if (user == null || !user.getRoles().contains(roleService.getUserRole())) throw new UserNotFoundException(id);
        return new ResponseEntity<>(userService.findOne(id), HttpStatus.OK);
    }

    @RequestMapping(value = "patient/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<User> deletePatient(@PathVariable Integer id) throws UserNotFoundException {
        User user = userService.findOne(id);
        if (user == null || !user.getRoles().contains(roleService.getUserRole())) throw new UserNotFoundException(id);
        userService.delete(id);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Error> userNotFound(UserNotFoundException e){
        Error error = new Error(7, "Patient with id " + e.getId() + "was not found!");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<Error> internalServerException(InternalServerException e){
        Error error = new Error(8, "Internal Server Exception");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
