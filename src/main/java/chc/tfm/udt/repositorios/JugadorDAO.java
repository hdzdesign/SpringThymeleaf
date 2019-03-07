package chc.tfm.udt.repositorios;

import chc.tfm.udt.entidades.JugadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JugadorDAO extends JpaRepository<JugadorEntity, Long> {
    /**
     * Metodo creado para buscar por nombre... se pueden crear metodos para buscar por cualquier atributo de la entidad.
     * @param nombre
     * @return
     */
    JugadorEntity findByNombre(String nombre);
}
