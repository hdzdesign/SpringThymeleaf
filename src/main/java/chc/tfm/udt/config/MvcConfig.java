package chc.tfm.udt.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * Clase que implimenta a WebMvcConfigurer , para poder complementar y modificar la configuración
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

      //  private final Logger log = LoggerFactory.getLogger(getClass());
    /**
     * Metodo que lo utilizaremos para manejar los recursos que estan fuera del proyecto.
     * Agregamos la ruta donde estarán los recursos externos , y le vamos a indicar a spring donde está y que lo considere
     * como un directorio del proyecto , fuera del JAR.
     * @param registry
     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        //Creamos la ruta absoluta , lo convertimos a una Uri agrega el esquema File y despues lo hacmoes string.
//        String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
//        log.info(resourcePath);
//        registry.addResourceHandler("/uploads/**")
//                .addResourceLocations(resourcePath);
//
//    }

    /**
     * Este metodo vamos a utilizarlo para generar 1 controlador de vistas , sin necesidad de generar 1 clase,
     *  Este metodo recive 1 objeto ViewControllerRegistri , que solo carga la vista pero no tiene lógica del controlador.
     *  con el setViewName , pasamos el nombre de la vista que vamos a enviar
     * @param registry
     */
    public void  addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/error_403").setViewName("error_403");

    }

    /**
     * Metodo que vamos a utilizar para codificar el password , utilizaremos BCryp, es uno de lo mas potentes para
     * spring securiry.
     * Anotamos con @Bean para que podamos inyectar este método desde la aplicación como un componente de Spring.
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder (){
        return  new BCryptPasswordEncoder();
    }

}
