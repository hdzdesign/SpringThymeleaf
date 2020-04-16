package chc.tfm.udt.config;

import chc.tfm.udt.auth.LoginSuccesHandler;
import chc.tfm.udt.auth.filter.JWTAuthenticationFilter;
import chc.tfm.udt.auth.filter.JWTAuthorizationFilter;
import chc.tfm.udt.auth.service.JWTService;
import chc.tfm.udt.servicio.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * Inyección de DataSource para JDBC y de BCryptPasswordEncoder, creado el singelton en la clase de configuración
     */

    /*@Autowired
    private DataSource dataSource;*/
    @Autowired
    private JpaUserDetailsService userDetailsService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JWTService jwtService;


    /**
     * Metodo que va a dar seguridad a nuestra aplicación, todas las paginas
     * Spring antes de ejecutar cualquier ruta , spring utiliza un interceptor para que nada se ejecute sin antes comprobar
     * los roles de acceso
     */
    @Autowired
    private LoginSuccesHandler succesHandler;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * Con el objeto HttpSecurity vamos a mapear las rutas , primero las que son permitidas por todos
         * Seguidamente vamos a ir otorgando permisos por roles a cada usuario.
         * con el metodo and, vamos a decir que en caso de no obtener un resultado positivo con la autenticación
         * pase automaticamente a la pagina de login
         */
        http.authorizeRequests()
                .antMatchers("/", "/css/**", "/js/**", "/img/**","/listar**","/locale","/login").permitAll()
                .anyRequest().authenticated()
               //Esta parte del código esta comentada por pertenecer al inicio de sesión basado en Sesiones
             /*  .and()
                    .formLogin()
                        .successHandler(succesHandler)// Inyección de la clase LoginSuccesHandler
                        .loginPage("/login")
                    .permitAll()
                .and()
                    .logout().permitAll()
                .and()
                    .exceptionHandling()
                        .accessDeniedPage("/error_403") // Excepción de acceso denegado*/
                .and()// Añadimos el filtro de JWT.
                    .addFilter(new JWTAuthenticationFilter(authenticationManager(),jwtService))
                    .addFilter(new JWTAuthorizationFilter(authenticationManager(),jwtService))
                    .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        /**
         * Vamos habilitar el sessiónmanager sin estado con el atributo STATELESS, con esta configuración si ejecutamos
         * no vamos a tener sessión , no hay manejo de sesiión , no va a fuincionar. porque no se mantiene persistente.
         */
    }

    /**
     * Metodo que nos permite insertar usuarios, otorgandole los privilegios
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder build) throws Exception{
        /**
         * Esta es la forma de implementar la seguridad en nuestros usuarios con JPA
         * Implementamos el metodo userDetailService , y le pasamos 1 implementación de nuestra clase JpaUserDetailService
         * Creada en nuestro Servicio, la inyectamos con Autowired
         */
        build.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        /**
         * Construimos nuestro acceso con jdbcAuthentication, al cual le vamos a pasar un dataSource y passwordEncoder
         * realizamos 2 Querys simples usando usersByUsernameQuery, y authoritiesByUsernameQuery
         * En AuthoritiesByUsernameQuery , necesitamos 1 consulta a varias tablas por eso usamos inner Join
         * dandole alias a cada tabla.
         */

      /*  build.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery("select username, password, enabled from users where username=?")
                .authoritiesByUsernameQuery(
                        "select u.username, a.authority from authorities a " +
                        "inner join users u on (a.user_id = u.id) where u.username=?");

*/


        /**
         * Una vez insertado JDBC esta parte va a quedar comentada , porque ya no vamos a usar inMemoryAuthentication()

         * Nos permite crear o generar los usuarios en memoria, con un econder de pasword por defecto.
         */
        /*
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        User.UserBuilder users = User.builder().passwordEncoder(encoder :: encode);
        */
        /**
         * De momento vamos a utilizar un sistema de autentificación en memoria, como un repositorio.
         */
        /*
        build.inMemoryAuthentication()
                .withUser(users.username("admin").password("1234").roles("ADMIN", "USER"))
                .withUser(users.username("carlos").password("1234").roles("USER"))
                .withUser("pepe").password("pepe").roles("ADMIN");*/
    }
}
