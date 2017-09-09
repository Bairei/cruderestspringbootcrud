package com.bairei.restspringboot.controllers;

import com.bairei.restspringboot.domain.Error;
import com.bairei.restspringboot.domain.Visit;
import com.bairei.restspringboot.exceptions.InternalServerException;
import com.bairei.restspringboot.exceptions.VisitNotFoundException;
import com.bairei.restspringboot.services.RoleService;
import com.bairei.restspringboot.services.UserService;
import com.bairei.restspringboot.services.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class VisitController {

    private final static Logger log = Logger.getLogger(VisitController.class.toString());

    @Autowired
    private VisitService visitService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;


    @RequestMapping(value = "/visits",method = RequestMethod.GET)
    public ResponseEntity<List<Visit>> list(){
        return new ResponseEntity<>(visitService.findAll(), HttpStatus.OK);
    }

    @RequestMapping("/visit/new")
    public ResponseEntity<Visit> newVisit(){
        return new ResponseEntity<>((Visit)null, HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping (value = "/visit", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Visit> saveVisit(@RequestBody Visit visit) throws InternalServerException {
        Visit savedVisit = visitService.save(visit);
        if (savedVisit == null) throw new InternalServerException();
        return new ResponseEntity<>(savedVisit, HttpStatus.CREATED);
    }

    @RequestMapping (value = "/visit", method = RequestMethod.PATCH, produces = "application/json")
    public ResponseEntity<Visit> updateVisit(@RequestBody Visit visit) throws InternalServerException {
        Visit savedVisit = visitService.saveOrUpdate(visit);
        if (savedVisit == null) throw new InternalServerException();
        return new ResponseEntity<>(savedVisit, HttpStatus.CREATED);
    }

    @RequestMapping (value = "/visit/{id}", method = RequestMethod.GET)
    public ResponseEntity<Visit> showVisit (@PathVariable Integer id, Model model) throws VisitNotFoundException {
        if (visitService.findOne(id) == null) throw new VisitNotFoundException(id);
        return new ResponseEntity<>(visitService.findOne(id), HttpStatus.OK);
    }

    @RequestMapping("/visit/edit/{id}")
    public ResponseEntity<Visit> editVisit(@PathVariable Integer id) throws VisitNotFoundException {
        if (visitService.findOne(id) == null) throw new VisitNotFoundException(id);
        return new ResponseEntity<>(visitService.findOne(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/visit/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Visit> deleteVisit(@PathVariable Integer id) throws VisitNotFoundException {
        Visit deletedVisit = visitService.findOne(id);
        if (deletedVisit == null) throw new VisitNotFoundException(id);
        visitService.delete(id);
        return new ResponseEntity<>(deletedVisit, HttpStatus.OK);
    }

    @ExceptionHandler(VisitNotFoundException.class)
    public ResponseEntity<Error> visitNotFound(VisitNotFoundException e){
        Error error = new Error (3, "Visit with id " + e.getId() + "was not found!");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<Error> internalServerException(InternalServerException e){
        Error error = new Error(4, "Internal Server Exception");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}