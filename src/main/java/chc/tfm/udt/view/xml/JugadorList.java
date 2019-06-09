package chc.tfm.udt.view.xml;

import chc.tfm.udt.entidades.JugadorEntity;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * - Esta clase es una clase envoltorio que va a contener la lista de clientes para que se pueda convertir a XML con un constructor
 * vacio para que pueda manejarlo el JAvax, y un constructor Con jugadores, ademas del metodo GetJugadores
 * - Con las anotaciones XmlRootElement , le indicamos que esta es la clase envoltorio que va a usarse para exportar la lista.
 * - Con la anotación XmlElement, le indicamos que  el list es la raiz de la lista, y se le puede dar un nombre.
 */

@XmlRootElement(name = "jugadores")
public class JugadorList {

    @XmlElement(name = "jugador")
    public List<JugadorEntity> jugadores;

    public JugadorList(){}

    public JugadorList(List<JugadorEntity> jugadores) {
        this.jugadores = jugadores;
    }
    public List<JugadorEntity> getJugadores(){
        return jugadores;
    }
}
