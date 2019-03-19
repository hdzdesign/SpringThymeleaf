package chc.tfm.udt.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String login(@RequestParam(value = "error", required = false) String error,
            Model model, Principal principal, RedirectAttributes push){
        if(principal != null){
            push.addFlashAttribute("info", "Ya ha iniciado sesión anteriormente");
            return "redirect:/";
        }
        if(error != null){
            model.addAttribute("error", "Error en el login: nombre de usuario o contraseña incorrecta");
        }

        return "login";
    }
}
