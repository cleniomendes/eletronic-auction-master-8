package com.taurus.auction.controller;


import com.taurus.auction.domain.Role;
import com.taurus.auction.domain.User;
import com.taurus.auction.repository.RoleRepository;
import com.taurus.auction.repository.UserRepository;
import com.taurus.auction.schedule.UserScheduled;
import com.taurus.auction.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by Clenio on 17/01/2018.
 */

@RestController
@RequestMapping("/api/users")
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
    private UserScheduled userScheduled;

    @Autowired
    private SessionRegistry sessionRegistry;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    @PreAuthorize("hasAuthority('Administrador') or hasAuthority('Cliente')")
    public ResponseEntity getUsers() {
        List<User> users = userService.findAllUsers();
        if (users != null) {
            log.info(String.format("Found %s users in database", users.size()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping(value = "/auction/{id}")
    @PreAuthorize("hasAuthority('Administrador') or hasAuthority('Cliente')")
    public ResponseEntity getUsersByAuction(@PathVariable("id") Long idAuction) {
        List<User> users = userService.findAllUsers();
        List<User> usersList = new ArrayList<>();

        if (users == null) {
            log.info(String.format("Not Found %s users in database"));
        } else {
            users.stream().forEach(user -> {
                if (user.getAuctionUsers() != null) {
                    if (user.getAuctionUsers().stream().filter(auctionUser -> auctionUser.getAuction().getId() == idAuction && auctionUser.getStatus()
                            .equals("A")).count() > 0L) {
                        usersList.add(user);
                    }
                }
            });
        }

        return ResponseEntity.status(HttpStatus.OK).body(usersList);
    }

    @PostMapping
    @PatchMapping
    @PreAuthorize("hasAuthority('Administrador')")
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
    @PreAuthorize("hasAuthority('Administrador')")
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
    @PreAuthorize("hasAuthority('Administrador')")
    public ResponseEntity listAllRoles() {
        List<Role> roles = roleRepository.findAll();
        if (roles != null) {
            log.info(String.format("Found %s roles in database", roles.size()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }

    @GetMapping(value = "/logged/{id}")
    @PreAuthorize("hasAuthority('Administrador')")
    public void getAllLoggedUsers(@PathVariable("id") Long idAuction) {
        //Get logged user for this request
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String currentPrincipalName = authentication.getName();

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

            log.info(String.format("Start schedule to send Logged Users"));
            Runnable task = () -> userScheduled.sendLoggedUsers(idAuction, executor, currentPrincipalName);
            executor.scheduleAtFixedRate(task, 2, 3, TimeUnit.SECONDS);
        }
    }

    @PostMapping(value = "/logout")
    @PreAuthorize("hasAuthority('Administrador') or hasAuthority('Cliente')")
    public void logout(@RequestBody Map<String, Object> requestJson) {
        String username = requestJson.get("username").toString();
        sessionRegistry.removeSessionInformation("taurussession" + username);
        log.info(String.format("Logout %s", username));
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('Administrador')")
    public ResponseEntity getUsersById(@PathVariable(value = "id") Long id) {
        User u = new User();
        try {
            u = userRepository.findOne(id);
            if (u == null) {
                throw new UsernameNotFoundException(String.format("The username %s doesn't exist"));
            }
            log.info(String.format("user =%s success", u.getUsername()));
        } catch (Exception e) {
            log.error(String.format("failed to persist database= %s", e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(u);
    }
}
