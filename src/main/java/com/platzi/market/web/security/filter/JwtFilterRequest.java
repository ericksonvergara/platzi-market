package com.platzi.market.web.security.filter;

import com.platzi.market.domain.service.PlatziUserDetailsService;
import com.platzi.market.web.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilterRequest extends OncePerRequestFilter {
    @Autowired
    private JWTUtil jwtUtil; // Inyectamos para poder verificar el usuario
    @Autowired
    private PlatziUserDetailsService platziUserDetailsService; //Servicio que hace la sveces de autenticacion
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization"); // Verificamos si lo que viene del encabezado de la peticion es un token y si es correcto

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")){
            String jwt = authorizationHeader.substring(7);// Capturamos el JWT
            String username = jwtUtil.extractUsername(jwt); //Verificamos el usuario del JWT

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){ // Se realiza con el fin de verificar que en el contexto no exista una autenticacion para el usuario.
                UserDetails userDetails = platziUserDetailsService.loadUserByUsername(username);

                if (jwtUtil.validaToken(jwt, userDetails)){// Validamos si el token es correcto
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Detalles de la conexion que esta recibiendo

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        }

        filterChain.doFilter(request, response);
    }
}
