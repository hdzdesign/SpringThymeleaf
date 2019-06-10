package chc.tfm.udt.view.json;

import chc.tfm.udt.entidades.JugadorEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Map;

/**
 * Esta clase es la encargada de recoger el objeto Model y filtrarlo para posteriormente mostrarlo.
 *
 */
@Component("listar.json")
public class JugadorListJsonView extends MappingJackson2JsonView {
    /**
     * En este metodo , vamos a filtrar lo que queremos mostrar , debemos recoger el modelo y eliminar aquellos datos
     * que no queremos que se muestren en el JSON , debemos limpiar el objeto Jugador, porque viene con configuraciones
     * del paginador.
     * Una vez limpio recogemos el objeto y unicamente mostramos el contenido del tal.
     * @param model
     * @return
     */
    @Override
    protected Object filterModel(Map<String, Object> model) {

        model.remove("titulo");
        model.remove("page");
        Page<JugadorEntity> jugadores = (Page<JugadorEntity>) model.get("jugadores");
        model.remove("jugadores");
        model.put("jugadores",jugadores.getContent());
        return super.filterModel(model);
    }
}
