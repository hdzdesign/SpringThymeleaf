package chc.tfm.udt.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.nio.file.Paths;
import java.util.Locale;

/**
 * Clase que implimenta a WebMvcConfigurer , para poder complementar y modificar la configuración
 * Al anotar esta clase con @Configuration , cuando arranca el contexto de Spring lee esta clase y carga en el contexto
 * aquellos metodos que esten anotados con @Bean para poder ser inyectados en el resto de la aplicación.
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
     * spring securiry config
     * Anotamos con @Bean para que podamos inyectar este método desde la aplicación como un componente de Spring.
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder (){
        return  new BCryptPasswordEncoder();
    }

    /**
     * Metodo que vamos a usar para definir nuestro idioma por defecto que se va a guardar en la sessión Http.
     */
    @Bean
    public LocaleResolver localeResolver (){
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("es", "ES"));
        return localeResolver;
    }
    /**
     * Este metodo va a ser el interceptor que se va encargar de cambiar los textos, cada vez que pasemos,
     * por url el parametro lang se va a realizar el cambio porque detecta el interceptoraa
     *
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor(){
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    /**
     * Con este metodo lo que vamos hacer es registrar nuestro inteceptor haciendo Override del metodo
     * InterceptorRegistry.
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    /**
     * Declaramos el constructor y llamamos al metodo:
     * SetClassesToBeBound Con este metodo vamos a conseguir declarar un Array de clases que queremos pasar a XML
     * Los nombres de las clases , las vamos a serializar , Retornamos el marshaller,
     * Con esta configuración , vamos a utilizar en la vista XML para convertir la respuesta , el bojeto Entity en un
     * documento XML , siendo este metodo el conversor.
     * JSON se puede serializar sin problema List, HasList, de objetos, que luego se convierten en un JSON
     * XML no puede convertir un objeto List , collection , por eso esta clase Wapper que envuelva la lista
     *
     * - chc.tfm.udt.view.xml.JugadorList.class, ahora ya esta configurado completamente, cuando exportemos, va a recoger
     * la lista de la clase envoltorio y la va a mapear en el documento XML
     * @return
     */
    @Bean
    public Jaxb2Marshaller jaxb2Marshaller(){
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(new Class[] {chc.tfm.udt.view.xml.JugadorList.class});
        return marshaller;
    }
}
