
package chc.tfm.udt.utils.paginator;

import org.springframework.data.domain.Page;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase en la que vamos a configurar para que se encague de controlar elementos muy importantes para el paginador
 * Total de paginas
 * numero de elementos por pagina
 * pagina actual.
 * Tambien se encargará de controlar el numero total de páginas.
 *
 * @param <T>
 */
public class PageRender<T> {

    private String url;
    private Page<T> page;
    private List<PageItem> paginas; // Lista de paginas y le pasamos la clase donde definimos el numero de la pagina y si es la actual
    private int totalPaginas;
    private int numElementosPorPagina;
    private int paginaActual;

    /**
     * INIZIALIZAMOS  por contrustor las variable.
     * Con el objeto Page , recogemos datos de los elementos de la lista
     * page.getSize recogemos todos los elementos de la pagina y asignamos a numero de elementos por pagina.
     * page.getTotalPages: hace la llamada para recuperar el total de paginas que tenemos
     *  page.getNumbre + 1 : hace referencia a la pagina acutal que vamos a sumarle  1 para que siempre muestre 1 mas
     * @param url
     * @param page
     */
    public PageRender(String url, Page<T> page){
        this.url    = url;
        this.page   = page;
        this.paginas = new ArrayList<PageItem>();
        numElementosPorPagina = page.getSize();
        totalPaginas  = page.getTotalPages();
        paginaActual = page.getNumber() +1 ; // Default es 0 por eso se pone + 1
        int desde; //Desde donde empieza  el paginador
        int hasta; // Hasta donde acaba el paginador
        //Si el total de paginas es menor al numero de elementos se mostrara el total de paginas. si hay 10 paginas se muestan las 10
        if(totalPaginas <= numElementosPorPagina){
            desde = 1;
            hasta = totalPaginas;
            //Navegación por rangos
        }else{
            //Si tenemos muchos elementos  vamos corriendo el rango de paginas para no mostrar todas a la vez.
            if(paginaActual <= numElementosPorPagina/2) {
                desde = 1;
                hasta = numElementosPorPagina;
                //Calculamos el rango final.
            }else if (paginaActual >= totalPaginas - numElementosPorPagina /2){
                desde = totalPaginas - numElementosPorPagina + 1 ;
                hasta =  numElementosPorPagina;
            }else{
                desde = paginaActual - numElementosPorPagina / 2;
                hasta = numElementosPorPagina;
            }
        }
        //recorremos el hasta y vamos a decir si es menor de los items, agregamos cada elemento con su numero y el desde + i
        for(int i=0; i< hasta; i++){
            //Estas son las páginas que vamos a mostrar en el paginador.
            paginas.add(new PageItem(desde + i, paginaActual == desde + i));
        }
    }

    public String getUrl() {
        return url;
    }

    public List<PageItem> getPaginas() {
        return paginas;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public int getPaginaActual() {
        return paginaActual;
    }
    //Metodo para motrar el link de primera página.
    public  boolean isFirst(){
        return page.isFirst();
    }
    //Link para motrar la ultima página
    public boolean isLast(){
        return page.isLast();
    }
    //Metodos para ir siguiente
    public boolean isHasNext(){
        return page.hasNext();
    }
    //Metodo para ir al anterior.
    public boolean isHasPrevious(){
        return page.hasPrevious();
    }
}
