package com.estudos.backend.config.security;

import com.estudos.backend.exception.TokenInvalidoException;
import com.estudos.backend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            var token = procurarToken(request);
            if(token != null){
                var login = tokenService.validarToken(token);
                if(login == null){
                    throw new TokenInvalidoException("Token inválido");
                }
                UserDetails user = userRepository.findByUsername(login);

                if(user == null){
                    throw new TokenInvalidoException("Usuário não encontrado");
                }
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
            }

            filterChain.doFilter(request,response);

        }catch (TokenInvalidoException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private String procurarToken(HttpServletRequest request) {
        var token = request.getHeader("Authorization");
        if (token == null ) {
            return null;
        }else{
            return token.replace("Bearer ", "");
        }
    }
}
