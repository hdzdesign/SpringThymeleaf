package chc.tfm.udt.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LocaleController {
    /**
     * Este metodo va controlar que no se pierda la cabezara de la url para añadirle 1 header que va hacer que
     * este controlador pueda concatenar la UltimaUrl.
     *
     * @param request
     * @return
     */
    @GetMapping("/locale")
    public String locale(HttpServletRequest request){
        String ultimaUrl = request.getHeader("referer");
        return "redirect:".concat(ultimaUrl);

    }
}
