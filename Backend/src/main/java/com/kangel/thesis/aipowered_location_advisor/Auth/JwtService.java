package com.kangel.thesis.aipowered_location_advisor.Auth;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Users.User;
import com.kangel.thesis.aipowered_location_advisor.Users.UserJwtDTO;

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

    @Autowired
    public JwtService(Dotenv env) {
        this.env = env;
        String jwtSecret = this.env.get("JWT_SECRET");
        SignedSecretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Generate a JWT token for some of the users details using a user DT object
    public String GenerateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        UserJwtDTO userDTO = new UserJwtDTO
        (   
            user.getFirstName(), 
            user.getLastName(), 
            user.getEmail(), 
            user.getUsername(), 
            user.getPfp()
        );
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
    public UserJwtDTO extractUser(String receivedToken) {
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

            return new UserJwtDTO(
                    (String) userClaims.get("firstName"),
                    (String) userClaims.get("lastName"),
                    (String) userClaims.get("email"),
                    (String) userClaims.get("username"),
                    (String) userClaims.get("pfp")
            );
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
            return username.equals(userDetails.getUsername()) && !isTokenExpired(claimsJws.getBody());
        } catch (JwtException e) {
            return false;
        }
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}