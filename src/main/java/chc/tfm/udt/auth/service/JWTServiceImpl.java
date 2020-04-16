package chc.tfm.udt.auth.service;

import chc.tfm.udt.auth.SimpleGrantedAuthorityMixin;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
@Component
public class JWTServiceImpl implements JWTService {
    /**
     *  1º  Obtenemos el autentication , donde almacenamos los datos del usuario
     *  2º  Con el username ya podemos crear el Token
     *  3º  Se firma con un algoritmo y una clave secreta
     *  4º  creamos el Header , pasamos el autentication y pasamos el Token con el prefijo Bearer
     *  5º  Creamos 1 map para pasar el token al usuario
     *  6º  Guardamos este mensaje en formato Json
     *  7º  Un estatus
     *  8º  El contentype pasamos que es un Json.
     *  9º  Creamos la KEY
     *  10º Seteamos el tiempo de Creación y de expiración
     *  11º Recuperamos los roles con authResult, recuperamos un Collection
     *  12º Al ser parte de los claims pues hay que añadirlos
     *  13º No se puede añadir los authorities como objeto , asique lo vamos a pasar a string con ObjectMapper.
     *  11º Importante añadir los claims
     * @param auth
     * @return
     * @throws JsonProcessingException
     */
    public static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    public static final long EXPIRATION_DATE = 3600000L;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    @Override
    public String create(Authentication auth) throws IOException {

        String username = ((User)auth.getPrincipal()).getUsername();
        Collection<? extends GrantedAuthority> roles =  auth.getAuthorities();
        Claims claims = Jwts.claims();
        claims.put("authorities",new ObjectMapper().writeValueAsString(roles));
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .signWith(SECRET_KEY)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
                .compact();
        return token;
    }

    /**
     * Recuperamos el Token y lo validamos, si es correcto , seguimos adelante si no lanzamos IllegalArgumentException
     * @param token
     * @return
     */
    @Override
    public Boolean validate(String token) {

        try {
            getClaims(token);
            return true;
        }catch (JwtException | IllegalArgumentException e){
            e.printStackTrace();
           return false;
        }
    }

    /**
     *  1º llamamos a  Jwts.parser() para Authorization.
     *  2º asignamos la clave secreta, que hemos generado con SecretKey
     * @param token
     * @return
     */
    @Override
    public Claims getClaims(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(resolve(token))
                .getBody();
        return claims;
    }

    /**
     *
     * Recuperamos el nombre de usuario con la clase Claims y getSubject
     * @param token
     * @return
     */
    @Override
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Clase que recupera todos los roles , se genera un objeto Collection que extiende de todas las clases
     * GrantedAuthority, para recuperar los reoles es necesario pasar a string el Object.
     * Para ello necesitamos  de la ayuda de una clase Mix de SimpleGrantedAuthority, con sufijo Mixin.
     * como segundo argumento pasamos un Array de SimpleGratedAuthority[].class
     * Con la clase Arrays.asList, lo pasamos a un tipo Collection para devolverlo.
     * @param token
     * @return
     * @throws IOException
     */
    @Override
    public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {
        Object roles = getClaims(token).get("authorities");
        Collection<? extends GrantedAuthority> authorities =
                Arrays.asList(new ObjectMapper()
                        .addMixIn(SimpleGrantedAuthority.class,SimpleGrantedAuthorityMixin.class)
                        .readValue(roles.toString()
                                        .getBytes()
                                ,SimpleGrantedAuthority[].class));
        return authorities;
    }

    /**
     * Remplazamos el Header, eliminando Bearer por un espacio.
     * @param token
     * @return
     */
    @Override
    public String resolve(String token) {
        if(token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.replace(TOKEN_PREFIX, "");
        }
        return null;
    }
}
