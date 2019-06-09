package chc.tfm.udt.view.xml;

import chc.tfm.udt.entidades.JugadorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.xml.MarshallingView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Esta clase es la que se va a encargar en recuperar la vista para poder trabajar con ella y  operar junto con la
 * clase envoltorio , haciendo uso de la configuración que hemos generado en el MvcConfig de la aplicación
 * haciendo uso del Bean generado en el contexto de Spring.
 */
@Component("listar.xml")
public class JugadorListXmlView extends MarshallingView {
    /**
     * Implementamos el Constructor de la Suoper clase , y hacemos una pequeña modificación , Ponemos la implementación
     * de la interfaz definida en la clase Configuración. Hay que inyectar con Autowired
     * @param marshaller
     */
    @Autowired
    public JugadorListXmlView(Jaxb2Marshaller marshaller) {
        super(marshaller);
    }

    /**
     * Antes de mapear el objeto model , debemos dejarlo limpio de aquello que trae de la vista.
     * Para eso utilizamos el remove. Antes de eliminar los jugadores, necesitamos traerlos con el get.
     *
     * @param model
     * @param request
     * @param response
     * @throws Exception
     */

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        model.remove("titulo");
        model.remove("page");
        Page<JugadorEntity> jugadores = (Page<JugadorEntity>)model.get("jugadores");

        model.remove("jugadores");
        /**
         * Para poder hacer la conversión es necesario que la lista que queremos pasar este declarada en la clase
         * envoltorio que anteriormente hemos creado JugadorList.
         * Los pasos a seguir.
         * 1º Recoger el objeto Model , que viene del controlador
         * 2º Eliminar lo que viene cargado en el , y no vamos a usar en esta conversión
         * 3º Recuperar toda la lista que viene de la vista Listar, viene con paginación , una vez recuperada..
         * 4º La eliminamos así queda únicamente la lista que esta  en la clase envoltorio.
         * 5º Hacemos un put, para enchufarle la lsita que esta en la clase envoltorio
         * 6º Le pasamso a la clase Padre através del metodo super , el model cargado con la lista, request y response
         */
        model.put("jugadorList",new JugadorList(jugadores.getContent()));



        super.renderMergedOutputModel(model, request, response);
    }
}
