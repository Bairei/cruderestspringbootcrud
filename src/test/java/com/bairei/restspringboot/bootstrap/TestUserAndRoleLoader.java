package com.bairei.restspringboot.bootstrap;

import com.bairei.restspringboot.domain.Role;
import com.bairei.restspringboot.domain.User;
import com.bairei.restspringboot.services.RoleService;
import com.bairei.restspringboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@Profile("test")
public class TestUserAndRoleLoader implements ApplicationListener<ContextRefreshedEvent> {

    private UserService userService;

    private RoleService roleService;

    private static final Logger log = Logger.getLogger(UserAndRoleLoader.class.toString());

    @Autowired
    public TestUserAndRoleLoader(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        roleService.save(userRole);
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        roleService.save(adminRole);

        User doctor = new User();
        doctor.setName("asdasd");
        doctor.setSurname("orortiehn");
        doctor.setEmail("asd@oro.com");
        doctor.setPassword("asdasd");
        doctor.setConfirmPassword("asdasd");
        doctor.setRoles(roleService.createAdminRole());
        doctor.setSecret("");
        try {
            userService.save(doctor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Saved doctor - id " + doctor.getId());

        User user = new User();
        user.setName("Jan");
        user.setSurname("Kowalski");
        user.setEmail("jakismail@mail.pl");
        user.setPassword("pass");
        user.setConfirmPassword("pass");
        user.setSecret("");
        user.setRoles(roleService.createUserRole());
        try {
            userService.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Saved user - id " + user.getId());
    }
}