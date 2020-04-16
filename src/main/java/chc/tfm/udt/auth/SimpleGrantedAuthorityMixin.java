package chc.tfm.udt.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Es una clase abstracta .
 * en esta clase lo único que vamos a realizar es la creación de 1 constructor
 * Lo anotaremos con la libreria Jackson , con la propiedad JsonCreator, que significa que sera la propiedad que lea
 * cuando se cree el JSON
 * Extraermos del Token los authority , puesto que vienen en 1 propiedad del JSON que genera el Token con la variable Authority
 * Y se lo asignamos al String Role , que pasaremos por esta clase Mixin al contructor de la clase SimpleGrantedAuthorities.
 */
public abstract class SimpleGrantedAuthorityMixin {
    @JsonCreator
    public SimpleGrantedAuthorityMixin(@JsonProperty("authority") String role) {
    }
}
