package chc.tfm.udt.view.pdf;


import chc.tfm.udt.entidades.DonacionEntity;
import chc.tfm.udt.entidades.ItemDonacionEntity;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Locale;
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
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private LocaleResolver localeResolver;
    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        /**
         * Recuperamos el objeto donacion, al igual que en el controller lo inyectamos a la vista, en este caso estamos
         * recuperandolo para popder generar el pdf. Con el objeto PdfTable, creamos las columnas y las celdas
         */
        DonacionEntity factura = (DonacionEntity) model.get("donacion");
        Locale locale = localeResolver.resolveLocale(request);

        /**
         * MessagesSorcuer, tiene esta clase que recupera ya directamente el Locale , sin necesidad e instanciarlo,
         * entonces es 1 manera mucho mas sencilla de recuperar los mensajes.
         * únicamente debemos recuperar el mensaje , si necesidad de llamar al locale en cada recuperación del mensaje.
         */
        MessageSourceAccessor mensajes = getMessageSourceAccessor();

        /**
         * cON EL OBJETO PdfPTable , vamos a definir el numero de columnas y después vamos añadir las columnas
         * que queremos que se  vean en nuestro PDF
         */
        PdfPTable tabla = new PdfPTable(1);
        tabla.setSpacingAfter(20);
        /**con el Objeto PdfPCell , vamos a definir el Style de la celda
         *
         */
        PdfPCell cell = null;
        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.pdf.datos.jugador", null, locale)));
        cell.setBackgroundColor(new Color(184,218 , 255));
        cell.setPadding(8f);
        tabla.addCell(cell);
        tabla.addCell(factura.getJugadorEntity().getNombre() + " " + factura.getJugadorEntity().getApellido1());
        tabla.addCell(factura.getJugadorEntity().getMail());

        PdfPTable tabla2 = new PdfPTable(1);
        tabla2.setSpacingAfter(20);
        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.pdf.datos.factura", null, locale)));
        cell.setBackgroundColor(new Color(195,230 , 203));
        cell.setPadding(8f);
        tabla2.addCell(cell);

        tabla2.addCell(mensajes.getMessage("text.pdf.datos.detalle.folio") + factura.getId());
        tabla2.addCell(mensajes.getMessage("text.pdf.datos.detalle.descripcion") + factura.getDescripcion());
        tabla2.addCell(mensajes.getMessage("text.pdf.datos.detalle.fecha") + factura.getCreateAt());

        document.add(tabla);
        document.add(tabla2);
        /**
         * Con setWidth vamos a añadir un espacio relativo a las columnas.
         */
        PdfPTable tabla3 = new PdfPTable(4);
        tabla3.setSpacingAfter(40);
        tabla3.setWidths(new float[]{3.5f,1,1,1});
        tabla3.addCell(messageSource.getMessage("text.pdf.datos.detalle.producto",null , locale));
        tabla3.addCell(messageSource.getMessage("text.pdf.datos.detalle.precio", null, locale));
        tabla3.addCell(messageSource.getMessage("text.pdf.datos.detalle.cantidad", null, locale));
        tabla3.addCell(messageSource.getMessage("text.pdf.datos.detalle.total", null, locale));
        /**
         * PAra rellenar 1 lista con datos de otros objetos , hacmeos como en HTML , creamos 1 bucle FOR
         * para ir rellenando las diferentes columnas.
         * Si queremos añadir estilos a las columnas podemos hacer como anteriormene , declarar el objeto PdfPcell y asi
         * podemos darle estilos.
         */
        for(ItemDonacionEntity items : factura.getItems()){
            tabla3.addCell(items.getProductoEntity().getNombre());
            tabla3.addCell(items.getProductoEntity().getPrecio().toString());
            cell = new PdfPCell(new Phrase(items.getCantidad().toString()));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            tabla3.addCell(cell);
            tabla3.addCell(items.calcularValor().toString());
        }
        /**
         * Este objeto se usará para darle formato al PDF.
         */
        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.pdf.datos.detalle.granTotal", null, locale)));
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabla3.addCell(cell);
        tabla3.addCell(factura.getTotal().toString());

        document.add(tabla3);

    }
}
