package chc.tfm.udt.utils.propiedades;

import org.springframework.beans.factory.annotation.Value;

public class VistasPropiedades {
    @Value("${application.propiedades.mensaje}")
    private String mensaje;
}
