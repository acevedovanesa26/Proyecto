package co.ucentral.VotosSmart.servicios;

import co.ucentral.VotosSmart.persistencia.entidades.Eleccion;
import co.ucentral.VotosSmart.persistencia.entidades.Voto;
import co.ucentral.VotosSmart.persistencia.repositorios.CandidatoRepositorio;
import co.ucentral.VotosSmart.persistencia.repositorios.VotoRepositorio;

import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.awt.*;

import java.io.IOException;
import java.util.List;


@Service
@AllArgsConstructor
public class VotoServicio {

    private final VotoRepositorio votoRepositorio;
    private final EleccionServicio eleccionServicio;
    private final CandidatoRepositorio candidatoRepositorio;

    public void registrarVoto(Long votanteId, Long candidatoId, Long eleccionId) {
        Voto voto = new Voto();
        voto.setVotanteId(votanteId);
        voto.setCandidatoId(candidatoId);
        voto.setEleccionId(eleccionId);
        votoRepositorio.save(voto);
    }

    // Métodopara verificar si el votante ya votó en una elección específica
    public boolean haVotadoEnEleccion(Long votanteId, Long eleccionId) {
        return votoRepositorio.existsByVotanteIdAndEleccionId(votanteId, eleccionId);
    }
    public List<Object[]> obtenerResultadosPorEleccion(Long eleccionId) {
        return votoRepositorio.contarVotosPorCandidatoEnEleccion(eleccionId);
    }

    public Long obtenerVotosEnBlanco(Long eleccionId) {
        return votoRepositorio.contarVotosEnBlanco(eleccionId);
    }

    public byte[] generarReportePDF(Long eleccionId) {
        Eleccion eleccion = eleccionServicio.obtenerEleccionPorId(eleccionId);

        if (eleccion == null) {
            throw new IllegalArgumentException("Elección con ID " + eleccionId + " no encontrada.");
        }

        List<Object[]> resultados = votoRepositorio.contarVotosPorCandidatoEnEleccion(eleccionId);
        Long votosEnBlanco = votoRepositorio.contarVotosEnBlanco(eleccionId);

        // Identificar el candidato con más votos
        Long maxVotos = resultados.stream()
                .mapToLong(r -> ((Number) r[4]).longValue())
                .max()
                .orElse(0L);

        PDDocument document = new PDDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            // Crear una página en orientación horizontal (paisaje)
            PDRectangle landscape = new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth());
            PDPage page = new PDPage(landscape);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Definir márgenes y posiciones
            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float yPosition = yStart;

            // Añadir el logo (opcional)
            try {
                String logoPath = "src/main/resources/static/imagenes/logo.png"; // Ruta del logo
                PDImageXObject pdLogo = PDImageXObject.createFromFile(logoPath, document);
                float logoWidth = 100;
                float logoHeight = 50;
                contentStream.drawImage(pdLogo, page.getMediaBox().getWidth() - margin - logoWidth, yPosition - logoHeight, logoWidth, logoHeight);
            } catch (IOException e) {
                // Manejar el error si el logo no se encuentra
                System.out.println("Logo no encontrado.");
            }

            // Añadir el título centrado
            String titulo = "Resultados de la Elección";
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
            contentStream.setNonStrokingColor(new Color(0, 121, 107)); // Verde para el título
            float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(titulo) / 1000 * 24;
            float titleX = (page.getMediaBox().getWidth() - titleWidth) / 2;
            contentStream.newLineAtOffset(titleX, yPosition - 70);
            contentStream.showText(titulo);
            contentStream.endText();

            // Añadir el nombre de la elección centrado
            String nombreEleccion = eleccion.getNombre();
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
            contentStream.setNonStrokingColor(Color.BLACK);
            float nombreWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(nombreEleccion) / 1000 * 18;
            float nombreX = (page.getMediaBox().getWidth() - nombreWidth) / 2;
            contentStream.newLineAtOffset(nombreX, yPosition - 100);
            contentStream.showText(nombreEleccion);
            contentStream.endText();

            // Añadir fechas de inicio y fin centradas
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String fechaInicio = "Fecha de Inicio: " + sdf.format(eleccion.getFechaInicio());
            String fechaFin = "Fecha de Fin: " + sdf.format(eleccion.getFechaFin());

