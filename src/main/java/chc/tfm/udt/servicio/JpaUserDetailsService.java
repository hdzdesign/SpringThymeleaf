package chc.tfm.udt.servicio;

import chc.tfm.udt.entidades.RoleEntity;
import chc.tfm.udt.entidades.UsuarioEntity;
import chc.tfm.udt.repositorios.IUsuarioDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase no necesita 1 interfaz propia , puesto que vamos a implementar a UserDetailsService
 * Esta intefaz la provee spring security para realziar el proceso de autenticación
 * Nos obliga a implementar 1 metodo
 */
@Service
public class JpaUserDetailsService implements UserDetailsService {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IUsuarioDAO usuarioDAO;
    /**
     * Cargar el usuario a través de su username.
     * 1º debemos recuperar el username de la base de datos, eso lo hacemos con la clase Repository
     * 2º Creamos 1 lista de tipo GrantedAuthority , y la inicializamos+
     * 3º Recorremos esa lista de Roles de ese usuario almacenados en la lista creada en UsuarioEntity.
     * 4º Retornamos un User de Spring Security.
     *    Le pasamos por parametro el username, passwosrd y si esta habilitado
     * 5º Anotamos con Transactional el metodo , con caracteristica de readOnly
     * 6º Realizamos las validaciones , y arrojamos excepciones para poder tratarlas en la vista, y mostrar mensajes
     *    al usuario.
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioEntity usuarioEntity = usuarioDAO.findByUsername(username);
        if(usuarioEntity == null){
            log.error("error Login : No Existe el usuario ' "+username+ " '");
            throw new UsernameNotFoundException("Ususername: " + username+ " No existe en el sistema!");
        }

        List<GrantedAuthority > authorities = new ArrayList<GrantedAuthority>();

        for(RoleEntity role : usuarioEntity.getRoles()){
            log.info("ROLE: ".concat(role.getAuthority()));
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }
        if(authorities.isEmpty()){
            log.error("error Login : usuario: ' "+username+ " ' No tiene roles asignados");
            throw new UsernameNotFoundException("Username: " + username + " No tiene roles asignados.");
        }

        return new User(usuarioEntity.getUsername()
                        ,usuarioEntity.getPassword()
                        ,usuarioEntity.getEnabled()
                        , true
                        , true
                        , true
                        , authorities);
    }
}
