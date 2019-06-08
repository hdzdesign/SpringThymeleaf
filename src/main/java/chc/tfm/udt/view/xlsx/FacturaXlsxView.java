package chc.tfm.udt.view.xlsx;

import chc.tfm.udt.entidades.DonacionEntity;
import chc.tfm.udt.entidades.ItemDonacionEntity;
import org.apache.poi.ss.usermodel.*;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Se le da este nombre porque no se puede anotar con el mismo nombre a 2 componentes de Spring
 */
@Component("donacion/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView {
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
/**
 * - Traemos la factura con el objeto model.
 * - Recuperamos el GetMessage para poder hacer de nuestra aplicación multilenguaje y tener los textos en 1 propertie
 * - Utilizamos el objeto response para cambiar el nombre del archivo que se va a descargar.
 */
        response.setHeader("Content-Disposition", "attachment; filename=\"Factura_View.xlsx\"");
        DonacionEntity factura = (DonacionEntity) model.get("donacion");
        MessageSourceAccessor mensaje = getMessageSourceAccessor();
        /**
         * Con esta implemntación creamos nuestra planilla
         */
        Sheet sheet = workbook.createSheet("Factura Spring");

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);

        cell.setCellValue(mensaje.getMessage("text.pdf.datos.jugador"));
        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue(factura.getJugadorEntity().getNombre() + " " + factura.getJugadorEntity().getApellido1());

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue(factura.getJugadorEntity().getMail());

        /**
         * Metodo mas simple para hacer lo mismo que arriba , consiste en encadenar metodos hasta llegar al setCellValue
         * 1º Creamos la Row
         * 2º Creamos la celda
         * 3º Seteamos el valor en la celda.
         */

        sheet.createRow(4).
                createCell(0).
                setCellValue(mensaje.getMessage("text.pdf.datos.factura"));
        sheet.createRow(5).
                createCell(0).
                setCellValue(mensaje.getMessage("text.pdf.datos.detalle.folio")+ factura.getId());
        sheet.createRow(6).
                createCell(0).
                setCellValue(mensaje.getMessage("text.pdf.datos.detalle.descripcion")+ factura.getDescripcion());
        sheet.createRow(7).
                createCell(0).
                setCellValue(mensaje.getMessage("text.pdf.datos.detalle.fecha")+ factura.getCreateAt());
        /**
         * Con este objeto podemos aplicar Estilos a cada una de las partes de nuestro documento, lo recuoperamos del
         * objeto workbook de la implentación del metodo. El que nos porpociona todos los estilos.
         */
        CellStyle theaderStyle = workbook.createCellStyle();
        theaderStyle.setBorderBottom(BorderStyle.MEDIUM);
        theaderStyle.setBorderTop(BorderStyle.MEDIUM);
        theaderStyle.setBorderRight(BorderStyle.MEDIUM);
        theaderStyle.setBorderLeft(BorderStyle.MEDIUM);
        theaderStyle.setFillForegroundColor(IndexedColors.GOLD.index);
        theaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle tbodyStyle = workbook.createCellStyle();
        tbodyStyle.setBorderBottom(BorderStyle.THIN);
        tbodyStyle.setBorderTop(BorderStyle.THIN);
        tbodyStyle.setBorderRight(BorderStyle.THIN);
        tbodyStyle.setBorderLeft(BorderStyle.THIN);
        /**
         * Cremos la Row Para el producto.  en la fila 9 o en la que queramos definiendola en el createRow.
         * Añadimos las cabeceras , con 1 texto.
         */
        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue(mensaje.getMessage("text.pdf.datos.detalle.producto"));
        header.createCell(1).setCellValue(mensaje.getMessage("text.pdf.datos.detalle.precio"));
        header.createCell(2).setCellValue(mensaje.getMessage("text.pdf.datos.detalle.cantidad"));
        header.createCell(3).setCellValue(mensaje.getMessage("text.pdf.datos.detalle.total"));
        header.getCell(0).setCellStyle(theaderStyle);
        header.getCell(1).setCellStyle(theaderStyle);
        header.getCell(2).setCellStyle(theaderStyle);
        header.getCell(3).setCellStyle(theaderStyle);
        /**
         * Para la parte en la que vamos a ir seteando los valores en cada fila , es muy importante tener claro que
         * debemos añadir 1 contador para ir sumando 1 a cada ROW , donde se va a insertar el dato.
         *
         * Para aplicar Estilos debemos coger el objeto Cell definido arriba para aplicar los estilos, igualandolo a
         * fila.createCell. haciendo esto podemos modificar el objeto cell.
         */
        int rownum = 10;
        for(ItemDonacionEntity item : factura.getItems()){
            Row fila = sheet.createRow(rownum ++);
            cell = fila.createCell(0);
            cell.setCellValue(item.getProductoEntity().getNombre());
            cell.setCellStyle(tbodyStyle);
            cell = fila.createCell(1);
            cell.setCellValue(item.getProductoEntity().getPrecio());
            cell.setCellStyle(tbodyStyle);
            cell = fila.createCell(2);
            cell.setCellValue(item.getCantidad());
            cell.setCellStyle(tbodyStyle);
            cell = fila.createCell(3);
            cell.setCellValue(item.calcularValor());
            cell.setCellStyle(tbodyStyle);
        }
        /**
         * Para añadir el gran total , debemos recoger el valor del contador en su ultimo valor , y añadir una fila nueva.
         * En esta fila vamos a tener 2 columnas , la primera con el mensaje del Label y la segunda con el gran total.
         */
        Row filaTotal = sheet.createRow(rownum);
        cell = filaTotal.createCell(2);
        cell.setCellValue(mensaje.getMessage("text.pdf.datos.detalle.granTotal"));
        cell.setCellStyle(tbodyStyle);

        cell = filaTotal.createCell(3);
        cell.setCellValue(factura.getTotal());
        cell.setCellStyle(tbodyStyle);


    }
}
