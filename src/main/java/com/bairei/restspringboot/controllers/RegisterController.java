package com.bairei.restspringboot.controllers;

import com.bairei.restspringboot.domain.User;
import com.bairei.restspringboot.services.RoleService;
import com.bairei.restspringboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

// TODO: handling POST request to allow creating either a patient user, or doctor (administrator)
//

@RestController
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

//    @Autowired
//    private LoginService loginService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ResponseEntity<User> userForm() {
        return new ResponseEntity<>((User) null, HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<User> saveUser(@ModelAttribute @Validated User user) {
        return new ResponseEntity<>((User) null, HttpStatus.NOT_IMPLEMENTED);
    }
}
