package com.taurus.auction.service;

import com.taurus.auction.domain.User;
import com.taurus.auction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clenio on 19/01/2018.
 */
@Service
public class UserService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRegistry sessionRegistry;

    public List<User> findAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User findUserByUsername(String username) { return userRepository.findByUsername(username); }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s);

        if(user == null) {
            throw new UsernameNotFoundException(String.format("The username %s doesn't exist", s));
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(user.getRole().getDescription()));

        UserDetails userDetails = new org.springframework.security.core.userdetails.
                User(user.getUsername(), user.getPassword(), authorities);


        sessionRegistry.registerNewSession("taurussession"+userDetails.getUsername(),user);

        return userDetails;
    }
}
