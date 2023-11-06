package br.com.transportae.config;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull FilterChain filterChain
        ) throws ServletException, IOException {
            final String bearerPrefix = "Bearer ";
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String email;
            
            if (Objects.isNull(authHeader) || !authHeader.startsWith(bearerPrefix)) {
                filterChain.doFilter(request, response);
                return;
            }

            try {
                jwt = authHeader.substring(bearerPrefix.length());
                email = jwtService.extrairEmail(jwt);

                SecurityContext securityContext = SecurityContextHolder.getContext();

                if (Objects.nonNull(email) && Objects.isNull(securityContext.getAuthentication())) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

                    if (jwtService.isTokenValido(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                        );

                        authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                        );

                        securityContext.setAuthentication(authToken);
                    }
                }

                filterChain.doFilter(request, response);

            } catch (Exception exception) {
                exceptionResolver.resolveException(request, response, null, exception);
            }
    }
    
}
