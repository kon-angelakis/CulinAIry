package com.kangel.thesis.aipowered_location_advisor.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Config.Security.Auth.UserPrincipal;
import com.kangel.thesis.aipowered_location_advisor.Models.User;
import com.kangel.thesis.aipowered_location_advisor.Repositories.UserRepo;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User loginedUser = username.contains("@") ? userRepo.findByEmail(username).orElse(null)
                : userRepo.findByUsername(username).orElse(null);
        if (loginedUser == null)
            throw new UsernameNotFoundException("User not found");
        return new UserPrincipal(loginedUser);
    }

}
