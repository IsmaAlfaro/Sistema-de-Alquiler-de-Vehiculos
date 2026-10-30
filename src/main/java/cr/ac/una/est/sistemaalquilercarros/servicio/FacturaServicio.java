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
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FacturaServicio {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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

        File archivo = new File(carpetaFacturas, "Factura_Alquiler_" + alquiler.getNumeroAlquiler() + ".pdf");

        try (PDDocument documento = new PDDocument()) {

            PDPage pagina = new PDPage(PDRectangle.A4);

            documento.addPage(pagina);

            PDFont fuenteNormal = PDType0Font.load(documento, new File("C:/Windows/Fonts/arial.ttf"));

            PDFont fuenteNegrita = PDType0Font.load(documento, new File("C:/Windows/Fonts/arialbd.ttf"));

            /*
             * Logo de XANTIUM
             */

            PDImageXObject logo;

            try (InputStream logoStream = getClass().getResourceAsStream("/cr/ac/una/est/sistemaalquilercarros/img/logo.png")) {

                if (logoStream == null) {

                    throw new IllegalStateException("No se pudo encontrar el logo de XANTIUM.");

                }

                logo = PDImageXObject.createFromByteArray(documento, logoStream.readAllBytes(), "logo-xantium");

            }

            try (PDPageContentStream contenido = new PDPageContentStream(documento, pagina)) {

                float margen = 36;

                float anchoPagina = pagina.getMediaBox().getWidth();

                float anchoContenido = anchoPagina - margen * 2;

                double subtotal = alquiler.calcularSubtotal();

                double deposito = alquiler.getDepositoGarantia();

                double totalInicial = alquiler.getMontoPagado() == null ? alquiler.calcularTotal() : alquiler.getMontoPagado();

                boolean alquilerFinalizado = alquiler.getEstado() == EstadoAlquiler.FINALIZADO;

                LocalDate fechaDevolucion = alquilerFinalizado ? alquiler.getFechaDevolucionReal() : null;

                long diasAtraso = alquilerFinalizado ? alquiler.calcularDiasAtraso(fechaDevolucion) : 0;

                double multa = alquilerFinalizado ? alquiler.calcularMulta(fechaDevolucion) : 0;

                double depositoDevuelto = alquilerFinalizado ? alquiler.calcularDepositoADevolver(fechaDevolucion) : 0;

                double saldoPendiente = alquilerFinalizado ? alquiler.calcularSaldoPendiente(fechaDevolucion) : 0;

                double costoFinalAlquiler = subtotal + multa;

                String numeroFactura = String.format("%08d", alquiler.getNumeroAlquiler());

                String tituloFactura = alquilerFinalizado ? "FINAL DE ALQUILER" : "DE ALQUILER";

                /*
                 * Encabezado principal
                 */

                dibujarRectanguloRelleno(contenido, margen, 715, anchoContenido, 92, 10, 70, 145);

                /*
                 * Fondo blanco para el logo
                 */

                dibujarRectanguloRelleno(contenido, margen + 15, 730, 62, 62, 255, 255, 255);

                contenido.drawImage(logo, margen + 20, 735, 52, 52);

                escribirTextoColor(contenido, fuenteNegrita, 21, margen + 90, 774, "XANTIUM", 255, 255, 255);

                escribirTextoColor(contenido, fuenteNegrita, 8, margen + 91, 758, "RENT A CAR", 210, 230, 250);

                escribirTextoColor(contenido, fuenteNormal, 8, margen + 91, 742, "Sistema de Alquiler de Vehículos", 255, 255, 255);

                escribirTextoDerechaColor(contenido, fuenteNegrita, 22, anchoPagina - margen - 16, 776, "FACTURA", 255, 255, 255);

                escribirTextoDerechaColor(contenido, fuenteNegrita, 9, anchoPagina - margen - 16, 759, tituloFactura, 220, 235, 250);

                escribirTextoDerechaColor(contenido, fuenteNormal, 8, anchoPagina - margen - 16, 741, "Nº " + numeroFactura, 255, 255, 255);

                escribirTextoDerechaColor(contenido, fuenteNormal, 8, anchoPagina - margen - 16, 727, "Emisión: " + formatoFecha(LocalDate.now()), 255, 255, 255);

                /*
                 * Tarjetas de cliente y vehículo
                 */

                float separacionTarjetas = 10;

                float anchoTarjeta = (anchoContenido - separacionTarjetas) / 2;

                float xCliente = margen;

                float xVehiculo = margen + anchoTarjeta + separacionTarjetas;

                dibujarRectanguloRelleno(contenido, xCliente, 595, anchoTarjeta, 95, 245, 249, 253);

                dibujarRectanguloRelleno(contenido, xVehiculo, 595, anchoTarjeta, 95, 245, 249, 253);

                dibujarRectanguloRelleno(contenido, xCliente, 665, anchoTarjeta, 25, 225, 239, 250);

                dibujarRectanguloRelleno(contenido, xVehiculo, 665, anchoTarjeta, 25, 225, 239, 250);

                escribirTextoColor(contenido, fuenteNegrita, 11, xCliente + 12, 673, "CLIENTE", 10, 70, 145);

                escribirTextoColor(contenido, fuenteNegrita, 11, xVehiculo + 12, 673, "VEHÍCULO", 10, 70, 145);

                escribirTexto(contenido, fuenteNormal, 8, xCliente + 12, 648, "Nombre:");

                escribirTexto(contenido, fuenteNegrita, 8, xCliente + 70, 648, alquiler.getCliente().getNombreCompleto());

                escribirTexto(contenido, fuenteNormal, 8, xCliente + 12, 633, "Cédula:");

                escribirTexto(contenido, fuenteNegrita, 8, xCliente + 70, 633, alquiler.getCliente().getCedula());

                escribirTexto(contenido, fuenteNormal, 8, xCliente + 12, 618, "Teléfono:");

                escribirTexto(contenido, fuenteNegrita, 8, xCliente + 70, 618, alquiler.getCliente().getTelefono());

                escribirTexto(contenido, fuenteNormal, 8, xCliente + 12, 603, "Correo:");

                escribirTexto(contenido, fuenteNegrita, 7, xCliente + 70, 603, alquiler.getCliente().getCorreo());

                escribirTexto(contenido, fuenteNormal, 8, xVehiculo + 12, 648, "Marca:");

                escribirTexto(contenido, fuenteNegrita, 8, xVehiculo + 70, 648, alquiler.getVehiculo().getMarca());

                escribirTexto(contenido, fuenteNormal, 8, xVehiculo + 12, 633, "Modelo:");

                escribirTexto(contenido, fuenteNegrita, 8, xVehiculo + 70, 633, alquiler.getVehiculo().getModelo());

                escribirTexto(contenido, fuenteNormal, 8, xVehiculo + 12, 618, "Placa:");

                escribirTexto(contenido, fuenteNegrita, 8, xVehiculo + 70, 618, alquiler.getVehiculo().getPlaca());

                escribirTexto(contenido, fuenteNormal, 7.5f, xVehiculo + 12, 603, "Estado del alquiler:");

                escribirTextoColor(contenido, fuenteNegrita, 8, xVehiculo + 105, 603, alquiler.getEstado().toString(), 10, 90, 160);

                /*
                 * Detalles del alquiler
                 */

                dibujarRectanguloRelleno(contenido, margen, 492, anchoContenido, 87, 245, 249, 253);

                dibujarRectanguloRelleno(contenido, margen, 554, anchoContenido, 25, 225, 239, 250);

                escribirTextoColor(contenido, fuenteNegrita, 11, margen + 12, 562, "DETALLES DEL ALQUILER", 10, 70, 145);

                escribirTexto(contenido, fuenteNormal, 8, margen + 12, 536, "Fecha de inicio:");

                escribirTexto(contenido, fuenteNegrita, 8, margen + 100, 536, formatoFecha(alquiler.getFechaInicio()));

                escribirTexto(contenido, fuenteNormal, 8, margen + 12, 518, "Fecha esperada:");

                escribirTexto(contenido, fuenteNegrita, 8, margen + 100, 518, formatoFecha(alquiler.getFechaDevolucionEsperada()));

                if (alquilerFinalizado) {

                    escribirTexto(contenido, fuenteNormal, 8, margen + 12, 500, "Fecha de devolución:");

                    escribirTexto(contenido, fuenteNegrita, 8, margen + 100, 500, formatoFecha(fechaDevolucion));

                }

                escribirTexto(contenido, fuenteNormal, 8, margen + 265, 536, "Cantidad de días:");

                escribirTexto(contenido, fuenteNegrita, 8, margen + 355, 536, String.valueOf(alquiler.getCantidadDias()));

                escribirTexto(contenido, fuenteNormal, 8, margen + 265, 518, "Tarifa diaria:");

                escribirTexto(contenido, fuenteNegrita, 8, margen + 355, 518, formatoDinero(alquiler.getTarifaDiariaAplicada()));

                /*
                 * Tabla económica
                 */

                float xDescripcion = margen;

                float anchoDescripcion = 245;

                float xCantidad = xDescripcion + anchoDescripcion;

                float anchoCantidad = 75;

                float xPrecio = xCantidad + anchoCantidad;

                float anchoPrecio = 100;

                float xImporte = xPrecio + anchoPrecio;

                float anchoImporte = anchoContenido - anchoDescripcion - anchoCantidad - anchoPrecio;

                dibujarRectanguloRelleno(contenido, margen, 453, anchoContenido, 27, 10, 70, 145);

                escribirTextoColor(contenido, fuenteNegrita, 8, xDescripcion + 10, 463, "DESCRIPCIÓN", 255, 255, 255);

                escribirTextoColor(contenido, fuenteNegrita, 8, xCantidad + 13, 463, "CANTIDAD", 255, 255, 255);

                escribirTextoColor(contenido, fuenteNegrita, 8, xPrecio + 10, 463, "PRECIO", 255, 255, 255);

                escribirTextoColor(contenido, fuenteNegrita, 8, xImporte + 10, 463, "IMPORTE", 255, 255, 255);

                dibujarRectanguloRelleno(contenido, margen, 425, anchoContenido, 28, 250, 252, 254);

                escribirTexto(contenido, fuenteNormal, 8, xDescripcion + 10, 435, "Alquiler " + alquiler.getVehiculo().getMarca() + " " + alquiler.getVehiculo().getModelo());

                escribirTexto(contenido, fuenteNormal, 8, xCantidad + 18, 435, alquiler.getCantidadDias() + " días");

                escribirTextoDerecha(contenido, fuenteNormal, 8, xPrecio + anchoPrecio - 10, 435, formatoDinero(alquiler.getTarifaDiariaAplicada()));

                escribirTextoDerecha(contenido, fuenteNegrita, 8, xImporte + anchoImporte - 10, 435, formatoDinero(subtotal));

                dibujarRectanguloRelleno(contenido, margen, 397, anchoContenido, 28, 245, 249, 253);

                escribirTexto(contenido, fuenteNormal, 8, xDescripcion + 10, 407, "Depósito de garantía");

                escribirTexto(contenido, fuenteNormal, 8, xCantidad + 30, 407, "1");

                escribirTextoDerecha(contenido, fuenteNormal, 8, xPrecio + anchoPrecio - 10, 407, formatoDinero(deposito));

                escribirTextoDerecha(contenido, fuenteNegrita, 8, xImporte + anchoImporte - 10, 407, formatoDinero(deposito));

                dibujarLinea(contenido, xCantidad, 397, xCantidad, 480, 210, 220, 230);

                dibujarLinea(contenido, xPrecio, 397, xPrecio, 480, 210, 220, 230);

                dibujarLinea(contenido, xImporte, 397, xImporte, 480, 210, 220, 230);

                /*
                 * Pago inicial
                 */

                float yTarjetasPago = 250;

                float altoTarjetasPago = 128;

                if (alquilerFinalizado) {

                    /*
                     * Pago inicial en alquiler finalizado
                     */

                    dibujarRectanguloRelleno(contenido, xCliente, yTarjetasPago, anchoTarjeta, altoTarjetasPago, 245, 249, 253);

                    dibujarRectanguloRelleno(contenido, xCliente, yTarjetasPago + altoTarjetasPago - 25, anchoTarjeta, 25, 225, 239, 250);

                    escribirTextoColor(contenido, fuenteNegrita, 11, xCliente + 12, yTarjetasPago + altoTarjetasPago - 17, "PAGO INICIAL", 10, 70, 145);

                    escribirTexto(contenido, fuenteNormal, 8, xCliente + 12, yTarjetasPago + 82, "Método:");

                    escribirTextoDerecha(contenido, fuenteNegrita, 8, xCliente + anchoTarjeta - 12, yTarjetasPago + 82, alquiler.getMetodoPago() == null ? "No registrado" : alquiler.getMetodoPago().toString());

                    escribirTexto(contenido, fuenteNormal, 8, xCliente + 12, yTarjetasPago + 64, "Monto pagado:");

                    escribirTextoDerecha(contenido, fuenteNegrita, 8, xCliente + anchoTarjeta - 12, yTarjetasPago + 64, formatoDinero(totalInicial));

                    if (alquiler.getMetodoPago() == MetodoPago.EFECTIVO) {

                        escribirTexto(contenido, fuenteNormal, 8, xCliente + 12, yTarjetasPago + 46, "Monto recibido:");

                        escribirTextoDerecha(contenido, fuenteNegrita, 8, xCliente + anchoTarjeta - 12, yTarjetasPago + 46, formatoDinero(alquiler.getMontoRecibido()));

                        escribirTexto(contenido, fuenteNormal, 8, xCliente + 12, yTarjetasPago + 28, "Vuelto:");

                        escribirTextoDerecha(contenido, fuenteNegrita, 8, xCliente + anchoTarjeta - 12, yTarjetasPago + 28, formatoDinero(alquiler.getVuelto()));

                    }

                    /*
                     * Devolución
                     */

                    dibujarRectanguloRelleno(contenido, xVehiculo, yTarjetasPago, anchoTarjeta, altoTarjetasPago, 245, 249, 253);

                    dibujarRectanguloRelleno(contenido, xVehiculo, yTarjetasPago + altoTarjetasPago - 25, anchoTarjeta, 25, 225, 239, 250);

                    escribirTextoColor(contenido, fuenteNegrita, 11, xVehiculo + 12, yTarjetasPago + altoTarjetasPago - 17, "DEVOLUCIÓN", 10, 70, 145);

                    escribirTexto(contenido, fuenteNormal, 7, xVehiculo + 12, yTarjetasPago + 82, "Fecha real:");

                    escribirTextoDerecha(contenido, fuenteNegrita, 7, xVehiculo + anchoTarjeta - 12, yTarjetasPago + 82, formatoFecha(fechaDevolucion));

                    escribirTexto(contenido, fuenteNormal, 7, xVehiculo + 12, yTarjetasPago + 67, "Días de atraso:");

                    escribirTextoDerecha(contenido, fuenteNegrita, 7, xVehiculo + anchoTarjeta - 12, yTarjetasPago + 67, String.valueOf(diasAtraso));

                    escribirTexto(contenido, fuenteNormal, 7, xVehiculo + 12, yTarjetasPago + 52, "Multa:");

                    escribirTextoDerecha(contenido, fuenteNegrita, 7, xVehiculo + anchoTarjeta - 12, yTarjetasPago + 52, formatoDinero(multa));

                    escribirTexto(contenido, fuenteNormal, 7, xVehiculo + 12, yTarjetasPago + 37, "Depósito devuelto:");

                    escribirTextoDerecha(contenido, fuenteNegrita, 7, xVehiculo + anchoTarjeta - 12, yTarjetasPago + 37, formatoDinero(depositoDevuelto));

                    escribirTexto(contenido, fuenteNegrita, 7, xVehiculo + 12, yTarjetasPago + 22, "Saldo adicional:");

                    escribirTextoDerechaColor(contenido, fuenteNegrita, 8, xVehiculo + anchoTarjeta - 12, yTarjetasPago + 22, formatoDinero(saldoPendiente), 10, 90, 160);

                    if (alquiler.getMetodoPagoDevolucion() != null) {

                        escribirTexto(contenido, fuenteNormal, 6.5f, xVehiculo + 12, yTarjetasPago + 8, "Pago adicional realizado con: " + alquiler.getMetodoPagoDevolucion());

                    }

                } else {

                    /*
                     * Pago inicial en alquiler activo
                     */

                    dibujarRectanguloRelleno(contenido, margen, yTarjetasPago, anchoContenido, altoTarjetasPago, 245, 249, 253);

                    dibujarRectanguloRelleno(contenido, margen, yTarjetasPago + altoTarjetasPago - 25, anchoContenido, 25, 225, 239, 250);

                    escribirTextoColor(contenido, fuenteNegrita, 11, margen + 12, yTarjetasPago + altoTarjetasPago - 17, "PAGO INICIAL", 10, 70, 145);

                    escribirTexto(contenido, fuenteNormal, 8, margen + 12, yTarjetasPago + 82, "Método de pago:");

                    escribirTextoDerecha(contenido, fuenteNegrita, 8, margen + anchoContenido / 2 - 20, yTarjetasPago + 82, alquiler.getMetodoPago() == null ? "No registrado" : alquiler.getMetodoPago().toString());

                    escribirTexto(contenido, fuenteNormal, 8, margen + 285, yTarjetasPago + 82, "Monto pagado:");

                    escribirTextoDerecha(contenido, fuenteNegrita, 8, margen + anchoContenido - 12, yTarjetasPago + 82, formatoDinero(totalInicial));

                    if (alquiler.getMetodoPago() == MetodoPago.EFECTIVO) {

                        escribirTexto(contenido, fuenteNormal, 8, margen + 12, yTarjetasPago + 58, "Monto recibido:");

                        escribirTextoDerecha(contenido, fuenteNegrita, 8, margen + anchoContenido / 2 - 20, yTarjetasPago + 58, formatoDinero(alquiler.getMontoRecibido()));

                        escribirTexto(contenido, fuenteNormal, 8, margen + 285, yTarjetasPago + 58, "Vuelto:");

                        escribirTextoDerecha(contenido, fuenteNegrita, 8, margen + anchoContenido - 12, yTarjetasPago + 58, formatoDinero(alquiler.getVuelto()));

                    }

                    escribirTextoColor(contenido, fuenteNegrita, 8, margen + 12, yTarjetasPago + 28, "ALQUILER ACTIVO", 10, 90, 160);

                    escribirTexto(contenido, fuenteNormal, 8, margen + 100, yTarjetasPago + 28, "Pendiente de devolución");

                }

                /*
                 * Resumen económico
                 */

                escribirTextoColor(contenido, fuenteNegrita, 9, margen, 221, "RESUMEN ECONÓMICO", 10, 70, 145);

                dibujarLinea(contenido, margen, 214, margen + anchoContenido, 214, 180, 200, 220);

                if (alquilerFinalizado) {

                    /*
                     * Resumen de alquiler finalizado
                     */

                    dibujarRectanguloRelleno(contenido, margen, 105, anchoContenido / 2 - 12, 84, 10, 70, 145);

                    escribirTextoColor(contenido, fuenteNormal, 9, margen + 15, 167, "COSTO FINAL DEL ALQUILER", 220, 235, 250);

                    escribirTextoColor(contenido, fuenteNegrita, 18, margen + 15, 140, formatoDinero(costoFinalAlquiler), 255, 255, 255);

                    escribirTextoColor(contenido, fuenteNormal, 7, margen + 15, 120, "Costo real del servicio después de liquidar el depósito", 220, 235, 250);

                    float xResumen = margen + anchoContenido / 2;

                    /*
                     * Liquidación del pago
                     */

                    escribirTextoColor(contenido, fuenteNegrita, 7, xResumen, 194, "LIQUIDACIÓN DEL PAGO", 10, 70, 145);

                    escribirTexto(contenido, fuenteNormal, 6.5f, xResumen, 176, "El depósito se descuenta del pago inicial cuando es devuelto");

                    /*
                     * Movimientos del pago
                     */

                    escribirTextoColor(contenido, fuenteNegrita, 7, xResumen, 143, "MOVIMIENTOS DEL PAGO", 10, 70, 145);

                    escribirTexto(contenido, fuenteNormal, 7.5f, xResumen, 127, "Pago inicial recibido");

                    escribirTextoDerecha(contenido, fuenteNegrita, 7.5f, margen + anchoContenido, 127, formatoDinero(totalInicial));

                    escribirTexto(contenido, fuenteNormal, 7.5f, xResumen, 111, "Depósito devuelto");

                    if (depositoDevuelto > 0) {

                        escribirTextoDerechaColor(contenido, fuenteNegrita, 7.5f, margen + anchoContenido, 111, formatoDinero(depositoDevuelto), 35, 140, 70);

                    } else {

                        escribirTextoDerecha(contenido, fuenteNegrita, 7.5f, margen + anchoContenido, 111, formatoDinero(depositoDevuelto));

                    }

                    escribirTexto(contenido, fuenteNormal, 7.5f, xResumen, 95, "Pago adicional");

                    if (saldoPendiente > 0) {

                        escribirTextoDerechaColor(contenido, fuenteNegrita, 7.5f, margen + anchoContenido, 95, formatoDinero(saldoPendiente), 190, 45, 45);

                    } else {

                        escribirTextoDerecha(contenido, fuenteNegrita, 7.5f, margen + anchoContenido, 95, formatoDinero(saldoPendiente));

                    }

                } else {

                    /*
                     * Resumen de alquiler activo
                     */

                    dibujarRectanguloRelleno(contenido, margen, 105, anchoContenido / 2 - 12, 84, 10, 70, 145);

                    escribirTextoColor(contenido, fuenteNormal, 9, margen + 15, 167, "TOTAL PAGADO INICIAL", 220, 235, 250);

                    escribirTextoColor(contenido, fuenteNegrita, 18, margen + 15, 140, formatoDinero(totalInicial), 255, 255, 255);

                    escribirTextoColor(contenido, fuenteNormal, 7, margen + 15, 120, "Alquiler actualmente activo", 220, 235, 250);

                    float xResumen = margen + anchoContenido / 2;

                    escribirTexto(contenido, fuenteNormal, 8, xResumen, 181, "Subtotal del alquiler");

                    escribirTextoDerecha(contenido, fuenteNegrita, 8, margen + anchoContenido, 181, formatoDinero(subtotal));

                    escribirTexto(contenido, fuenteNormal, 8, xResumen, 159, "Depósito de garantía");

                    escribirTextoDerecha(contenido, fuenteNegrita, 8, margen + anchoContenido, 159, formatoDinero(deposito));

                    escribirTexto(contenido, fuenteNormal, 8, xResumen, 137, "Total pagado");

                    escribirTextoDerechaColor(contenido, fuenteNegrita, 9, margen + anchoContenido, 137, formatoDinero(totalInicial), 10, 90, 160);

                    escribirTexto(contenido, fuenteNormal, 7, xResumen, 115, "La devolución todavía no ha sido registrada");

                }

                /*
                 * Pie de página
                 */

                dibujarLinea(contenido, margen, 67, margen + 180, 67, 10, 70, 145);

                dibujarLinea(contenido, anchoPagina - margen - 180, 67, anchoPagina - margen, 67, 10, 70, 145);

                float anchoGracias = fuenteNegrita.getStringWidth("GRACIAS POR UTILIZAR XANTIUM") / 1000 * 8;

                escribirTextoColor(contenido, fuenteNegrita, 8, (anchoPagina - anchoGracias) / 2, 64, "GRACIAS POR UTILIZAR XANTIUM", 10, 70, 145);

                escribirTexto(contenido, fuenteNormal, 6.5f, margen, 43, "Factura generada por el Sistema de Alquiler de Vehículos XANTIUM");

                escribirTextoDerecha(contenido, fuenteNormal, 6.5f, margen + anchoContenido, 43, "Nº " + numeroFactura);

            }

            documento.save(archivo);

            return archivo;

        } catch (IOException e) {

            throw new RuntimeException("No se pudo generar la factura PDF.", e);

        }

    }

    /*
     * Qué hace: Escribe una línea de texto dentro del documento PDF utilizando la fuente, tamaño y posición indicados
     * Recibe: El flujo de contenido del PDF, la fuente, el tamaño de letra, las coordenadas X e Y y el texto que se desea escribir
     * Retorna: No retorna ningún valor
     */
    private void escribirTexto(PDPageContentStream contenido, PDFont fuente, float tamano, float x, float y, String texto) throws IOException {

        contenido.setNonStrokingColor(0f, 0f, 0f);

        contenido.beginText();

        contenido.setFont(fuente, tamano);

        contenido.newLineAtOffset(x, y);

        contenido.showText(texto);

        contenido.endText();

    }

    /*
     * Qué hace: Escribe un texto utilizando un color específico dentro del documento PDF convirtiendo los valores RGB de 0 a 255 al rango de 0 a 1 utilizado por PDFBox
     * Recibe: El flujo de contenido, la fuente, tamaño, posición, texto y valores RGB del color
     * Retorna: No retorna ningún valor
     */
    private void escribirTextoColor(PDPageContentStream contenido, PDFont fuente, float tamano, float x, float y, String texto, int rojo, int verde, int azul) throws IOException {

        contenido.setNonStrokingColor(rojo / 255f, verde / 255f, azul / 255f);

        contenido.beginText();

        contenido.setFont(fuente, tamano);

        contenido.newLineAtOffset(x, y);

        contenido.showText(texto);

        contenido.endText();

        contenido.setNonStrokingColor(0f, 0f, 0f);

    }

    /*
     * Qué hace: Escribe un texto alineado hacia la derecha tomando como referencia una posición horizontal determinada
     * Recibe: El flujo de contenido, la fuente, el tamaño, la posición derecha, la posición vertical y el texto
     * Retorna: No retorna ningún valor
     */
    private void escribirTextoDerecha(PDPageContentStream contenido, PDFont fuente, float tamano, float xDerecha, float y, String texto) throws IOException {

        float anchoTexto = fuente.getStringWidth(texto) / 1000 * tamano;

        escribirTexto(contenido, fuente, tamano, xDerecha - anchoTexto, y, texto);

    }

    /*
     * Qué hace: Escribe un texto de color alineado hacia la derecha tomando como referencia una posición horizontal determinada
     * Recibe: El flujo de contenido, la fuente, el tamaño, la posición derecha, la posición vertical, el texto y los valores RGB
     * Retorna: No retorna ningún valor
     */
    private void escribirTextoDerechaColor(PDPageContentStream contenido, PDFont fuente, float tamano, float xDerecha, float y, String texto, int rojo, int verde, int azul) throws IOException {

        float anchoTexto = fuente.getStringWidth(texto) / 1000 * tamano;

        escribirTextoColor(contenido, fuente, tamano, xDerecha - anchoTexto, y, texto, rojo, verde, azul);

    }

    /*
     * Qué hace: Dibuja un rectángulo completamente relleno utilizando un color RGB y convierte sus valores al rango requerido por PDFBox
     * Recibe: El flujo de contenido, la posición, el tamaño del rectángulo y los valores RGB del color
     * Retorna: No retorna ningún valor
     */
    private void dibujarRectanguloRelleno(PDPageContentStream contenido, float x, float y, float ancho, float alto, int rojo, int verde, int azul) throws IOException {

        contenido.setNonStrokingColor(rojo / 255f, verde / 255f, azul / 255f);

        contenido.addRect(x, y, ancho, alto);

        contenido.fill();

        contenido.setNonStrokingColor(0f, 0f, 0f);

    }

    /*
     * Qué hace: Dibuja una línea dentro del documento PDF utilizando un color RGB convertido al rango requerido por PDFBox
     * Recibe: El flujo de contenido, las coordenadas de inicio y fin y los valores RGB
     * Retorna: No retorna ningún valor
     */
    private void dibujarLinea(PDPageContentStream contenido, float xInicio, float yInicio, float xFin, float yFin, int rojo, int verde, int azul) throws IOException {

        contenido.setStrokingColor(rojo / 255f, verde / 255f, azul / 255f);

        contenido.setLineWidth(0.7f);

        contenido.moveTo(xInicio, yInicio);

        contenido.lineTo(xFin, yFin);

        contenido.stroke();

        contenido.setStrokingColor(0f, 0f, 0f);

    }

    /*
     * Qué hace: Dibuja una barra visual utilizada como encabezado para separar cada sección de la factura
     * Recibe: El flujo de contenido, la fuente en negrita, la posición, el ancho disponible y el título de la sección
     * Retorna: No retorna ningún valor
     */
    private void dibujarTituloSeccion(PDPageContentStream contenido, PDFont fuente, float x, float y, float ancho, String titulo) throws IOException {

        dibujarRectanguloRelleno(contenido, x, y - 7, ancho, 23, 235, 243, 249);

        escribirTextoColor(contenido, fuente, 10, x + 10, y, titulo, 20, 90, 150);

    }

    /*
     * Qué hace: Convierte una fecha al formato día, mes y año utilizado dentro de la factura
     * Recibe: La fecha que se desea mostrar
     * Retorna: La fecha convertida al formato dd/MM/yyyy o un guion cuando no existe
     */
    private String formatoFecha(LocalDate fecha) {

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
    private String formatoDinero(Double monto) {

        if (monto == null) {

            return "₡0.00";

        }

        return String.format("₡%,.2f", monto);

    }

}