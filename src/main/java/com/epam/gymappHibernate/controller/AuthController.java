package com.epam.gymappHibernate.controller;

import com.epam.gymappHibernate.dao.TraineeRepository;
import com.epam.gymappHibernate.dao.UserRepository;

import com.epam.gymappHibernate.dto.AuthRequest;
import com.epam.gymappHibernate.services.LoginAttemptService;
import com.epam.gymappHibernate.services.SecurityService;
import com.epam.gymappHibernate.services.TokenBlacklistService;
import com.epam.gymappHibernate.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SecurityService userDetailsService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LoginAttemptService loginAttemptService;
    @Autowired
    private TokenBlacklistService tokenBlacklistService;




    @Autowired
    private JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(TraineeRepository.class);


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthRequest authenticationRequest) throws Exception {
        String username = authenticationRequest.getUsername();

        if (loginAttemptService.isBlocked(username)) {
            return ResponseEntity.status(HttpStatus.LOCKED).body("Account is locked due to too many failed login attempts. Please try again later.");
        }

        try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );
            loginAttemptService.loginSucceeded(username);
        } catch (Exception e) {
            loginAttemptService.loginFailed(username);
            throw e;
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(jwt);

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            tokenBlacklistService.addToBlacklist(jwt);
        }



        return ResponseEntity.ok().body("Logged out successfully");
    }

}

