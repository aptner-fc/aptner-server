package com.fc8.aptner.infrastructure.jwt;

import com.fc8.aptner.security.AptnerMember;
import com.fc8.aptner.security.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    public static final Long ACCESS_TOKEN_EXPIRE_TIME = Duration.ofHours(6).toMillis();
    public static final Long REFRESH_TOKEN_EXPIRE_TIME = Duration.ofDays(14).toMillis();
    public static final String TOKEN_PREFIX = "Bearer ";
//    public static final String ROLE = "role";

    private final SecretKey key;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtTokenProvider(
            @Value("${spring.jwt.secret}") String key,
            CustomUserDetailsService customUserDetailsService
    ) {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.customUserDetailsService = customUserDetailsService;
    }

    public String createAccessToken(Authentication authentication) {
        Date now = new Date();
        long accessTokenExpireTime = now.getTime() + ACCESS_TOKEN_EXPIRE_TIME;

//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));

        return Jwts.builder()
//                .claim(ROLE, authorities)
                .subject(authentication.getName())
                .issuedAt(now)
                .expiration(new Date(accessTokenExpireTime))
                .signWith(key)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        AptnerMember principal = (AptnerMember) customUserDetailsService.loadUserByUsername(getSubjectByToken(token));
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }

    public String resolveToken(String header) {
        return header.replace(TOKEN_PREFIX, "");
    }

    public boolean isValidateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        return !getExpirationByToken(token).before(new Date());
    }

    private String getSubjectByToken(String token) {
        return parseClaims(token).getSubject();
    }

    private Date getExpirationByToken(String token) {
        return parseClaims(token).getExpiration();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SignatureException ex) {
            throw new JwtException("Invalid JWT signature.");
        } catch (MalformedJwtException ex) {
            throw new JwtException("Invalid JWT token.");
        } catch (ExpiredJwtException ex) {
            throw new JwtException("Expired JWT token.");
        } catch (UnsupportedJwtException ex) {
            throw new JwtException("Unsupported JWT token.");
        } catch (IllegalArgumentException ex) {
            throw new JwtException("JWT claims string is empty.");
        }
    }

}
