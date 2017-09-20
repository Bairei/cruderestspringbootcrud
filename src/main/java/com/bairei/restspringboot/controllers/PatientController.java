package com.bairei.restspringboot.controllers;

import com.bairei.restspringboot.models.User;
import com.bairei.restspringboot.models.Error;
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
@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
public class PatientController {

    private final static Logger log = Logger.getLogger(PatientController.class.toString());

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public PatientController(UserService userService, RoleService roleService){
        this.userService = userService;
        this.roleService = roleService;
    }

    @RequestMapping(value = "/patient",method = RequestMethod.GET)
    public ResponseEntity<List<User>> list(){
        return new ResponseEntity<>(userService.findUsersByRolesContaining(roleService.getUserRole()), HttpStatus.OK);
    }

    @RequestMapping (value = "/patient", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<User> savePatient(@RequestBody User patient) throws InternalServerException {
        try {
            if (patient.getRoles().size() < 1) patient.setRoles(roleService.createUserRole());
            User savedPatient = userService.save(patient);
            return new ResponseEntity<>(savedPatient, HttpStatus.OK);
        } catch (Exception e) {
            log.warning(e.toString());
            throw new InternalServerException(e);
        }
    }

    @RequestMapping (value = "/patient/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> showPatient (@PathVariable Integer id) throws UserNotFoundException {
        User user = userService.findOne(id);
        if (user == null || !user.getRoles().contains(roleService.getUserRole())) throw new UserNotFoundException(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/patient/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<User> deletePatient(@PathVariable Integer id) throws UserNotFoundException, InternalServerException {
        User user = userService.findOne(id);
        if (user == null || !user.getRoles().contains(roleService.getUserRole())) throw new UserNotFoundException(id);
        try {
            userService.delete(id);
        } catch (UserNotFoundException unfe) {
            throw new UserNotFoundException(id);
        } catch (Exception e){
            throw new InternalServerException(e);
        }
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Error> userNotFound(UserNotFoundException e){
        Error error = new Error(7, "Patient with id " + e.getId() + "was not found!");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<Error> internalServerException(InternalServerException e){
        Error error = new Error(8, "Internal Server Exception:\n" + e.getException());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
