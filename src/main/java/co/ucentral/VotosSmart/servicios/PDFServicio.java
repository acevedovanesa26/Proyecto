package co.ucentral.VotosSmart.servicios;

import co.ucentral.VotosSmart.persistencia.entidades.Candidato;
import co.ucentral.VotosSmart.persistencia.entidades.Eleccion;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@AllArgsConstructor
@Service
public class PDFServicio {

    private final CandidatoServicio candidatoServicio;
    private final VotoServicio votoServicio;

    public ByteArrayInputStream generarReporteResultados(Eleccion eleccion) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            List<Candidato> candidatos = candidatoServicio.obtenerPorEleccionId(eleccion.getId());

            // Obtener votos por candidato
            for (Candidato candidato : candidatos) {
                Long votosCandidato = votoServicio.contarVotosPorCandidato(candidato.getId());
                candidato.setNumeroVotos(votosCandidato);
            }

            PdfWriter.getInstance(document, out);
            document.open();

            // Título del documento
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph titulo = new Paragraph("Resultados de la Elección: " + eleccion.getNombre(), fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            document.add(new Paragraph("Fecha de Inicio: " + eleccion.getFechaInicio()));
            document.add(new Paragraph("Fecha de Fin: " + eleccion.getFechaFin()));
            document.add(new Paragraph(" "));

            // Tabla de resultados
            PdfPTable tabla = new PdfPTable(3);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new int[]{4, 4, 2});

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("Nombre", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Descripción", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Votos", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcell);

            for (Candidato candidato : candidatos) {
                PdfPCell cell;

                cell = new PdfPCell(new Phrase(candidato.getNombre()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(cell);

                cell = new PdfPCell(new Phrase(candidato.getDescripcion()));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(cell);

                cell = new PdfPCell(new Phrase(candidato.getNumeroVotos().toString()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(cell);
            }

            document.add(tabla);

            document.close();

        } catch (DocumentException ex) {
            ex.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
