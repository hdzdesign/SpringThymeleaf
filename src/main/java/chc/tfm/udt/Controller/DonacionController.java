package chc.tfm.udt.Controller;

import chc.tfm.udt.entidades.DonacionEntity;
import chc.tfm.udt.entidades.ItemDonacionEntity;
import chc.tfm.udt.entidades.JugadorEntity;
import chc.tfm.udt.entidades.ProductoEntity;
import chc.tfm.udt.servicio.IJugadorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Map;
/**
 * Clase controladora de las donaciones destinadas a los jugadores
 *
 */
@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/donacion")
@SessionAttributes("donacion")
public class DonacionController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private IJugadorService jugadorService;
    @Autowired
    private MessageSource messageSource;
    @GetMapping("/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id,
                      Model model,
                      RedirectAttributes push, Locale locale){
        log.info("LLega al controller VER");
        DonacionEntity donacion = jugadorService.fechDonacionByIdWithJugadorWithItemDonacionWithProducto(id);//jugadorService.findDonacionById(id);
       if(donacion == null){
           push.addFlashAttribute("error", messageSource.getMessage("text.flash.error.ver", null ,locale ));
           return "redirect/listar";
       }
       model.addAttribute("donacion", donacion);
       model.addAttribute("titulo", messageSource.getMessage("text.detalle.factura.cabecera", null, locale).concat(donacion.getDescripcion()));
       return "donacion/ver";
    }

    /**
     * Con este metodo vamos a comunicarnos con el formulario que va hacer posible la inserción en base de datos de los
     * distintos productos que van a estar asociados a un jugador.
     * @param jugadorEntityId La id del jugador al que va a estar asociada la donación por parte del club.
     * @param model Objeto de tipo MAP , para pasar a la vista los distintos objetos de la aplicación
     * @param push Objeto que utilizaremos para mostar al usuario mensajes de información
     * @return
     */
    @GetMapping("/form/{jugadorEntityId}")
    public String crear(@PathVariable(  value = "jugadorEntityId")
                                        Long jugadorEntityId,
                                        Map<String, Object> model,
                                        RedirectAttributes push, Locale locale){

        JugadorEntity jugadorEntity = jugadorService.findOne(jugadorEntityId);

        if(jugadorEntity == null){
            push.addFlashAttribute("error", messageSource.getMessage("text.flash.error.crear",null ,locale ));
            return "redirect:/listar";
        }
        DonacionEntity donacion = new DonacionEntity();
        donacion.setJugadorEntity(jugadorEntity);

        model.put("donacion", donacion);
        model.put("titulo",messageSource.getMessage("text.crear.factura.cabecera", null,  locale));

        return "donacion/form";
    }
    // tiene un pathVariable , que sería el texto , {term}
    // produces , salida del tipo json
    // @ResponseBody Suprime el cargar una vista de thymeleaft , en vez de eso , toma el return converido a json y lo va a poner en el body de la respuesta.

    /**
     * Metodo que utilizaremos para controlar la devolución de la base de datos de los productos asociados a un jugador.
     * @GetMapping : Mapeamos la misma url que definimos en la llamada AJAX al servidor, pasando como parametro el texto
     * @ResponseBody: Con esta anotación elminamos la carga de la vista con http://www.thymeleaf.org, en vez de eso,
     *                Retornamos convertido a Json para mostrarlo en el body de la respuesta.
     * @PathVariable: La variable que manejamos es el texto que se introduce en el imput de busqueda.
     * @param term
     * @return
     */
    @GetMapping (value = "/cargar-productos/{term}", produces = { "application/json" })
    public @ResponseBody List<ProductoEntity> cargarProducto(@PathVariable String term){
        return jugadorService.findByNombre(term);
    }


    @PostMapping("/form")
    public String guardar( @ModelAttribute("donacion") @Valid DonacionEntity donacion, BindingResult result, Model model,
                          @RequestParam(name = "item_id[]",required = false) Long[] itemId,
                          @RequestParam(name = "cantidad[]",required = false) Integer[] cantidad,
                          RedirectAttributes push, SessionStatus status, Locale locale){
        log.info("Estamos en Guardar");

        if(result.hasErrors()){
            log.info("Entramos en el If para comprobar la descripción");
            model.addAttribute("titulo", "Crear Factura");
            return "donacion/form";
        }
        if(itemId == null || itemId.length ==0){
            log.info("Entramos en el If para comprobar las lineas");
            model.addAttribute("titulo", "Crear Factura");
            model.addAttribute("error", messageSource.getMessage("text.flash.error.lineas",null , locale)); // "Las lineas de la factura no pueden estar vacias"
            return "donacion/form";
        }
        for (int i = 0; i < itemId.length; i++ ){
            ProductoEntity productoEntity = jugadorService.findProductoEntityById(itemId[i]);

            ItemDonacionEntity linea = new ItemDonacionEntity();
            linea.setCantidad(cantidad[i]);
            linea.setProductoEntity(productoEntity);
            donacion.addItemDonacion(linea);

            log.info("ID " + itemId[i].toString() + ", cantidad " + cantidad[i].toString());
        }
        jugadorService.saveDonacion(donacion);
        status.setComplete();
        push.addFlashAttribute("success", messageSource.getMessage("text.flash.exito", null, locale));
        return "redirect:/ver/" + donacion.getJugadorEntity().getId();
    }
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes push, Locale locale){
        DonacionEntity donacion = jugadorService.findDonacionById(id);
        if(donacion != null){
            jugadorService.deleteDonacion(id);
            push.addFlashAttribute("success", messageSource.getMessage("text.flash.eliminar", null, locale));
            return "redirect:/ver/" + donacion.getJugadorEntity().getId();
        }
        push.addFlashAttribute("error", messageSource.getMessage("text.flash.error.eliminar", null, locale));
        return "redirect:/listar";
    }


}