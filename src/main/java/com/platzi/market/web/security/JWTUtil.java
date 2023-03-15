package com.platzi.market.web.security;

import io.jsonwebtoken.Claims;
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

    public boolean validaToken(String token, UserDetails userDetails) { //Validamos que el jwt este valido y sea correcto
        return userDetails.getUsername().equals(extractUsername(token)) && !isTokenExpired(token); //Verificamos si el usuario que esta llegando a la peticion es el mismo que el que viene en el token.
    }
    public  String extractUsername(String token){ //Extraemos el usuario del JWT
        return getClaims(token).getSubject(); // EN el Subjet es donde esta el usuario de la peticion.
    }
    public boolean isTokenExpired(String token){ //Verificamos si el token ya expiro.
        return getClaims(token).getExpiration().before(new Date());
    }
    private Claims getClaims(String token){ //Metodo auxliar que retorna los Claim(Objetos dentro del JWT).
        return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
    }
}
