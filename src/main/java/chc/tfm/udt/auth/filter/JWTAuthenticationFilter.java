package chc.tfm.udt.auth.filter;

import chc.tfm.udt.auth.service.JWTService;
import chc.tfm.udt.auth.service.JWTServiceImpl;
import chc.tfm.udt.entidades.UsuarioEntity;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    private JWTService jwtService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,JWTService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
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
      * LLamamos a la clase Service para crear el TOKEN
     * Recogemos todos los datos para enviarlos en el body en 1 respeusta.
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = jwtService.create(authResult);

        response.addHeader(JWTServiceImpl.HEADER_STRING, JWTServiceImpl.TOKEN_PREFIX + token);
        Map<String,Object> body = new HashMap<String,Object>();
        body.put("token", token);
        body.put("user", (User) authResult.getPrincipal());
        body.put("mensaje",String.format("Hola %s, has inciado sesión con exito",((User) authResult.getPrincipal()).getUsername()));
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(200);
        response.setContentType("application/json");
    }

    /**
     * Cuando la autenticación no ha ido bien.
     * podemos enviar mensajes al usuario de forma básica , o redirigir a 1 pagina personalizada.
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        Map<String, Object> body = new HashMap<>();
        body.put("mensaje","Error de autenticacion : usuario o password incorrectos" );
        body.put("error", failed.getMessage());
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType("application/json");

    }
}
