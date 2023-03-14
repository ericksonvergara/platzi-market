package com.platzi.market.web.controller;

import com.platzi.market.domain.dto.AuthenticationRequest;
import com.platzi.market.domain.dto.AuthenticationResponse;
import com.platzi.market.domain.service.PlatziUserDetailsService;
import com.platzi.market.web.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager; //Inyectamos de spring
    @Autowired
    private PlatziUserDetailsService platziUserDetailsService; // Inyeccion del servicio para obtener los detalles del usuario
    @Autowired
    private JWTUtil jwtUtil; // Inyectamos la clase del JWT

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> createToken(@RequestBody AuthenticationRequest request){ //Respondemos un ResponseEntity de AuthenticationResponse de la clase DTO que creamos anteriormente.
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())); // Comprobamos atravez de usuario y contraseña
            UserDetails userDetails = platziUserDetailsService.loadUserByUsername(request.getUsername()); // Obtenemos los detalles desde el servicio que se creo
            String jwt = jwtUtil.generateToken(userDetails); //Generamos el JWT

            return new ResponseEntity<>(new AuthenticationResponse(jwt), HttpStatus.OK);
        }catch (BadCredentialsException e){
              return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
