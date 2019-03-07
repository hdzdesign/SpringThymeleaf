package chc.tfm.udt.servicio;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Interfaz que vamos a usar para hacer el servicio al controlador de la subida de las fotos al directorio.
 */
public interface IUploadFileService{

    /**
     * guardar la respuesta del responseEntity , como argumento pasamos el normbre del archivo.
     */
    Resource load (String filename) throws MalformedURLException;
    /**
     * Sería el nombre cambiado de la imagén , el nombre único de la imagen para obtenelo en el controladotr
     */
    String copy(MultipartFile file) throws IOException;

    /**
     * con el boolean sabemos si lo elimino o no lo elimino para saber si debemos mandar el mensaje al front con el push.
     */
    Boolean delete(String filename);
    /**
     * Metodo que va a borrar el directorio de forma recursiva, estos metodos los usaremos en desarrollo.
     */
    void deleteAll();

    /**
     * Metodo que va a crear el directorio donde vamos alojar los archivos, estos metodos los usaremos en desarrollo..
     */

    void init() throws IOException;


}
