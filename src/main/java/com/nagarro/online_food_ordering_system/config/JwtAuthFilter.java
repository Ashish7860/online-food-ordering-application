package com.nagarro.online_food_ordering_system.config;

import com.nagarro.online_food_ordering_system.entity.Customer;
import com.nagarro.online_food_ordering_system.repository.CustomerRepository;
import com.nagarro.online_food_ordering_system.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final CustomerRepository customerDao;

    public JwtAuthFilter(JwtTokenUtil jwtTokenUtil, CustomerRepository customerDao) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.customerDao = customerDao;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String header = request.getHeader("Authorization");
        System.out.println("Authorization header: " + header);

        if (header != null && header.startsWith("Bearer ")) {
            final String token = header.substring(7).trim();
            System.out.println("Token: " + token);

            if (jwtTokenUtil.validateToken(token)) {
                String email = jwtTokenUtil.getEmailFromToken(token);
                System.out.println("Email from token: " + email);

                Customer customerDetails = customerDao.findByEmail(email).orElse(null);
                if (customerDetails != null) {
                    String userRole = jwtTokenUtil.getRoleFromToken(token);
                    System.out.println("User role from token: " + userRole);

                    GrantedAuthority authority = new SimpleGrantedAuthority(userRole);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            customerDetails, null, List.of(authority));
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    System.out.println("Authentication set in SecurityContext");
                } else {
                    System.out.println("No customer found for email: " + email);
                }
            } else {
                System.out.println("Invalid token");
            }
        } else {
            System.out.println("No Bearer token found, proceeding without authentication");
        }

        filterChain.doFilter(request, response);
    }
}

