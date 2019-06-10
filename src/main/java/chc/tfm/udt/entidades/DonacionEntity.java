package chc.tfm.udt.entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase que va a Persistir en base de datos los datos básicos de la adquisición de 1 equipación de futbol que el equipo
 * dona al jugador con su incorporación al equipo , automaticamente cuando un jugador entra en el equipo se le asigna 1 equipación
 * por tanto estará implementado en el registro del jugador.
 */
@Data
@Entity
@Table(name = "donaciones")
public class DonacionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String descripcion;
    private String observacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "create_at")
    private Date createAt;
    /**
     * Muchas Donaciones un solo jugador.
     * Existe una relación bidireccional.
     */
    @ManyToOne(fetch = FetchType.LAZY) // Solo se realiza la consulta cuando se invoca al metodo
    @JoinColumn(name = "jugador_id")
    @JsonBackReference
    private JugadorEntity jugadorEntity;
    /**
     * Una donación contiene muchos itemsDonaciónEntity.
     * JoinColum: Indicamos a la base de datos cual es la llave foranea de la relación ,
     * puesto que es una relación unidireccional.
     * Hay que crear el campo donacion_id en la tabla  en base datos no en el Entity.
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "donacion_id")
    private List<ItemDonacionEntity> items;


    //Inicializamos el list Items por el constructor
    public DonacionEntity() {
        this.items = new ArrayList<>();
    }

    //Metodo que usaremos para persistir la fecha justn en el momento de crear la claes
    @PrePersist
    public void prePersist() {
        createAt = new Date();
    }

    // MEtodo que vamos a utilizar para  añadir un solo item a la lista, al contrario que con el set que añadimos 1 lista.
    public void addItemDonacion(ItemDonacionEntity item) {
        this.items.add(item);

    }

    public Double getTotal() {
        Double total = 0.0;
        int size = items.size();
        for (int i = 0; i < size; i++) {
            total += items.get(i).calcularValor();
        }
        return total;
    }
    @XmlTransient
    public JugadorEntity getJugadorEntity() {
        return jugadorEntity;
    }
}
