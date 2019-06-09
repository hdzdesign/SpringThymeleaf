package chc.tfm.udt.view.csv;

import chc.tfm.udt.entidades.JugadorEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *  - Anotamos la clase como un Bean de Spring y le asignamos el nombre de la vista a la que va hacer referencia,
 *      como solo tenemos 1 vista que es listar no hace falta agregar la extensión.
 *  - En esta ocasión no tenemos una clase especifica de Spring que nos proporcione los metodos necesarios para recupera
 *      la información, de la vista y trabajar con ello.
 *  - Extendemos de una clase mas Generica que si nos proporciona Spring y tenemos que ir adapatando los metodos a nuestras
 *      necesidades
 */
@Component("listar.csv")
public class JugadorCsvView extends AbstractView {
    /**
     * Geeneramos 1 contructor, en el que vamos a definir el contentType.
     */
    public JugadorCsvView() {
        setContentType("text/csv");
    }

    /**
     * Este metodo retorna un boolean  , genera 1 contenido descargable , TRUE
     * @return
     */
    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    /**
     * - En este metodo vamos a configurar la respuesta , para asignar 1 nombre a nuestro archovo, cambiamos el header.
     * - Recuperamos el ContentType.
     * @param model Recuperamos los datos de la vista con este objeto, en este caso estamos recuperando 1 lista, de tipo
     *              PAGE , tenemos que castear para que devuelva correctamente.
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {

        response.setHeader("Content-Disposition", "attachment; filename=\"jugadores.csv\"");
        response.setContentType(getContentType());

        Page<JugadorEntity> jugadores = (Page<JugadorEntity>)model.get("jugadores");
/**
 * Si visitamos la documentación http://super-csv.github.io/super-csv/examples_writing.html , vemos que existe 1 clase
 * que nos permite exportar a archivo ya con una serie de caracteristicas,  en el ejemplo nos dice como exportarlo
 * directamente a 1 archivo , pero nosotros vamso a utilizar la respuesta del servidor para incluirlo en el archivo
 *
 * Toma 1 beans una clase Entity con geters y setes, y luego lo convierte en 1 linea en un archivo plano.
 */
        ICsvBeanWriter beanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] header = {"id","nombre", "apellido1","mail","nacionalidad","dorsal"};
        beanWriter.writeHeader(header);

        for (JugadorEntity jugador : jugadores){
            beanWriter.write(jugador,header);
        }
        beanWriter.close();

    }
}
