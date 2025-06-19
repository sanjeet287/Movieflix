package com.movieflix.playbackservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String SECRET_KEY = "d1ef2f1324fd2b9798ff6c608b2b8afad05b915395472ba8a280063370c031204103d69b24f322d302b865cc4a2ad2afa10052311a5a7bd589a5abef3d51641318f3a0b79c6c52993e482fbb3372ea28094c6239cc8d909e08bc747c217917751f8b0a9a3ecb0d844e7bad7620b0471a003d2ca326d4a1fee0532fb4f6d57ce3524b7bae76a52b5a4db19cbc0d7cf1b26a03c72f6a17ff65658c58a0633b0b30fddf694dfade1c9fccdd1fb455eaaa2f8ca93bc5b1d2135971ea02d3aa94cc89260c2fefbcb459e5a066384675232922d2b00a5fd783999a015dddaefa3982f556f1ed2f2a5b6d230be86a13a91a58ca964a4e46e1938ec30a44bfbab9fd812d5ac0816817003219ae15ce35da39aacf0dd3938dfffd97af7c19762ce3884797b806ce8d7e8d631f1d25c8becc768219a12711ac17b49b636b1485315371d640d989a4b03bde4c0b1091034a5a1762c6974c889aa71ba817299b6c180660804518d4dace42a8a74f82ab416130fb8069bf388b65f7b6755431725b80262c6070d3c09f9748f49948697ae3203e7ab15c9e356b5dad6008af2b0b8406fe70eab647d1919654f2106e09167894f0aa1aed8416264c0b9e26172e8c74125d5966e69bd0e775f913dda90437a396afc75597c08f20b9a65d970ae1374862dc1d48d80901d2b1d5939a800ba7445ae6d2a2ef235d07e8bb3b7e78682cf676dd3e0a93";

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    
    public JwtAuthenticationFilter() {
        log.info("JwtAuthenticationFilter initialized");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("JWT Filter triggered for URI: {}", request.getRequestURI());

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            log.info("JWT token validated. User: {}", claims.getSubject());

            // Continue the filter chain
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.error("JWT token expired: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        }
    }

    // Optional: Filter only URLs starting with /api/videos
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Skip if URI does NOT start with /api/videos/
        return !request.getRequestURI().startsWith("/api/videos/");
    }
}
