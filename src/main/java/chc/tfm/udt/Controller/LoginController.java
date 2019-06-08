package chc.tfm.udt.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.Locale;

@Controller
public class LoginController {
   private final Logger log = LoggerFactory.getLogger(getClass());
    /**
     * Con el objeto Principal vamos a validar de que el usuario no esté logeado en la página.
     * Cada vez que el usuario , ejecute el metodo post del formulario va a venir a este metodo del controlador
     * @param model
     * @param principal
     * @param push
     * @return
     */
    //private MessageSource messageSource;
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model, Principal principal, RedirectAttributes push, Locale locale){
        log.info("Entramos en login");
        if(principal != null){
            log.info("En principal");
            push.addFlashAttribute("info", "Ya ha iniciado sesión anteriormente Sr. "+principal.getName());
            return "redirect:/";
        }
        if(error != null){
            log.info("Entramos error");
            model.addAttribute("error",
                                "Error en el login: nombre de usuario o contraseña incorrecta");
        }
        if(logout != null){
            log.info("Entramos logout");
            model.addAttribute("success", "Ha cerrado sesión con exito");
        }

        return "login";
    }
}
