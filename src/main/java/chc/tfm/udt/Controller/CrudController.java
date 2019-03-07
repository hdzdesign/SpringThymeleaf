package chc.tfm.udt.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.util.List;

public interface CrudController<H> {
    /**
     * METODO QUE DEVUELTE 1 LISTA DE TODOS LOS OBJETOS
     */
    ResponseEntity<List> recuperarTodos(Model model);
    /**
     * METODO PARA CREAR 1 OBJETO EN BASE DE DATOS PASANDOLE 1 PARAMETRO
     * @return
     */
    ResponseEntity<H> crearUno(H h);
    /**
     * METODO PARA RECUPERAR 1 OBJETO EN BASE DE DATOS PASANDOLE EL ID
     * @return
     */
    ResponseEntity<H> recuperarUno(Integer id);
    /**
     * METODO PARA ACTUALIZAR 1 OBJETO EN BASE DE DATOS PASANDOLE EL ID
     * @return
     */
    ResponseEntity<H> actualizarUno(Integer id);
    /**
     * METODO PARA BORRAR 1 OBJETO EN BASE DE DATOS PASANDOLE EL ID
     * @return
     */
    ResponseEntity<H> borrarUno(Integer id);



}
