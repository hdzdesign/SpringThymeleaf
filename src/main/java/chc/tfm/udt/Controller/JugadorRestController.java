package chc.tfm.udt.Controller;

import chc.tfm.udt.entidades.JugadorEntity;
import chc.tfm.udt.servicio.IJugadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Esta clase controladora que tenemos anotada con RESTCONTROLLER, tiene heredado el responsBody y Controller,
 * dotando de capacidad para contestar en JSON.
 */
@RestController
@RequestMapping("/api/jugadores")
public class JugadorRestController {

    @Autowired
    private IJugadorService jugadorService;
    /**
     * Mi primer metodo REST. con el responseBody  indicamos que la respeusta sea en el cuerpo , y convierte una respuesta
     * en XML o JSON.
     */
    @GetMapping(value = "listar")
    public List<JugadorEntity> listar() {
        return jugadorService.findAll();
    }
}
