package ru.mail.senokosov.artem.web.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.mail.senokosov.artem.service.model.UserLogin;
import ru.mail.senokosov.artem.web.model.LoginRequest;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/auth/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            LoginRequest loginRequest = new ObjectMapper().readValue(sb.toString(), LoginRequest.class);
            log.info("Attempting authentication for user: {}", loginRequest.getEmail());

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword());
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException exception) {
            log.error("Error in attemptAuthentication: ", exception);
            throw new RuntimeException(exception);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String token = generateJwtToken(authResult);
        log.info("Authentication successful for user: {}", ((UserLogin) authResult.getPrincipal()).getUsername());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = "{\"token\":\"" + token + "\"}";

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.warn("Authentication attempt failed: {}", failed.getMessage());
        super.unsuccessfulAuthentication(request, response, failed);
    }

    private String generateJwtToken(Authentication authResult) {
        UserLogin userLogin = (UserLogin) authResult.getPrincipal();
        long now = System.currentTimeMillis();
        String base64Secret = "qxBrVY8AbdTqUA1w+XrFNDzm2O2Aju1mN+eyiIgFJ6Udx1uDfyy/6KSGpqyHN6uNwrgDpFS+7C+bX6+QlVJdog==";
        SecretKey secretKey = decodeSecretKey(base64Secret);

        return Jwts.builder()
                .setSubject(userLogin.getUsername())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 7200000))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    private SecretKey decodeSecretKey(String base64Secret) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Secret);
        return Keys.hmacShaKeyFor(decodedKey);
    }
}