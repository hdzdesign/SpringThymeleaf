package chc.tfm.udt.repositorios;

import chc.tfm.udt.entidades.JugadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JugadorDAO extends JpaRepository<JugadorEntity, Long> {
    /**
     * Metodo creado para buscar por nombre... se pueden crear metodos para buscar por cualquier atributo de la entidad.
     * @param nombre
     * @return
     */
    JugadorEntity findByNombre(String nombre);

    /**
     * Select dentro del objeto JugadorEntity join Fetch donaciones cuando el id = ? parametro es el primero.
     * @param id
     * @return
     */
    @Query("select j from JugadorEntity  j left join fetch j.donaciones d where j.id=?1")
    JugadorEntity fetchByIdWithDonaciones(Long id);
}
