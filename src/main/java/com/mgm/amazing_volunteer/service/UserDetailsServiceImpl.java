package com.mgm.amazing_volunteer.service;

import com.mgm.amazing_volunteer.dto.JwtRequest;
import com.mgm.amazing_volunteer.dto.JwtResponse;
import com.mgm.amazing_volunteer.model.CustomUserDetails;
import com.mgm.amazing_volunteer.model.User;
import com.mgm.amazing_volunteer.repository.UserRepository;
import com.mgm.amazing_volunteer.security.JwtAuthenticateProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtAuthenticateProvider jwtAuthenticateProvider;

    public JwtResponse createJwtResponse(JwtRequest jwtRequest) throws Exception {
        String email = jwtRequest.getEmail();
        String password = jwtRequest.getPassword();
        authenticate(email, password);

        final CustomUserDetails customUserDetails = (CustomUserDetails) loadUserByUsername(email);

        String generateToken = jwtAuthenticateProvider.generateToken(customUserDetails);

        return new JwtResponse(generateToken);
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("User is disabled");
        } catch (BadCredentialsException e) {
            throw new Exception("Bad credentials from user");
        }
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Username %s not found", email));
        }
        return new CustomUserDetails(user);
    }

}
