package chc.tfm.udt.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Anotamos esta clase con Component para poder inyectarla mas adelante en la configuración de Spring security
 */
@Component
public class LoginSuccesHandler extends SimpleUrlAuthenticationSuccessHandler {
    /**
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        /**
         * Vamos a crear 1 MAP que contenga los mensajes FLASH
         */
        SessionFlashMapManager flashMapManager = new SessionFlashMapManager();
        /**
         * Usamos esta forma porque nos epuede usar el RedirectAtributte que usamos en el controlador, esta clase,
         * con este mapa , vamos a poder pasar a la vista mensajes.
         * con el objeto Authenticatión que hereda de principal podemos sacar el nombre del usuario logado
         * hereda de HasMap.
         */
        FlashMap flashMap = new FlashMap();
        flashMap.put("success","Hola, " + authentication.getName()+ " Has inciado sesión con exito");
        /**
         * Al heredaar de una clase que tiene el objeto Logger podemos utilizarlo en esta clase,
         * El objeto authentication podemos comprobar si el usuario esta logeado , al heredar de Principal
         */
        if(authentication != null){
            logger.info("El usuario '" + authentication.getName() + "' ha iniciado sesión con exito");
        }
        /**
         * Con este metodo vamos a guardar el Map de salida , le pasamos el request y el respose, ademas del flashMap
         * Con el objeto SessionFlashMapManager podemos guardar los mensajes generados con el map , y pasarlos a la vista.
         */
        flashMapManager.saveOutputFlashMap(flashMap,request,response);


        super.onAuthenticationSuccess(request, response, authentication);
    }
}
