package chc.tfm.udt.servicio;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final static String UPLOADS_FOLDER = "uploads";


    @Override
    /**
     * METODO VER FOTO
     * Metodo que usaremos para cargar el Resource.
     * Lanza 1 excepticón al controlador.
     * SI existe una excepción indicando que ha habido 1 error al cargar la image + el nombre de la foto.
     * USAMOS EL GETPATH PARA RECUPERAR LA URL Y GENERAR 1 RECURSO
     * @Return retornamos el recurso al controlador : que contiene pathFoto con la ruta completa  del archivo.
     */
    public Resource load (String filename) throws MalformedURLException {
        Path pathFoto = getPath(filename);
        log.info("pathFoto " + pathFoto );
        Resource recurso = null;
        recurso = new UrlResource(pathFoto.toUri());

            //Creamos un condicional para controlar las excepciones
            if(!recurso.exists() || !recurso.isReadable()){
                //Si no se puede leer o no existe lanzamos la excepión con un mensaje y el nombre de la imagén.
                throw new RuntimeException("Error no se puede cargar la imagen " + pathFoto.toString());
            }
        return recurso;
    }
    /**
     * METODO GUARDAR.
     * Metodo que utilizaremos para:
     * 1º copiar la imagen original
     * 2º Renombrala con UUID.random para asignarle un nombre único.
     * MultipartFile, Inyectamos de forma automatica un archivo que estamos enviando al servidor.
     * Files usamos esta interfaz para copiar el nombre del archivo con 1 inputStream que viene de la vista y le pasamos la ruta absoluta.
     */
    @Override
    public String copy(MultipartFile file) throws IOException {
        /*  Almacenamos en esta variable la traducción de UUID ( "universally unique identifier" ).random para que no
        se repitan los nombres en nuestro servidor , asi evitar que se sobrescriban.*/
        String nombreUnicoDeArchivo = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        //Recuperamos la ruta absoluta con getPath y le asignamos el nombre unico del archivo.
        Path rootPath = getPath(nombreUnicoDeArchivo);
        log.info("rootPath " + rootPath);

        //Con el metodo copy lo que hacemos es copiar la ruta a través del inputStream de entrada y la ruta absoluta.
        Files.copy(file.getInputStream(), rootPath);
        return nombreUnicoDeArchivo;
            /*
    '''''''''''''''''''''''''''''''''''''''''OTRA FORMA DE HACER LAS COSAS '''''''''''''''''''''''''''''''''''''''''''''''''''''

    * CON ESTE METODO LO PASAMOS A BYTES Y GENERAMOS LA URI
    |       recuperamos los bytes de la foto para ajustarlo al limite de 10mb que hemos marcado en propiedades                   |
    |       byte[] bytes = foto.getBytes();                                                                                      |
    |       recuperamos la URI completa de la foto.                                                                              |
    |       Path rutaCompleta = Paths.get(rootPath + "//" + foto.getOriginalFilename());                                         |
    |       Escribimos la ruta completa y los bytes en el directorio UPLOAD                                                      |
    |       Files.write(rutaCompleta,bytes);                                                                                     |
    |       Escribimos la ruta completa y los bytes en el directorio UPLOAD.                                                     |
    |        Usamos el Files.copy, para obtener el inputStream para poder copiarlo al nuevo directorio, también se               |
    |         puede hacer con el write                                                                                           |
     ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''*/
     /* Tenemos que indicar los valores de la ruta.
    ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    @    /*Objeto que nos permite recuperar la ruta donde van a estar los archivos para usarlo desde el propio proyecto         @
    @        // Path directorioRecursos = Paths.get("src//main//resources//static/uploads");                                    @
    @    Creamos un string para poder trabajar con las imagenes en este directorio                                              @
    @       // String rootPath = directorioRecursos.toFile().getAbsolutePath();                                                 @
    @    Creamos un STRING con una ruta externa al proyecto otra forma de hacerlo mejor                                         @
    @       // String rootPath = "D://worktfm//uploads";                                                                        @
    ''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''*/
    }

    /**
     * Eeste metodo lo vamos a utilizar para:
     * 1º Recuperar la ruta absuluta
     * 2º Con la Interfaz File guardamos la ruta en 1 archivo.
     * 3º creamos el condicional para verificar que existe y que se puede leer, si es así lo borramos y retornamos al usuario un true para mostar el mensaje push.
     */
    @Override
    public Boolean delete(String filename) {
        // Al eliminar el jugador debemos borrar su foto del servidor para evitar que queden archivos residuales.
        Path rootPath = getPath(filename); // recuperamos la ruta absoluta
        File archivo = rootPath.toFile();
        if(archivo.exists() && archivo.canRead()){
            if(archivo.delete()){ // borramos el archivo y mostramos un mensaje push al usuario.
               return true;
            }
        }
        return false;
    }

    /**
     * Este metodo se va a encargar de eliminar el directorio donde estan almacenados los recursos.lo usarmos para desarrollo
     */
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());

    }

    @Override
    public void init() throws IOException {
        Files.createDirectory(Paths.get(UPLOADS_FOLDER));

    }
    /**
     * Metodo que usamos para  generar el path absoluto del archivo
     * 1º Recuperamos el direcitorio donde esta guardado
     * 2º con resolve concatenamos el string del filname
     * 3º creamos la ruta absoluta con .toAbsolutePath
     */
    public Path getPath(String filename){

        return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
    }


}
