package chc.tfm.udt.Controller;

/**
 * import propios del proyecto
 */

import chc.tfm.udt.entidades.JugadorEntity;
import chc.tfm.udt.servicio.IJugadorService;
import chc.tfm.udt.servicio.IUploadFileService;
import chc.tfm.udt.utils.paginator.PageRender;
/**
 * import Springframework
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 * import de javax
 */
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
/**
 * imports propios de JAVA.
 */
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Map;

@SessionAttributes("jugadorEntity")
@Controller
public class JugadorController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private IJugadorService jugadorService;
    @Autowired
    private IUploadFileService uploadFileService;
    /**
     * Metodo que vamos a usar para recuperar el rcurso desde el servicio UploadFileService
     * para pasarlo a la vista en el body.
     * En el value , usamos una expresión regular para formatear la extensión de la imagén
     */
    @Secured ({"ROLE_USER","ROLE_ADMIN"})
    @GetMapping(value = "/uploads/{filename:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String filename){
        Resource recurso = null;
        try {
            recurso = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        /**ResponseEntity.ok y le añadimos un header para adjutar la imagen en la respuesta con attachment.
            llamamos a recurso para recuperar el filname con el metodo get que nos proporciona la interfaz.
            Añadimos al body el recurso.
            @CONTENT_DISPOSITION : indicamos que el contenido es un archivo adjunto.
         */
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" +  recurso.getFilename() +"\"")
                .body(recurso);
    }

    /**
     * Usaremos este metodo para cargar la vista del detalle del jugador
     * @param id El identificador del cliente
     * @param model Pasamos datos a la vista con el objeto MAP, clave Valor.
     * @param push REDIRECTATTributes para mensajes PUSH en la vista.
     * @PreAuthorize(hasAnyRole) ver documetación en spring Scurirty
     * @return
     */
   @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id , Map<String,Object> model, RedirectAttributes push){
        JugadorEntity jugadorEntity =  jugadorService.fetchByWithDonaciones(id);// jugadorService.findOne(id);
        if(jugadorEntity == null){
            push.addFlashAttribute("error", "El jugador no existe en la base de datos");
            return "redirect:/listar";
        }
        model.put("jugadorEntity",jugadorEntity);
        model.put("tituloDetalle","Detalle del jugador: " + jugadorEntity.getNombre());
        return "ver";
    }

    /**
     * USAMOS ESTE METODO PARA LISTAR TODOS LOS JUGADORES QUE ESTAN EN BASE DE DATOS.
     * @param model USAMOS ESTE OBJETO PARA PASAR DATOS A LA VISTA.
     * CREAMOS EL OBJETO PAGEABLE Y LE PASAMOS UN NUMERO DE PAGINAS PARA MOSTRAR, LE ASIGNAMOS UN VALOR POR DEFECTO Y UNA VARIABLE.
     * Creamos 1 array con 2 rutas , listar y la página de incio de la aplicación
     * @return
     */

    @RequestMapping(value = {"/listar", "/"}, method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
                         Authentication authentication,
                         HttpServletRequest request){

        if(authentication != null){
        logger.info("Hola Usuario autenticado, tu username es: ".concat(authentication.getName()));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null){
            logger.info("utilizando forma estatica SecurityContextHolder.getContext().getAuthentication(): usuario autenticado, username "
                    .concat(auth.getName()));
        }
        /**
         * FORMA 1º
         * Creamos 1 metodo hasRole que valida los roles
         * invocamos el metodo hasRole dentro de 1 if peusto que es 1 booolean y le pasamos por parametro el rol que deseamos.
         */
        if(hasRole("ROLE_ADMIN")){
            logger.info("Hola ".concat(auth.getName().concat(" tienes acceso")));
        }else{
            logger.info("Hola ".concat(auth.getName().concat(" No tienes acceso")));
        }

        /**
         * Esta clase de Spring nos va reducir la cantidad de código que nosotros tenemos que introducir puesto que esta definido
         * en esta interfaz, es necesario inyectar la interfaz HttpServletRequest
         * El metodo hasRole de esta misma clase es igual que lo que estamos llamando en la interfaz, por tanto esta forma de
         * validar los roles sería la mas escalable y la que menos código es necesario realizar.
         */
        SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
        if(securityContext.isUserInRole("ADMIN")){
            logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola ".concat(auth.getName().concat(" tienes acceso")));
        }else{
            logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola ".concat(auth.getName().concat(" No tienes acceso")));
        }
        /**
         * 4º FORMA
         * De forma nativa usando el HttpServletRequest vamos a chequear los roles , es importante añadir el prefijo ROL
         */
        if(request.isUserInRole("ROLE_ADMIN")){
            logger.info("Forma usando de forma nativa HttpServletRequest request : Hola ".concat(auth.getName().concat(" tienes acceso")));
        }else{
            logger.info("Forma usando de forma nativa HttpServletRequest request: Hola ".concat(auth.getName().concat(" No tienes acceso")));
        }

        Pageable pageRequest = PageRequest.of(page,5);
        Page<JugadorEntity> jugadorEntityPage = jugadorService.findAll(pageRequest);
        PageRender<JugadorEntity> pageRender = new PageRender<>("/listar", jugadorEntityPage);

        model.addAttribute("titulo","listado de jugadores");
        model.addAttribute("jugadores", jugadorEntityPage);
        model.addAttribute("page",pageRender);
        return "listar";
    }

    /**
     * Metodo que se encarga de conectar el formulario de la vista con el back.
     * @param model
     * @return
     */
    @Secured ("ROLE_ADMIN")
    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> model){
        JugadorEntity jugadorEntity = new JugadorEntity();
        model.put("jugadorEntity", jugadorEntity);
        model.put("titulo","Dar de alta");

        return "form";
    }

    /**
     * Metodo que utilizaremos para editar los jugadores, va mapeado al formulario de entrada.
     * le psamos un id con PathVariable.
     * @param id
     * @param model
     * @param push Objeto que usamos para mostrar mensajes de error o correcto al usuario por pantalla , al realizar 1 acción
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/form/{id}")
    public String editar (@PathVariable(value = "id") Long id, Map<String , Object> model,RedirectAttributes push){
        JugadorEntity jugadorEntity = null;
        //Comprobamos que el id es superior a 0, si no, no existe el jugador.
        if(id >0){
            jugadorEntity = jugadorService.findOne(id);
            // Si el jugador es null , no existe en base de datos.
            if(jugadorEntity == null){
                push.addFlashAttribute("error","El jugador no existe ");
                return "redirect:/listar";
            }
            // Si el id introducido es 0 se lo mostramos como error.
        }else{
            push.addFlashAttribute("error","El id no puede ser 0 ");
            return "redirect:/listar";
        }
        model.put("jugadorEntity", jugadorEntity);
        model.put("titulo","Editar Jugador");
        return "form";
    }
    /**
     * Metodo para procesar el submit, almacenandlo en base de datos.
     * Recibe el objeto jugador entity y lo guardamos con save.
     * @Valid habilita la validación en el objeto mapeado al form
     * MODEL: USAMOS ESTE OBJETO PARA PASAR DATOS A LA VISTA CON .ADDATRIBUTE
     * SessionStatus , lo utilizamos para controlar la session que hemos creado.
     * BINDINGRESULT usamos este objeto para comprobar que se cumplen los requisitos de los campos y mostrar 1 mensaje.
     * REDIRECTATRIBUTES : USAMOS ESTE OBJETO PARA MOSTRAR AL USUARIO UN MENSAJE DE SUCCESS O ERROR AL REALIZAR 1 ACCIÓN.
     * SESSIONSTATUS : USAMOS ESTE OBJETO PARA OBTENER UN STATUS DE LA SESIÓN Y PODER TRABAJAR CON EL ID DE FORMA SEGURA.
     * @RequestParam(FILE) MultipartFile, Inyectamos de forma automatica un archivo que estamos enviando al servidor.
     *  * ******************************************************************************************************************
     *      * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ CONTROL DEL METODO SERVICE COPY @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
     *      * 1º LLamamos al servicio uploadFileService.delete y le pasamos el nombre de la imágen.
     *      * 2º Movemos la imágen, Un string que contiene el nombre unico de la foto , generado con la interfaz UUID.
     */
    @Secured ("ROLE_ADMIN")
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid JugadorEntity jugadorEntity, BindingResult result,
                          Model model, RedirectAttributes push,
                          SessionStatus status, @RequestParam("file")MultipartFile foto){

        //Si el resultado contiene errores , retornamos al formulario
        if(result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Jugadores");
            return "form";
        }
        //Preguntamos que si no esta vacio el objeto foto.
        if(!foto.isEmpty()){
            //comprobamos que el jugador no viene vacio ni es null para proceder al borrado de la foto con el objeto File
            if(
                    jugadorEntity.getId() != null
                &&  jugadorEntity.getId() > 0
                &&  jugadorEntity.getFoto() != null
                &&  jugadorEntity.getFoto().length() > 0)
            {
                uploadFileService.delete(jugadorEntity.getFoto());
            }
            String nombreUnicoDeArchivo = null;
            try {
                nombreUnicoDeArchivo = uploadFileService.copy(foto);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Mostramos un mensaje al usuario.
            push.addFlashAttribute("info", "Ha sido subida correctamente," + nombreUnicoDeArchivo+"");
            //Pasamos la foto a la entity para que quede almacenada en base de datos.
            jugadorEntity.setFoto(nombreUnicoDeArchivo);
        }
        // CONDICIONAL QUE INDICA SI EL ID ES DISTINTO DE 0 SE ESTA EDITANDO Y SI ES 0 SE ESTA CREANDO PARA MOSTRAR EL MENSAJE
        String mensajePush = (jugadorEntity.getId() != null)? "Jugador Editado con exito" : "Jugador creado con exito";

        jugadorService.save(jugadorEntity);
        //CERRAMOS LA SESSIÓN UNA VEZ COMPLETADO EL PROCESO DE GUARDADO.
        status.setComplete();
        push.addFlashAttribute("success", mensajePush);
        return "redirect:listar";
    }

    /**
     * Metodo que utilizaremos para llamar al servicio de eliminar, recogiendo el id de la vista.
     * @param id
     * REDIRECTATRIBUTES : USAMOS ESTE OBJETO PARA MOSTRAR AL USUARIO UN MENSAJE POR PANTALLA DE SUCCESS O ERROR AL REALIZAR 1 ACCIÓN.
     * @return
     */
    @Secured ("ROLE_ADMIN")
    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes push){
        if(id > 0){
            JugadorEntity jugadorEntity = jugadorService.findOne(id);
            jugadorService.delete(id);
            push.addFlashAttribute("success","Jugador eliminado con exito");

            // Al eliminar el jugador debemos borrar su foto del servidor para evitar que queden archivos residuales.
            if(uploadFileService.delete(jugadorEntity.getFoto())){
                // borramos el archivo y mostramos un mensaje push al usuario.
                    push.addFlashAttribute("info","Foto " + jugadorEntity.getFoto()+ " Foto eliminada con exito ");
                }
            }
        return "redirect:/listar";
    }

    /**
     * Metodo para validar los roles.
     * El objeto SecurityContexgt nos va a servir para recuperar las autenticaciones del contexto de Spring
     * Creamos 1 objeto de tipo Authentication y con el objeto SecurityContext recuperamos todas las autenticaciones
     * Creammos validaciones por si alguno de los objetos viniera nulo.
     * Cremos 1 Collection , objeto de Java Util, creamos la colección de GrantedAuthority , que cualquier clase Role
     * que este en nuestra aplicación tiene que implementar esta interfaz.
     * con el signo Ternario, vamos a implementar 1 generico y decir que es 1 colección de cualquier tipo de objeto que implemte
     * esta interfaz o herede de ella, ahora recuperamos con auth una lista de todos los autorities
     *
     * Spring Automaticamente maneja los roles de la aplicación con la clase SimpleGratedAuthority --- implementa a GrantedAuthority
     * @param role
     * @return
     */
    private boolean hasRole(String role){
        SecurityContext context = SecurityContextHolder.getContext();
        if(context == null){
            return false;
        }
       Authentication auth =  context.getAuthentication();
        if(auth == null){
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        /**
         * 3º FORMA
         * Esta linea va a validar a través de la clase SimpleGrantedAuthority si la colección contiene un rol, esto
         * retornara un true o un false, no retornara el nombre del ROL como de la otra forma , recorriendo la colección
         */
        return authorities.contains(new SimpleGrantedAuthority(role));
/**
 * 2º FORMA
 * Con este for recorremos la lista collection y podemos saber cual es el nombre del ROL autenticado
 */
       /* for(GrantedAuthority authority : authorities){
            if(role.equals(authority.getAuthority())){
                logger.info("Hola ".concat(auth.getName().concat(" tu rol es: ".concat(authority.getAuthority()))));
                return true;
            }
        }
        return false;*/

    }
}

