package chc.tfm.udt.servicio;

import chc.tfm.udt.entidades.DonacionEntity;
import chc.tfm.udt.entidades.JugadorEntity;
import chc.tfm.udt.entidades.ProductoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IJugadorService {

    /**
     * Metodo para buscar todos los Jugadores
     * @return
     */
    List<JugadorEntity> findAll();

    /**
     * Metodo para recuperar toda la lista de paginas.
     * @param pageable
     * @return
     */

    Page<JugadorEntity> findAll(Pageable pageable);



    /**
     * Metodo que utilizaremos para almacenar 1 jugador en la base de datos
     * @param jugadorEntity
     */
    void save(JugadorEntity jugadorEntity);

    /**
     * Metodo que utilizaremos para buscar por un id
     * @param id
     * @return
     */
    JugadorEntity findOne(Long id);

    /**
     * Metodo para buscar por nombre a los jugadores
     * @param nombre
     * @return
     */

  //  JugadorEntity findByNombre(String nombre);

    /**
     * Metodo que utilizaremos para borrar por id haciendo uso del findOne
     * @param id
     */
    void delete (Long id);

    /**
     * Metodo que usaremos para retornar una lista de productos , buscados por nombre
     * @param term
     * @return
     */
     List<ProductoEntity> findByNombre(String term);

    /**
     * Metodo que nos va a permitir guardar las facturas en base de datos.
     * @param donacionEntity
     */
    void saveDonacion(DonacionEntity donacionEntity);

    /**
     * Metodo que usaremos para recuperar 1 producto de la base de datos
      * @param id
     * @return
     */
     ProductoEntity findProductoEntityById(Long id);

}
