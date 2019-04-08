package chc.tfm.udt.entidades;

import lombok.Data;

import javax.persistence.*;
@Data
@Entity
/**
 * uniqueConstrains , le estamos indicando a JPA que al crear la tabla ponca estos atributos como unicos.
 */
@Table(name = "authorities", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id","authority"})})
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String authority;
}
