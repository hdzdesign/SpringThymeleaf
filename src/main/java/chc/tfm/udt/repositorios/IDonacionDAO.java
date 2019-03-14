package chc.tfm.udt.repositorios;

import chc.tfm.udt.entidades.DonacionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IDonacionDAO extends CrudRepository<DonacionEntity, Long> {
    /**
     * Seleccionamos del objeto Donacion join fetch, al jugador, dentro del objeto donación seleccionamos los items
     * y a través de los items seleccionamos las lineas del producto y la condición es el id =? 1 el primer parametro.
     * @param id
     * @return
     */
    @Query("select f from DonacionEntity f " +
            "join fetch f.jugadorEntity j " +
            "join fetch f.items l  " +
            "join fetch l.productoEntity " +
            "where f.id=?1")
    public DonacionEntity fechByIdWithJugadorWithItemDonacionWithProducto(Long id);
}
