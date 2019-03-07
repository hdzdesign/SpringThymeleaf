package chc.tfm.udt.repositorios;

import chc.tfm.udt.entidades.DonacionEntity;
import org.springframework.data.repository.CrudRepository;

public interface IDonacionDAO extends CrudRepository<DonacionEntity, Long> {
}
