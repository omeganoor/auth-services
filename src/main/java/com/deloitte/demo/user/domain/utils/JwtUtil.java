package com.deloitte.demo.user.domain.utils;

import com.deloitte.demo.user.config.security.exception.InvalidTokenException;
import com.deloitte.demo.user.domain.model.TokenClaims;
import com.deloitte.demo.user.domain.model.UserData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class JwtUtil {

  private static final String SECRET_KEY = "037c7a2a99625f76859b6c3e36165455aaf01eb869c234382c293d03f00af043"; // Use a stronger key
  private static final long expirationTimeMillis = 60; // 1 hour in milliseconds

  public static String generateToken (UserData userData) {
    Date expdate = Date.from(LocalDateTime.now().plusMinutes(expirationTimeMillis)
        .atZone(ZoneId.systemDefault()).toInstant());
    return Jwts.builder()
          .setId(String.valueOf(userData.getId()))
          .setSubject(userData.getUsername())
          .setIssuedAt(new Date())
          .setExpiration(expdate) // 1 hour expiration
          .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
          .compact();
    }

    public static Claims getTokenClaims (String token) {
      return Jwts.parser()
          .setSigningKey(SECRET_KEY)
          .parseClaimsJws(token)
          .getBody();
    }

    public static boolean isTokenExpired (String token) {
      return extractExpiration(token).before(new Date());
    }

    private static Date extractExpiration (String token) {
      return Jwts.parser()
          .setSigningKey(SECRET_KEY)
          .parseClaimsJws(token)
          .getBody()
          .getExpiration();
    }

    public static TokenClaims validateToken (String token) {
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