package chc.tfm.udt.utils.paginator;

/**
 * Clase que usaremos para definir cual es el numero y si ees actual o no.
 */
public class PageItem {
    private int numero;
    private boolean actual;

    public PageItem(int numero, boolean actual) {
        this.numero = numero;
        this.actual = actual;
    }

    public int getNumero() {
        return numero;
    }

    public boolean isActual() {
        return actual;
    }
}
