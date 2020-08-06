package com.concrete.poletime.jwt;

import com.concrete.poletime.security.MyUserDetails;
import com.concrete.poletime.utils.properties.ApplicationProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private ApplicationProperties properties;

    @Autowired
    public JwtUtil(ApplicationProperties properties) {
        this.properties = properties;
    }

    public Long extractUserId(String token) {
        return Long.parseLong(extractAllClaims(token).get("id").toString());
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role").toString();
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(properties.getJwtSecretKey()).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(MyUserDetails userDetails) {
        Map<String, Object> claims = createClaims(userDetails);
        return createToken(claims);
    }

    public Map createClaims(MyUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userDetails.getId());
        claims.put("role", userDetails.getRole());
        return claims;
    }

    private String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + properties.getJwtExpiration()))
                .signWith(SignatureAlgorithm.HS256, properties.getJwtSecretKey()).compact();
    }

    public Boolean validateToken(String token, MyUserDetails userDetails) {
        final Long userId = extractUserId(token);
        return ((userId == userDetails.getId()) && !isTokenExpired(token));
    }
}
