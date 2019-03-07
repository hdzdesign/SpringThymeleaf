package chc.tfm.udt.servicio;
import chc.tfm.udt.entidades.DonacionEntity;
import chc.tfm.udt.entidades.JugadorEntity;
import chc.tfm.udt.entidades.ProductoEntity;
import chc.tfm.udt.repositorios.IDonacionDAO;
import chc.tfm.udt.repositorios.IProductoDAO;
import chc.tfm.udt.repositorios.JugadorDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Clase que usaremos para seguir el patron de diseño "Business Service Facade"
 */
@Service
public class JugadorServiceImpl implements IJugadorService {

    private static final Log logger = LogFactory.getLog(JugadorServiceImpl.class);


    @Autowired
    private JugadorDAO jugadorDAO;
    @Autowired
    private IProductoDAO productoDAO;
    @Autowired
    private IDonacionDAO donacionDAO;

    @Override
    @Transactional(readOnly = true)
    public List<JugadorEntity> findAll() {
        return (List<JugadorEntity>)jugadorDAO.findAll();
    }

    @Override
    public Page<JugadorEntity> findAll(Pageable pageable) {
        return jugadorDAO.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public JugadorEntity findOne(Long id) {
        return jugadorDAO.findById(id).orElse(null);
    }


    @Override
    @Transactional
    public void save(JugadorEntity jugadorEntity) {
        jugadorDAO.save(jugadorEntity);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        jugadorDAO.deleteById(id);

    }
    @Transactional(readOnly = true)
    @Override
    public List<ProductoEntity> findByNombre(String term) {
        return productoDAO.findByNombreLikeIgnoreCase("%"+term+"%");
    }
    @Transactional
    @Override
    public void saveDonacion(DonacionEntity donacionEntity) {
        donacionDAO.save(donacionEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductoEntity findProductoEntityById(Long id) {
        return productoDAO.findById(id).orElse(null);
    }
}
