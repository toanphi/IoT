package com.uet.iot.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class AuthUserDetailService implements UserDetailsService {

    List<SimpleGrantedAuthority> userAuth = Arrays.asList(new SimpleGrantedAuthority("USER"));
    List<SimpleGrantedAuthority> adminAuth = Arrays.asList(new SimpleGrantedAuthority("ADMIN"));
    List<SimpleGrantedAuthority> serviceAuth = Arrays.asList(new SimpleGrantedAuthority("SERVICE"));

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userFactory(username);
        return user;
    }

    private User userFactory(String username){
        if (username.equals("service"))
            return new User("service", "uet@iot@service", serviceAuth);
        else if (username.equals("admin"))
            return new User("admin", "uet@iot@admin", adminAuth);
        else
            return new User("user", "uet@iot@user", userAuth);
    }
}
