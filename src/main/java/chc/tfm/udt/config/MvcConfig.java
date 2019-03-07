package chc.tfm.udt.config;


/*import org.slf4j.Logger;
*/import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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

}
