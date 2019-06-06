package chc.tfm.udt.view.pdf;


import chc.tfm.udt.entidades.DonacionEntity;
import chc.tfm.udt.entidades.ItemDonacionEntity;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Implementamos la clase AbstractPdfView que a su vez el padre es la interfaz View que se usa para renderizar contenido
 * del contexto.
 * Declaramos la clase con 1 Bean , componente y le dotamos del nombre que queremos eexportar.
 */
@Component("donacion/ver")
public class FacturaPdfView extends AbstractPdfView {
    /**
     *Con este metodo vamos a tener aquello que es necesario para recuperar de la vista la información y implentarla.
     * @param model obtenemos la factura através del model
     * @param document Lo utilizaremos para guardar las tablas.
     * @param writer
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        /**
         * Recuperamos el objeto donacion, al igual que en el controller lo inyectamos a la vista, en este caso estamos
         * recuperandolo para popder generar el pdf. Con el objeto PdfTable, creamos las columnas y las celdas
         */
        DonacionEntity factura = (DonacionEntity) model.get("donacion");
        /**
         * cON EL OBJETO PdfPTable , vamos a definir el numero de columnas y después vamos añadir las columnas
         * que queremos que se  vean en nuestro PDF
         */
        PdfPTable tabla = new PdfPTable(1);
        tabla.setSpacingAfter(20);
        tabla.addCell("Jugador");
        tabla.addCell(factura.getJugadorEntity().getNombre() + " " + factura.getJugadorEntity().getApellido1());
        tabla.addCell(factura.getJugadorEntity().getMail());

        PdfPTable tabla2 = new PdfPTable(1);
        tabla2.setSpacingAfter(20);
        tabla2.addCell("Datos de la Factura");
        tabla2.addCell("Folio: " + factura.getId());
        tabla2.addCell("Descripción: " + factura.getDescripcion());
        tabla2.addCell("Fecha: " + factura.getCreateAt());

        document.add(tabla);
        document.add(tabla2);

        PdfPTable tabla3 = new PdfPTable(4);
        tabla3.setSpacingAfter(40);
        tabla3.addCell("Producto");
        tabla3.addCell("Precio");
        tabla3.addCell("Cantidad");
        tabla3.addCell("Total");
        /**
         * PAra rellenar 1 lista con datos de otros objetos , hacmeos como en HTML , creamos 1 bucle FOR
         * para ir rellenando las diferentes columnas.
         */
        for(ItemDonacionEntity items : factura.getItems()){
            tabla3.addCell(items.getProductoEntity().getNombre());
            tabla3.addCell(items.getProductoEntity().getPrecio().toString());
            tabla3.addCell(items.getCantidad().toString());
            tabla3.addCell(items.calcularValor().toString());
        }
        /**
         * Este objeto se usará para darle formato al PDF.
         */
        PdfPCell cell = new PdfPCell(new Phrase("Total: "));
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabla3.addCell(cell);
        tabla3.addCell(factura.getTotal().toString());

        document.add(tabla3);

    }
}
