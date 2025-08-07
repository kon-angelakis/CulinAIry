package com.kangel.thesis.aipowered_location_advisor.Config.Security.Auth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kangel.thesis.aipowered_location_advisor.Models.UserJwtDTO;
import com.kangel.thesis.aipowered_location_advisor.Services.UserService;
import com.kangel.thesis.aipowered_location_advisor.Services.Authentication.Auth.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter{

    @Autowired
    private JwtService jwtService;
    @Autowired
    private ApplicationContext context;

    //Security filter that prioritizes JWT tokens over the conventional username passw authentication
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                
        String authHeader = request.getHeader("authorization");
        String receivedToken = null;
        UserJwtDTO userDTO = null;

        //Extract the JWT token from the Authorization header and extract the userDTO details
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            receivedToken = authHeader.substring(7);
            userDTO = jwtService.extractUser(receivedToken);
        }

        //If the userDTO is not null and the user is not already authenticated, authenticate the user
        if (userDTO != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = context.getBean(UserService.class).loadUserByUsername(userDTO.getUsername());
            if (jwtService.validateToken(receivedToken, userDetails)) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }
        filterChain.doFilter(request, response);
    }

}
