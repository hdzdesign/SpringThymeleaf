package chc.tfm.udt.repositorios;

import chc.tfm.udt.entidades.UsuarioEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsuarioDAO extends CrudRepository<UsuarioEntity, Long> {
    /**
     * A través del nombre del metodo findByUsername Spring realiza 1 consulta JPQL
     * select u from UsuarioEntity u Where u.username=?1
     * @param username
     * @return
     */
    UsuarioEntity findByUsername(String username);

}
