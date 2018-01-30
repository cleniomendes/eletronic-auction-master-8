package com.taurus.auction.controller;


import com.taurus.auction.domain.Role;
import com.taurus.auction.domain.User;
import com.taurus.auction.repository.RoleRepository;
import com.taurus.auction.repository.UserRepository;
import com.taurus.auction.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Clenio on 17/01/2018.
 */

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasAuthority('Administrador')")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SessionRegistry sessionRegistry;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public ResponseEntity getUsers() {
        List<User> users = userService.findAllUsers();
        if (users!=null){
            log.info(String.format("Found %s users in database", users.size()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PostMapping
    @PatchMapping
    public ResponseEntity saveUser(@RequestBody User u) {

        if (userService.findUserByUsername(u.getUsername()) != null) {
            throw new UsernameNotFoundException(String.format("The username %s already exist", u.getUsername()));
        }

        try {
            u.setPassword(bCryptPasswordEncoder.encode(u.getPassword()));
            userRepository.saveAndFlush(u);
            log.info(String.format("save user=%s success", u.getUsername()));
        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(u);
    }

    @DeleteMapping
    public ResponseEntity deleteUser(@RequestBody User u) {
        try {
            userRepository.delete(u);
            log.info(String.format("delete user=%s success", u.getUsername()));
        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(u);
    }


    @GetMapping(value = "/roles")
    public ResponseEntity listAllRoles(){
        List<Role> roles = roleRepository.findAll();
        if (roles!=null){
            log.info(String.format("Found %s roles in database", roles.size()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }

    @GetMapping(value = "/logged")
    public ResponseEntity getAllLoggedUsers(){
        List<Object> principals = sessionRegistry.getAllPrincipals();

        List<String> usersNamesList = new ArrayList<>();

        for (Object principal: principals) {
            if (principal instanceof User) {
                usersNamesList.add(((User) principal).getUsername());
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(usersNamesList);
    }

    @PostMapping(value = "/logout")
    public void logout(@RequestBody Map<String,Object> requestJson){
        String username = requestJson.get("username").toString();
        sessionRegistry.removeSessionInformation("taurussession"+username);
    }

}
