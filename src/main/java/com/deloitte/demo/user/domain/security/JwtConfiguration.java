package com.deloitte.demo.user.domain.security;

import com.deloitte.demo.user.config.security.exception.InvalidTokenException;
import com.deloitte.demo.user.domain.model.TokenClaims;
import com.deloitte.demo.user.domain.model.UserData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Configuration
public class JwtConfiguration {

    @Value("${security.authentication.jwt.base64-secret}")
    private String jwtSecretKey;
//  private static final String SECRET_KEY = "N2Q5OTBmNjcwNWQ0MDExN2VhOTcwNWI0ZTJiZjZhYmZmOTQzNTNkZjQ2ZDA1NDIwYTQ2MjY5NTdkODk3ZWVmOWUxOTcyMWI4MDYxZmZlODgwMmE4OTBkZjA0YmQ3MTljNzY5ZjI2NmNiM2JlMGE5Y2E1MGE4MzYwMjRhOTA1Njk="; // Use a stronger key

    @Value("${security.authentication.jwt.expiration.time}")
    private long jwtExpirationTime; // 1 hour in milliseconds
//  private static long EXPIRATION_TIME = 86400; // 1 hour in milliseconds

    public String generateToken (UserData userData) {
      Date expDate = Date.from(LocalDateTime.now().plusSeconds(jwtExpirationTime)
        .atZone(ZoneId.systemDefault()).toInstant());
    return Jwts.builder()
          .setId(String.valueOf(userData.getId()))
          .setSubject(userData.getUsername())
          .setIssuedAt(new Date())
          .setExpiration(expDate) // 1 hour expiration
          .signWith(SignatureAlgorithm.HS256, getSecretKey(jwtSecretKey))
          .compact();
    }

    private byte[] getSecretKey (String secretKey) {
      byte[] keyBytes = Base64.getDecoder().decode(secretKey);;
      return keyBytes;
    }

    private Claims getTokenClaims (String token) {
      return Jwts.parser()
          .setSigningKey(getSecretKey(jwtSecretKey))
          .parseClaimsJws(token)
          .getBody();
    }

    private boolean isTokenExpired (String token) {
      return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration (String token) {
      return Jwts.parser()
          .setSigningKey(getSecretKey(jwtSecretKey))
          .parseClaimsJws(token)
          .getBody()
          .getExpiration();
    }

    public TokenClaims validateToken (String token) {
      log.info("validateToken({})", token);
      if (isTokenExpired(token)) {
        throw new InvalidTokenException("Token Not Valid");
      }
      Claims claims = getTokenClaims(token);
      return TokenClaims.builder()
          .active(!isTokenExpired(token))
          .exp(claims.getExpiration().getTime())
          .username(claims.getSubject())
          .subject(claims.getSubject())
          .iat(claims.getIssuedAt().getTime())
          .uid(claims.getId())
          .build();
    }
  }