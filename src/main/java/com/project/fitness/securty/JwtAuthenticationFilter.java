package com.project.fitness.securty;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    private UserDetailsService userDetailsService;



    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {


        System.out.println("AuthTokenFilterChain Called");


        try {

            String jwt = parseJwt(request);


            if(jwt != null && jwtUtils.validateJwtToken(jwt)){


                String username = jwtUtils.getUserIdFromToken(jwt);


                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);



                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );



                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);



            }


        } catch(Exception e){

            e.printStackTrace();

        }



        filterChain.doFilter(request,response);

    }



    private String parseJwt(HttpServletRequest request){

        return jwtUtils.getJwtFromHeader(request);

    }


}