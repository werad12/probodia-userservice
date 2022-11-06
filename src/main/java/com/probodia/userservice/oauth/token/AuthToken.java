package com.probodia.userservice.oauth.token;

import com.probodia.userservice.oauth.exception.TokenValidFailedException;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.security.Key;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class AuthToken {

    @Getter
    private final String token;
    private final Key key;

    private static final String AUTHORITIES_KEY = "role";

    AuthToken(String id, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(id, expiry);
    }

    AuthToken(String id, String role, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(id, role, expiry);
    }

    private String createAuthToken(String id, Date expiry) {
        return Jwts.builder()
                .setSubject(id)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }

    private String createAuthToken(String id, String role, Date expiry) {
        log.debug("Create Auth Token {} , {}, {}", id,role,key.getEncoded());
        return Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }

    public boolean validate() {
        log.debug("validate : {}",this.getTokenClaims());
        return this.getTokenClaims() != null;
    }

    public boolean validateForExpired(){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e){
            return true;
        } catch (Exception e){
            return false;
        }
        return false;
    }

    public Claims getTokenClaims() {
        String expired = null;
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            log.debug("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.debug("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            expired = "Not invalid";
            log.debug("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.debug("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.debug("JWT token compact of handler are invalid.");
        } catch (Exception e){
            log.debug("Token Auth failed");
        }
        return null;
    }

    public Claims getExpiredTokenClaims() {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.debug("Expired JWT token.");
            return e.getClaims();
        }
        return null;
    }
}