            String fechas = fechaInicio + "    " + fechaFin;
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 14);
            contentStream.setNonStrokingColor(Color.DARK_GRAY);
            float fechasWidth = PDType1Font.HELVETICA.getStringWidth(fechas) / 1000 * 14;
            float fechasX = (page.getMediaBox().getWidth() - fechasWidth) / 2;
            contentStream.newLineAtOffset(fechasX, yPosition - 120);
            contentStream.showText(fechas);
            contentStream.endText();

            // Dibujar la tabla de resultados
            float tableTopY = yPosition - 150;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float rowHeight = 60; // Aumentar la altura de las filas
            float yTableStart = tableTopY;

            // Definir las columnas
            float[] colWidths = {100, 200, 300, 100}; // Foto, Nombre, Descripción, Votos
            String[] headers = {"Foto", "Nombre del Candidato", "Descripción", "Votos"};

            // Dibujar encabezados de tabla con fondo color gris claro
            contentStream.setNonStrokingColor(new Color(220, 220, 220)); // Light Gray para encabezados
            contentStream.addRect(margin, yTableStart - rowHeight, tableWidth, rowHeight);
            contentStream.fill();

            // Dibujar texto de encabezado
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.setNonStrokingColor(Color.BLACK);
            float headerY = yTableStart - 40;
            contentStream.newLineAtOffset(margin + 5, headerY);
            for (int i = 0; i < headers.length; i++) {
                contentStream.showText(headers[i]);
                contentStream.newLineAtOffset(colWidths[i], 0);
            }
            contentStream.endText();

            // Dibujar líneas de la tabla
            contentStream.setStrokingColor(Color.BLACK);
            contentStream.setLineWidth(1);

            // Línea horizontal debajo de los encabezados
            contentStream.moveTo(margin, yTableStart - rowHeight);
            contentStream.lineTo(margin + tableWidth, yTableStart - rowHeight);
            contentStream.stroke();

            yPosition = yTableStart - rowHeight;

            // Alternar colores de filas para mejorar la legibilidad
            boolean fillRow = false;
            Color rowColor = new Color(245, 245, 245); // White Smoke
            Color highlightColor = new Color(11, 142, 123); // Light Green para resaltar

            float votosXPos = 0;
            for (Object[] resultado : resultados) {
                Long candidatoId = ((Number) resultado[0]).longValue();
                String nombre = (String) resultado[1];
                String descripcion = (String) resultado[2];
                Long votos = ((Number) resultado[4]).longValue();
                String imagenUrl = (String) resultado[3];

                // Determinar si esta fila tiene la mayor cantidad de votos
                boolean isMaxVotos = votos.equals(maxVotos);

                // Aplicar color de fondo alterno o resaltar si es la fila con más votos
                if (isMaxVotos) {
                    contentStream.setNonStrokingColor(highlightColor);
                    contentStream.addRect(margin, yPosition - rowHeight, tableWidth, rowHeight);
                    contentStream.fill();
                } else if (fillRow) {
                    contentStream.setNonStrokingColor(rowColor);
                    contentStream.addRect(margin, yPosition - rowHeight, tableWidth, rowHeight);
                    contentStream.fill();
                }
                fillRow = !fillRow;

                // Dibujar contenido de la fila
                // Espacio para la imagen
                float imageX = margin + 10;
                float imageY = yPosition - rowHeight + 15;
                float imageDiameter = 30;

                if (imagenUrl != null && !imagenUrl.trim().isEmpty()) {
                    try {
                        String imagenPath = "src/main/resources/static/imagenes/" + imagenUrl;
                        PDImageXObject pdImage = PDImageXObject.createFromFile(imagenPath, document);
                        // Dibujar la imagen en círculo
                        drawCircularImage(contentStream, pdImage, imageX, imageY, imageDiameter);
                    } catch (IOException e) {
                        // Si la imagen no se encuentra o hay un error, añadir texto alternativo
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
                        contentStream.setNonStrokingColor(Color.GRAY);
                        contentStream.newLineAtOffset(imageX + 5, imageY + 10);
                        contentStream.showText("Sin Img");
                        contentStream.endText();
                    }
                } else {
                    // Añadir texto alternativo si no hay imagen
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
                    contentStream.setNonStrokingColor(Color.GRAY);
                    contentStream.newLineAtOffset(imageX + 5, imageY + 10);
                    contentStream.showText("Sin Img");
                    contentStream.endText();
                }

                // Definir posiciones X para cada columna
                float nombreXPos = margin + colWidths[0] + 10;
                float descripcionXPos = margin + colWidths[0] + colWidths[1] + 10;
                votosXPos = margin + colWidths[0] + colWidths[1] + colWidths[2] + 10;

                // Definir posición Y para el texto
                float textY = yPosition - rowHeight + 35;

                // Nombre del candidato
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(nombreXPos, textY);
                contentStream.showText(nombre);
                contentStream.endText();

                // Descripción del candidato
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(descripcionXPos, textY);
                contentStream.showText(descripcion != null ? descripcion : "N/A");
                contentStream.endText();

                // Votos del candidato, alineados a la izquierda dentro de su columna
                String votosTexto = String.valueOf(votos);
                float votosTextWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(votosTexto) / 1000 * 12;
                // Alinear a la izquierda dentro de la columna de votos
                float votosFinalX = votosXPos + 5f; // Ajustado para alinear correctamente

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.newLineAtOffset(votosFinalX, textY);
                contentStream.showText(votosTexto);
                contentStream.endText();

                // Dibujar líneas horizontales
                contentStream.moveTo(margin, yPosition - rowHeight);
                contentStream.lineTo(margin + tableWidth, yPosition - rowHeight);
                contentStream.stroke();

                yPosition -= rowHeight;
            }

            // Añadir votos en blanco al final de la tabla
            // Dibujar fondo de la fila de votos en blanco
            contentStream.setNonStrokingColor(new Color(220, 220, 220)); // Light Gray para votos en blanco
            contentStream.addRect(margin, yPosition - rowHeight, tableWidth, rowHeight);
            contentStream.fill();

            // Dibujar texto de votos en blanco
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.setNonStrokingColor(Color.BLACK);
            float votosBlancoXPos = margin + 5;
            float votosBlancoYPos = yPosition - rowHeight + 25;
            contentStream.newLineAtOffset(votosBlancoXPos, votosBlancoYPos);
            contentStream.showText("Votos en Blanco");
            contentStream.endText();

            // Votos en blanco alineados a la izquierda dentro de la columna de votos
            String votosBlancoTexto = String.valueOf(votosEnBlanco);
            float votosBlancoTextWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(votosBlancoTexto) / 1000 * 14;
            // Alinear a la izquierda dentro de la columna de votos
            float votosBlancoFinalX = votosXPos + 5f; // Ajustado para alinear correctamente

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.setNonStrokingColor(Color.BLACK);
            contentStream.newLineAtOffset(votosBlancoFinalX, votosBlancoYPos);
            contentStream.showText(votosBlancoTexto);
            contentStream.endText();

            // Dibujar línea final de la tabla
            contentStream.moveTo(margin, yPosition - rowHeight);
            contentStream.lineTo(margin + tableWidth, yPosition - rowHeight);
            contentStream.stroke();

            // Cerrar el content stream
            contentStream.close();

            // Guardar el documento en el ByteArrayOutputStream
            document.save(out);
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar el PDF", e);
        }

        return out.toByteArray();
    }

    /**
     * Dibuja una imagen circular en el PDF.
     *
     * @param contentStream El content stream del PDF.
     * @param pdImage       La imagen a dibujar.
     * @param x             Coordenada X de la esquina inferior izquierda del círculo.
     * @param y             Coordenada Y de la esquina inferior izquierda del círculo.
     * @param diameter      Diámetro del círculo.
     * @throws IOException Si ocurre un error al dibujar la imagen.
     */
    private void drawCircularImage(PDPageContentStream contentStream, PDImageXObject pdImage, float x, float y, float diameter) throws IOException {
        // Guardar el estado gráfico actual
        contentStream.saveGraphicsState();

        // Definir parámetros para el círculo
        float radius = diameter / 2;
        float centerX = x + radius;
        float centerY = y + radius;
        float k = 0.5522847498f; // Constante para aproximar un círculo con curvas de Bezier

        // Crear el path del círculo usando curvas de Bezier
        contentStream.moveTo(centerX + radius, centerY);
        contentStream.curveTo(centerX + radius, centerY + k * radius,
                centerX + k * radius, centerY + radius,
                centerX, centerY + radius);
        contentStream.curveTo(centerX - k * radius, centerY + radius,
                centerX - radius, centerY + k * radius,
                centerX - radius, centerY);
        contentStream.curveTo(centerX - radius, centerY - k * radius,
                centerX - k * radius, centerY - radius,
                centerX, centerY - radius);
        contentStream.curveTo(centerX + k * radius, centerY - radius,
                centerX + radius, centerY - k * radius,
                centerX + radius, centerY);
        contentStream.closePath();

        // Aplicar clipping al círculo
        contentStream.clip();

        // Dibujar la imagen dentro del clipping circular
        contentStream.drawImage(pdImage, x, y, diameter, diameter);

        // Restaurar el estado gráfico original
        contentStream.restoreGraphicsState();
    }
}


