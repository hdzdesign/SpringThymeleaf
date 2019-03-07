package chc.tfm.udt.repositorios;

import chc.tfm.udt.entidades.ProductoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IProductoDAO extends CrudRepository<ProductoEntity, Long> {
    /**
     * Query a nivel de objeto no de tabla, ? hace referencia al parametro y el 1 al parametro que se le pasa.
     * Metodo que retorna una lista de productos , según el termino que le pasemos.
     * @param term
     * @return
     */
    @Query("select p from ProductoEntity p where p.nombre like %?1%")
    List<ProductoEntity> findByNombre(String term);

    /**
     * Metodo que va a buscar ignorando las mayusculas
     * @param term
     * @return
     */
    List<ProductoEntity> findByNombreLikeIgnoreCase(String term);
}
