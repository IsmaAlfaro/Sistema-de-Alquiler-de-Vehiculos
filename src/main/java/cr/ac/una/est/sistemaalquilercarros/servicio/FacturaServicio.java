package cr.ac.una.est.sistemaalquilercarros.servicio;

import cr.ac.una.est.sistemaalquilercarros.modelo.Alquiler;
import cr.ac.una.est.sistemaalquilercarros.modelo.EstadoAlquiler;
import cr.ac.una.est.sistemaalquilercarros.modelo.MetodoPago;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FacturaServicio {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /*
     * Qué hace: Genera un archivo PDF estilizado con la información completa del alquiler seleccionado, mostrando una factura inicial cuando el alquiler está activo o una factura final cuando ya fue devuelto
     * Recibe: El alquiler del cual se desea generar la factura
     * Retorna: El archivo PDF generado
     */
    public File generarFactura(Alquiler alquiler) {

        if (alquiler == null) {
            throw new IllegalArgumentException("Debe seleccionar un alquiler.");
        }

        File carpetaFacturas = new File("facturas");

        if (!carpetaFacturas.exists()) {
            carpetaFacturas.mkdirs();
        }

        File archivo = new File(
                carpetaFacturas,
                "Factura_Alquiler_" + alquiler.getNumeroAlquiler() + ".pdf"
        );

        try (PDDocument documento = new PDDocument()) {

            PDPage pagina = new PDPage(PDRectangle.A4);

            documento.addPage(pagina);

            PDFont fuenteNormal =
                    PDType0Font.load(
                            documento,
                            new File("C:/Windows/Fonts/arial.ttf")
                    );

            PDFont fuenteNegrita =
                    PDType0Font.load(
                            documento,
                            new File("C:/Windows/Fonts/arialbd.ttf")
                    );

            try (PDPageContentStream contenido =
                         new PDPageContentStream(documento, pagina)) {

                float margen = 45;
                float anchoPagina = pagina.getMediaBox().getWidth();
                float anchoContenido = anchoPagina - margen * 2;

                float y = 797;

                /*
                 * Encabezado principal
                 */

                dibujarRectanguloRelleno(
                        contenido,
                        margen,
                        742,
                        anchoContenido,
                        65,
                        20,
                        115,
                        190
                );

                escribirTextoColor(
                        contenido,
                        fuenteNegrita,
                        22,
                        margen + 18,
                        778,
                        "XANTIUM",
                        255,
                        255,
                        255
                );

                escribirTextoColor(
                        contenido,
                        fuenteNormal,
                        10,
                        margen + 18,
                        760,
                        "Sistema de Alquiler de Vehículos",
                        255,
                        255,
                        255
                );

                String titulo =
                        alquiler.getEstado() == EstadoAlquiler.ACTIVO
                                ? "FACTURA DE ALQUILER"
                                : "FACTURA FINAL DE ALQUILER";

                escribirTextoDerechaColor(
                        contenido,
                        fuenteNegrita,
                        13,
                        anchoPagina - margen - 18,
                        779,
                        titulo,
                        255,
                        255,
                        255
                );

                escribirTextoDerechaColor(
                        contenido,
                        fuenteNormal,
                        10,
                        anchoPagina - margen - 18,
                        760,
                        "Alquiler #" + alquiler.getNumeroAlquiler(),
                        255,
                        255,
                        255
                );

                y = 718;

                /*
                 * Estado del alquiler
                 */

                escribirTexto(
                        contenido,
                        fuenteNegrita,
                        10,
                        margen,
                        y,
                        "Estado:"
                );

                escribirTexto(
                        contenido,
                        fuenteNormal,
                        10,
                        margen + 45,
                        y,
                        alquiler.getEstado().toString()
                );

                y -= 27;

                /*
                 * Cliente y vehículo
                 */

                dibujarTituloSeccion(
                        contenido,
                        fuenteNegrita,
                        margen,
                        y,
                        anchoContenido,
                        "DATOS GENERALES"
                );

                y -= 28;

                float columnaIzquierda = margen + 12;
                float columnaDerecha = margen + anchoContenido / 2 + 12;

                escribirTexto(
                        contenido,
                        fuenteNegrita,
                        11,
                        columnaIzquierda,
                        y,
                        "Cliente"
                );

                escribirTexto(
                        contenido,
                        fuenteNegrita,
                        11,
                        columnaDerecha,
                        y,
                        "Vehículo"
                );

                y -= 19;

                escribirTexto(
                        contenido,
                        fuenteNormal,
                        9,
                        columnaIzquierda,
                        y,
                        alquiler.getCliente().getNombreCompleto()
                );

                escribirTexto(
                        contenido,
                        fuenteNormal,
                        9,
                        columnaDerecha,
                        y,
                        alquiler.getVehiculo().getMarca()
                                + " "
                                + alquiler.getVehiculo().getModelo()
                );

                y -= 16;

                escribirTexto(
                        contenido,
                        fuenteNormal,
                        9,
                        columnaIzquierda,
                        y,
                        "Cédula: " + alquiler.getCliente().getCedula()
                );

                escribirTexto(
                        contenido,
                        fuenteNormal,
                        9,
                        columnaDerecha,
                        y,
                        "Placa: " + alquiler.getVehiculo().getPlaca()
                );

                y -= 16;

                escribirTexto(
                        contenido,
                        fuenteNormal,
                        9,
                        columnaIzquierda,
                        y,
                        "Teléfono: " + alquiler.getCliente().getTelefono()
                );

                y -= 16;

                escribirTexto(
                        contenido,
                        fuenteNormal,
                        9,
                        columnaIzquierda,
                        y,
                        "Correo: " + alquiler.getCliente().getCorreo()
                );

                y -= 28;

                /*
                 * Datos del alquiler
                 */

                dibujarTituloSeccion(
                        contenido,
                        fuenteNegrita,
                        margen,
                        y,
                        anchoContenido,
                        "DETALLE DEL ALQUILER"
                );

                y -= 28;

                float columnaDatos = margen + 12;
                float columnaMontos = margen + 300;

                escribirTexto(
                        contenido,
                        fuenteNormal,
                        9,
                        columnaDatos,
                        y,
                        "Fecha de inicio"
                );

                escribirTexto(
                        contenido,
                        fuenteNegrita,
                        9,
                        margen + 145,
                        y,
                        formatoFecha(alquiler.getFechaInicio())
                );

                escribirTexto(
                        contenido,
                        fuenteNormal,
                        9,
                        columnaMontos,
                        y,
                        "Tarifa diaria"
                );

                escribirTextoDerecha(
                        contenido,
                        fuenteNegrita,
                        9,
                        margen + anchoContenido - 12,
                        y,
                        formatoDinero(alquiler.getTarifaDiariaAplicada())
                );

                y -= 18;

                escribirTexto(
                        contenido,
                        fuenteNormal,
                        9,
                        columnaDatos,
                        y,
                        "Fecha esperada"
                );

                escribirTexto(
                        contenido,
                        fuenteNegrita,
                        9,
                        margen + 145,
                        y,
                        formatoFecha(alquiler.getFechaDevolucionEsperada())
                );

                escribirTexto(
                        contenido,
                        fuenteNormal,
                        9,
                        columnaMontos,
                        y,
                        "Subtotal"
                );

                escribirTextoDerecha(
                        contenido,
                        fuenteNegrita,
                        9,
                        margen + anchoContenido - 12,
                        y,
                        formatoDinero(alquiler.calcularSubtotal())
                );

                y -= 18;

                escribirTexto(
                        contenido,
                        fuenteNormal,
                        9,
                        columnaDatos,
                        y,
                        "Cantidad de días"
                );

                escribirTexto(
                        contenido,
                        fuenteNegrita,
                        9,
                        margen + 145,
                        y,
                        String.valueOf(alquiler.getCantidadDias())
                );

                escribirTexto(
                        contenido,
                        fuenteNormal,
                        9,
                        columnaMontos,
                        y,
                        "Depósito"
                );

                escribirTextoDerecha(
                        contenido,
                        fuenteNegrita,
                        9,
                        margen + anchoContenido - 12,
                        y,
                        formatoDinero(alquiler.getDepositoGarantia())
                );

                y -= 27;

                /*
                 * Total inicial
                 */

                dibujarRectanguloRelleno(
                        contenido,
                        margen,
                        y - 10,
                        anchoContenido,
                        31,
                        225,
                        239,
                        250
                );

                escribirTexto(
                        contenido,
                        fuenteNegrita,
                        11,
                        margen + 12,
                        y,
                        "TOTAL PAGADO"
                );

                escribirTextoDerechaColor(
                        contenido,
                        fuenteNegrita,
                        13,
                        margen + anchoContenido - 12,
                        y,
                        formatoDinero(alquiler.getMontoPagado()),
                        20,
                        115,
                        190
                );

                y -= 42;

                /*
                 * Pago inicial
                 */

                dibujarTituloSeccion(
                        contenido,
                        fuenteNegrita,
                        margen,
                        y,
                        anchoContenido,
                        "PAGO INICIAL"
                );

                y -= 28;

                escribirTexto(
                        contenido,
                        fuenteNormal,
                        9,
                        columnaIzquierda,
                        y,
                        "Método de pago"
                );

                escribirTexto(
                        contenido,
                        fuenteNegrita,
                        9,
                        margen + 130,
                        y,
                        String.valueOf(alquiler.getMetodoPago())
                );

                if (alquiler.getMetodoPago() == MetodoPago.EFECTIVO) {

                    escribirTexto(
                            contenido,
                            fuenteNormal,
                            9,
                            columnaDerecha,
                            y,
                            "Monto recibido"
                    );

                    escribirTextoDerecha(
                            contenido,
                            fuenteNegrita,
                            9,
                            margen + anchoContenido - 12,
                            y,
                            formatoDinero(alquiler.getMontoRecibido())
                    );

                    y -= 18;

                    escribirTexto(
                            contenido,
                            fuenteNormal,
                            9,
                            columnaDerecha,
                            y,
                            "Vuelto"
                    );

                    escribirTextoDerecha(
                            contenido,
                            fuenteNegrita,
                            9,
                            margen + anchoContenido - 12,
                            y,
                            formatoDinero(alquiler.getVuelto())
                    );
                }

                /*
                 * Información de devolución
                 */

                if (alquiler.getEstado() == EstadoAlquiler.FINALIZADO) {

                    /*
                     * Línea divisoria entre el pago inicial y la devolución
                     */

                    y -= 22;

                    dibujarLinea(
                            contenido,
                            margen,
                            y,
                            margen + anchoContenido,
                            y,
                            190,
                            200,
                            210
                    );

                    y -= 22;

                    LocalDate fechaDevolucion =
                            alquiler.getFechaDevolucionReal();

                    long diasAtraso =
                            alquiler.calcularDiasAtraso(fechaDevolucion);

                    double multa =
                            alquiler.calcularMulta(fechaDevolucion);

                    double depositoDevuelto =
                            alquiler.calcularDepositoADevolver(fechaDevolucion);

                    double saldoPendiente =
                            alquiler.calcularSaldoPendiente(fechaDevolucion);

                    dibujarTituloSeccion(
                            contenido,
                            fuenteNegrita,
                            margen,
                            y,
                            anchoContenido,
                            "DEVOLUCIÓN"
                    );

                    y -= 28;

                    escribirTexto(
                            contenido,
                            fuenteNormal,
                            9,
                            columnaDatos,
                            y,
                            "Fecha real"
                    );

                    escribirTexto(
                            contenido,
                            fuenteNegrita,
                            9,
                            margen + 145,
                            y,
                            formatoFecha(fechaDevolucion)
                    );

                    escribirTexto(
                            contenido,
                            fuenteNormal,
                            9,
                            columnaMontos,
                            y,
                            "Multa"
                    );

                    escribirTextoDerecha(
                            contenido,
                            fuenteNegrita,
                            9,
                            margen + anchoContenido - 12,
                            y,
                            formatoDinero(multa)
                    );

                    y -= 18;

                    escribirTexto(
                            contenido,
                            fuenteNormal,
                            9,
                            columnaDatos,
                            y,
                            "Días de atraso"
                    );

                    escribirTexto(
                            contenido,
                            fuenteNegrita,
                            9,
                            margen + 145,
                            y,
                            String.valueOf(diasAtraso)
                    );

                    escribirTexto(
                            contenido,
                            fuenteNormal,
                            9,
                            columnaMontos,
                            y,
                            "Depósito devuelto"
                    );

                    escribirTextoDerecha(
                            contenido,
                            fuenteNegrita,
                            9,
                            margen + anchoContenido - 12,
                            y,
                            formatoDinero(depositoDevuelto)
                    );

                    y -= 28;

                    /*
                     * Saldo adicional sin marco
                     */

                    escribirTexto(
                            contenido,
                            fuenteNegrita,
                            10,
                            margen + 12,
                            y,
                            "SALDO ADICIONAL"
                    );

                    escribirTextoDerechaColor(
                            contenido,
                            fuenteNegrita,
                            11,
                            margen + anchoContenido - 12,
                            y,
                            formatoDinero(saldoPendiente),
                            20,
                            115,
                            190
                    );

                    if (alquiler.getMetodoPagoDevolucion() != null) {

                        y -= 42;

                        dibujarTituloSeccion(
                                contenido,
                                fuenteNegrita,
                                margen,
                                y,
                                anchoContenido,
                                "PAGO ADICIONAL DE DEVOLUCIÓN"
                        );

                        y -= 28;

                        escribirTexto(
                                contenido,
                                fuenteNormal,
                                9,
                                columnaIzquierda,
                                y,
                                "Método de pago"
                        );

                        escribirTexto(
                                contenido,
                                fuenteNegrita,
                                9,
                                margen + 130,
                                y,
                                String.valueOf(alquiler.getMetodoPagoDevolucion())
                        );

                        escribirTexto(
                                contenido,
                                fuenteNormal,
                                9,
                                columnaDerecha,
                                y,
                                "Monto pagado"
                        );

                        escribirTextoDerecha(
                                contenido,
                                fuenteNegrita,
                                9,
                                margen + anchoContenido - 12,
                                y,
                                formatoDinero(alquiler.getMontoPagadoDevolucion())
                        );

                        if (alquiler.getMetodoPagoDevolucion() == MetodoPago.EFECTIVO) {

                            y -= 18;

                            escribirTexto(
                                    contenido,
                                    fuenteNormal,
                                    9,
                                    columnaIzquierda,
                                    y,
                                    "Monto recibido"
                            );

                            escribirTexto(
                                    contenido,
                                    fuenteNegrita,
                                    9,
                                    margen + 130,
                                    y,
                                    formatoDinero(alquiler.getMontoRecibidoDevolucion())
                            );

                            escribirTexto(
                                    contenido,
                                    fuenteNormal,
                                    9,
                                    columnaDerecha,
                                    y,
                                    "Vuelto"
                            );

                            escribirTextoDerecha(
                                    contenido,
                                    fuenteNegrita,
                                    9,
                                    margen + anchoContenido - 12,
                                    y,
                                    formatoDinero(alquiler.getVueltoDevolucion())
                            );
                        }
                    }
                }

                /*
                 * Pie de página
                 */

                dibujarLinea(
                        contenido,
                        margen,
                        55,
                        margen + anchoContenido,
                        55,
                        180,
                        180,
                        180
                );

                escribirTexto(
                        contenido,
                        fuenteNormal,
                        8,
                        margen,
                        37,
                        "Gracias por utilizar XANTIUM"
                );

                escribirTextoDerecha(
                        contenido,
                        fuenteNormal,
                        8,
                        margen + anchoContenido,
                        37,
                        "Alquiler #" + alquiler.getNumeroAlquiler()
                );
            }

            documento.save(archivo);

            return archivo;

        } catch (IOException e) {

            throw new RuntimeException(
                    "No se pudo generar la factura PDF.",
                    e
            );
        }
    }

    /*
     * Qué hace: Escribe una línea de texto dentro del documento PDF utilizando la fuente, tamaño y posición indicados
     * Recibe: El flujo de contenido del PDF, la fuente, el tamaño de letra, las coordenadas X e Y y el texto que se desea escribir
     * Retorna: No retorna ningún valor
     */
    private void escribirTexto(
            PDPageContentStream contenido,
            PDFont fuente,
            float tamano,
            float x,
            float y,
            String texto) throws IOException {

        contenido.setNonStrokingColor(
                0f,
                0f,
                0f
        );

        contenido.beginText();

        contenido.setFont(
                fuente,
                tamano
        );

        contenido.newLineAtOffset(
                x,
                y
        );

        contenido.showText(
                texto
        );

        contenido.endText();
    }

    /*
     * Qué hace: Escribe un texto utilizando un color específico dentro del documento PDF convirtiendo los valores RGB de 0 a 255 al rango de 0 a 1 utilizado por PDFBox
     * Recibe: El flujo de contenido, la fuente, tamaño, posición, texto y valores RGB del color
     * Retorna: No retorna ningún valor
     */
    private void escribirTextoColor(
            PDPageContentStream contenido,
            PDFont fuente,
            float tamano,
            float x,
            float y,
            String texto,
            int rojo,
            int verde,
            int azul) throws IOException {

        contenido.setNonStrokingColor(
                rojo / 255f,
                verde / 255f,
                azul / 255f
        );

        contenido.beginText();

        contenido.setFont(
                fuente,
                tamano
        );

        contenido.newLineAtOffset(
                x,
                y
        );

        contenido.showText(
                texto
        );

        contenido.endText();

        contenido.setNonStrokingColor(
                0f,
                0f,
                0f
        );
    }

    /*
     * Qué hace: Escribe un texto alineado hacia la derecha tomando como referencia una posición horizontal determinada
     * Recibe: El flujo de contenido, la fuente, el tamaño, la posición derecha, la posición vertical y el texto
     * Retorna: No retorna ningún valor
     */
    private void escribirTextoDerecha(
            PDPageContentStream contenido,
            PDFont fuente,
            float tamano,
            float xDerecha,
            float y,
            String texto) throws IOException {

        float anchoTexto =
                fuente.getStringWidth(texto)
                        / 1000
                        * tamano;

        escribirTexto(
                contenido,
                fuente,
                tamano,
                xDerecha - anchoTexto,
                y,
                texto
        );
    }

    /*
     * Qué hace: Escribe un texto de color alineado hacia la derecha tomando como referencia una posición horizontal determinada
     * Recibe: El flujo de contenido, la fuente, el tamaño, la posición derecha, la posición vertical, el texto y los valores RGB
     * Retorna: No retorna ningún valor
     */
    private void escribirTextoDerechaColor(
            PDPageContentStream contenido,
            PDFont fuente,
            float tamano,
            float xDerecha,
            float y,
            String texto,
            int rojo,
            int verde,
            int azul) throws IOException {

        float anchoTexto =
                fuente.getStringWidth(texto)
                        / 1000
                        * tamano;

        escribirTextoColor(
                contenido,
                fuente,
                tamano,
                xDerecha - anchoTexto,
                y,
                texto,
                rojo,
                verde,
                azul
        );
    }

    /*
     * Qué hace: Dibuja un rectángulo completamente relleno utilizando un color RGB y convierte sus valores al rango requerido por PDFBox
     * Recibe: El flujo de contenido, la posición, el tamaño del rectángulo y los valores RGB del color
     * Retorna: No retorna ningún valor
     */
    private void dibujarRectanguloRelleno(
            PDPageContentStream contenido,
            float x,
            float y,
            float ancho,
            float alto,
            int rojo,
            int verde,
            int azul) throws IOException {

        contenido.setNonStrokingColor(
                rojo / 255f,
                verde / 255f,
                azul / 255f
        );

        contenido.addRect(
                x,
                y,
                ancho,
                alto
        );

        contenido.fill();

        contenido.setNonStrokingColor(
                0f,
                0f,
                0f
        );
    }

    /*
     * Qué hace: Dibuja una línea dentro del documento PDF utilizando un color RGB convertido al rango requerido por PDFBox
     * Recibe: El flujo de contenido, las coordenadas de inicio y fin y los valores RGB
     * Retorna: No retorna ningún valor
     */
    private void dibujarLinea(
            PDPageContentStream contenido,
            float xInicio,
            float yInicio,
            float xFin,
            float yFin,
            int rojo,
            int verde,
            int azul) throws IOException {

        contenido.setStrokingColor(
                rojo / 255f,
                verde / 255f,
                azul / 255f
        );

        contenido.setLineWidth(
                0.7f
        );

        contenido.moveTo(
                xInicio,
                yInicio
        );

        contenido.lineTo(
                xFin,
                yFin
        );

        contenido.stroke();

        contenido.setStrokingColor(
                0f,
                0f,
                0f
        );
    }

    /*
     * Qué hace: Dibuja una barra visual utilizada como encabezado para separar cada sección de la factura
     * Recibe: El flujo de contenido, la fuente en negrita, la posición, el ancho disponible y el título de la sección
     * Retorna: No retorna ningún valor
     */
    private void dibujarTituloSeccion(
            PDPageContentStream contenido,
            PDFont fuente,
            float x,
            float y,
            float ancho,
            String titulo) throws IOException {

        dibujarRectanguloRelleno(
                contenido,
                x,
                y - 7,
                ancho,
                23,
                235,
                243,
                249
        );

        escribirTextoColor(
                contenido,
                fuente,
                10,
                x + 10,
                y,
                titulo,
                20,
                90,
                150
        );
    }

    /*
     * Qué hace: Convierte una fecha al formato día, mes y año utilizado dentro de la factura
     * Recibe: La fecha que se desea mostrar
     * Retorna: La fecha convertida al formato dd/MM/yyyy o un guion cuando no existe
     */
    private String formatoFecha(
            LocalDate fecha) {

        if (fecha == null) {
            return "-";
        }

        return fecha.format(
                FORMATO_FECHA
        );
    }

    /*
     * Qué hace: Convierte un valor numérico a un texto con formato monetario en colones costarricenses
     * Recibe: El monto que se desea mostrar
     * Retorna: Una cadena de texto con el monto formateado utilizando el símbolo del colón
     */
    private String formatoDinero(
            Double monto) {

        if (monto == null) {
            return "₡0.00";
        }

        return String.format(
                "₡%,.2f",
                monto
        );
    }
}