package com.bairei.restspringboot.exceptions;

public class UserNotFoundException extends Throwable {

    private Integer id;

    public UserNotFoundException(Integer id){
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
