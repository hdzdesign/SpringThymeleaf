package chc.tfm.udt.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class LoginController {
    /**
     * Con el objeto Principal vamos a validar de que el usuario no esté logeado en la página.
     * Cada vez que el usuario , ejecute el metodo post del formulario va a venir a este metodo del controlador
     * @param model
     * @param principal
     * @param push
     * @return
     */
    @GetMapping("/login")
    public String login(Model model, Principal principal, RedirectAttributes push){
        if(principal != null){
            push.addFlashAttribute("info", "Ya ha iniciado sesión anteriormente");
            return "redirect:/";
        }

        return "login";
    }
}
