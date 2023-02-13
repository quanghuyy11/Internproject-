package com.mgm.amazing_volunteer.security;

import com.mgm.amazing_volunteer.model.CustomUserDetails;
import com.mgm.amazing_volunteer.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import io.jsonwebtoken.*;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtAuthenticateProvider jwtAuthenticateProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader("Authorization");

        String jwtToken = null;
        String email = null;
        if(header != null && header.startsWith("Bearer ")){
            jwtToken = header.substring(7);
            try {
                email = jwtAuthenticateProvider.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e){
                System.out.println("Unable to get token");
            } catch (ExpiredJwtException e){
                System.out.println("Jwt token is expired");
            }
        }

        if (email != null){
            CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(email);
            //check jwt token
            if(jwtAuthenticateProvider.validateToken(jwtToken, customUserDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null,customUserDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
