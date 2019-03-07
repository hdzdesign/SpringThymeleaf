package chc.tfm.udt.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;

/**
 * Esta clase nos va a servir para definir las lineas que va a contener la donación , por el momento solo el id
 * y la cantidad donada al jugador.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "donaciones_items")
public class ItemDonacionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;
    /**
     * ManyToOne: Muchos items en una sola equipación.
     * JoinColumn: Indicamos el nombre de la llave foranea de la tabla donaciones_items, aunque se crearía de forma automatica
     * si no lo especificamos
     * ItemDonaciónEntity , tiene una relación unidireccional con EquipaciónEntity, por eso unicamente se especifica aqui la relación.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private ProductoEntity productoEntity;

  /*  @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viajes_id")
    private ViajesEntity viajesEntity;*/


    /**
     * Metodo que usaremos para indicar el valor de la equipación al jugador incorporado.
     *
     * @return
     */
    public Double calcularValor() {
        return cantidad.doubleValue() * productoEntity.getPrecio();


    }
}
