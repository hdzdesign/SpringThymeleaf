package chc.tfm.udt.auth.filter;

import chc.tfm.udt.entidades.UsuarioEntity;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Al igual que usamos para JPA una interfaz UserDetailsService que nos provee de un metodo para recuperar todos los
 * roles que estan definidos en la clase de SpringSecurity ,  aqui extendemos de esta clase filtro que nos va a
 * permitir recuperar los Authenticatiion , si vemos la clase nos dice por constructor que solo sera llamado cuando
 * la ruta sea /login , aplicando el metodo post.
 *
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));

    }

    /**
     * Como dije antes vamos a tener 1 contenedor que le pasamos las credenciales  y nos devuelve el Token
     * 1º Observamos la clase padre y reutilizamos las validaciones del Username y Password para crear nuestro metodo
     *    que hemos implementado
     * 2º UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,password );
     * 3º Creamos la variable AuthenticationManager y la inicializamos por constructor
     * 4º Retornamos a través del Autentication manager el authToken creado anteriormente.
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        /**
         * Recuperar lso datos en JSON desde postman
         * 1º creamos las variables String username y password
         * 2º Si los parametros vienen vacios se esta indicando que se estan tratando los datos en el body como string
         * 3º entonces creamos unavariable tipo Model Usuario.
         * 4º recuperarmos los datos del request con el metodo getInputStream.
         * 5 º Lo almacenamos en  1 variable ya convertido a Objeto con ObjectMapper pero en vez de escribir leemos readValue
         * 6º Seteamos los valroes recuperados desde el modelo a las variables
         * 7º Manejamos las excepciones con try catch.
         */
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        if(username !=null && password != null){
            logger.info("Username desde request Parameter ( form-data) : " + username);
            logger.info("Username desde request Parameter ( form-data) : " + password);
        }else{
            UsuarioEntity usuario = null;
            try {
                usuario = new ObjectMapper().readValue(request.getInputStream(), UsuarioEntity.class);
               username = usuario.getUsername();
               password = usuario.getPassword();
                logger.info("Username desde request request InputStream ( raw ) : " + username);
                logger.info("Username desde request request InputStream ( raw ) : " + password);
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        username = username.trim();
        /**
         * Instanciamos el objeto AuthenticationToken, que es 1 contenedor donde almacenaremos el objeto principal y las credenciales
         */
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,password );
        return authenticationManager.authenticate(authToken);
    }

    /**
     * 1º  Obtenemos el autentication , donde almacenamos los datos del usuario
     * 2º  Con el username ya podemos crear el Token
     * 3º  Se firma con un algoritmo y una clave secreta
     * 4º  creamos el Header , pasamos el autentication y pasamos el Token con el prefijo Bearer
     * 5º  Creamos 1 map para pasar el token al usuario
     * 6º  Guardamos este mensaje en formato Json
     * 7º  Un estatus
     * 8º  El contentype pasamos que es un Json.
     * 9º  Creamos la KEY
     * 10º Seteamos el tiempo de Creación y de expiración
     * 11º Recuperamos los roles con authResult, recuperamos un Collection
     * 12º Al ser parte de los claims pues hay que añadirlos
     * 13º No se puede añadir los authorities como objeto , asique lo vamos a pasar a string con ObjectMapper.
     * 11º Importante añadir los claims
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = ((User)authResult.getPrincipal()).getUsername();
        Collection<? extends GrantedAuthority> roles =  authResult.getAuthorities();
        Claims claims = Jwts.claims();
        claims.put("authorities",new ObjectMapper().writeValueAsString(roles));
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        /**
         * Si queremos generar la clave secreta para decodificarla después.
         */
       String token = Jwts.builder()
               .setClaims(claims)
                .setSubject(username)
                .signWith(secretKey)
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + 3600000L))
               .compact();
        response.addHeader("Authorization", "Bearer" + token);
        Map<String,Object> body = new HashMap<String,Object>();
        body.put("token", token);
        body.put("user", (User) authResult.getPrincipal());
        body.put("mensaje",String.format("Hola %s, has inciado sesión con exito",username));
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(200);
        response.setContentType("application/json");
    }
}
