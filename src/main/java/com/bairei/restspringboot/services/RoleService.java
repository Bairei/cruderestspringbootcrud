package com.bairei.restspringboot.services;


import com.bairei.restspringboot.models.Role;

import java.util.Set;

public interface RoleService {
    Set<Role> createAdminRole();
    Set<Role> createUserRole();
    Role getUserRole();
    Role getAdminRole();
    Role save(Role role);
}
