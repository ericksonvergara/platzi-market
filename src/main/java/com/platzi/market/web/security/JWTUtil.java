package com.platzi.market.web.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JWTUtil  {

    private static final String KEY = "Admin1234"; //Clave que se genera para pasarla al firmar el jsonwebtoken

    public String generateToken(UserDetails userDetails){
        return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date()) //Obtenemso el usuario
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 *60 *10)) //Tiempo de expiracion del webtoken
                .signWith(SignatureAlgorithm.HS256, KEY).compact(); //Firmamos el metodo en este caso se uso el HS256
    }


}
