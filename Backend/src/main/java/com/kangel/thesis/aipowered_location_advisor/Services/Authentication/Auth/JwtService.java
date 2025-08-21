package com.kangel.thesis.aipowered_location_advisor.Services.Authentication.Auth;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Models.User;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.UserDTO;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final Key SignedSecretKey;
    private final Dotenv env;

    public JwtService(Dotenv env) {
        this.env = env;
        String jwtSecret = this.env.get("JWT_SECRET");
        SignedSecretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Generate a JWT token for some of the users details using a user DT object
    public String GenerateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        UserDTO userDTO = new UserDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                null,
                user.getPfp());
        claims.put("User", userDTO);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(SignedSecretKey)
                .compact();
    }

    // Extract the user details from the received JWT token
    public UserDTO extractUser(String receivedToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SignedSecretKey)
                    .build()
                    .parseClaimsJws(receivedToken)
                    .getBody();

            Map<String, Object> userClaims = claims.get("User", Map.class);

            if (userClaims == null) {
                return null;
            }

            return new UserDTO(
                    (String) userClaims.get("firstName"),
                    (String) userClaims.get("lastName"),
                    (String) userClaims.get("email"),
                    (String) userClaims.get("username"),
                    null,
                    (String) userClaims.get("pfp"));
        } catch (JwtException | ClassCastException e) {
            return null;
        }
    }

    // Validate the received JWT token if it is not expired and the user exists
    public boolean validateToken(String receivedToken, UserDetails userDetails) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(SignedSecretKey)
                    .build()
                    .parseClaimsJws(receivedToken);

            String username = claimsJws.getBody().getSubject();
            return !isTokenExpired(claimsJws.getBody()) && username.equals(userDetails.getUsername());
        } catch (JwtException e) {
            return false;
        }
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}