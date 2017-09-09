package com.bairei.restspringboot.exceptions;

public class VisitNotFoundException extends Throwable {
    private Integer id;

    public VisitNotFoundException(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
