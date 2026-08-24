package com.YuderTM;

import com.YuderTM.modelo.Venta;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Prueba de humo, sin contexto de Spring: valida que VentaTicket.jrxml
// compile y se pueda llenar/exportar a PDF sin errores. Ademas deja
// escrito el .jasper precompilado junto al .jrxml (mismo patron que
// Factura1.jasper / Rotulo1.jasper), porque compilar el .jrxml en
// tiempo de ejecucion no funciona dentro del jar ejecutable de Spring Boot.
//
// IMPORTANTE: correr este test con JDK 17 (JAVA_HOME apuntando a un JDK 17),
// no con una version mas nueva. El compilador interno de JasperReports genera
// el bytecode de las expresiones del reporte con la version del JDK que
// ejecuta el test, y la imagen Docker corre sobre Java 17 (eclipse-temurin:17-jdk):
// si se compila con un JDK mas nuevo, el .jasper falla en el contenedor con
// "UnsupportedClassVersionError" al intentar llenarlo.
class VentaTicketReportTest {

    private static final String REPORTS_DIR = "src/main/resources/reports/";

    @Test
    void compilaYLlenaVentaTicket() throws Exception {

        JasperCompileManager.compileReportToFile(REPORTS_DIR + "VentaTicket.jrxml", REPORTS_DIR + "VentaTicket.jasper");
        JasperCompileManager.compileReportToFile(REPORTS_DIR + "Documento.jrxml", REPORTS_DIR + "Documento.jasper");

        // Verifica el .jasper ya precompilado, tal como lo carga ReporteService en produccion
        JasperReport report;
        try (InputStream jasperStream = new FileInputStream(REPORTS_DIR + "VentaTicket.jasper")) {
            report = (JasperReport) net.sf.jasperreports.engine.util.JRLoader.loadObject(jasperStream);
        }

        Venta venta = new Venta();
        venta.setId_venta(1);
        venta.setNumero_venta("VEN-0001");
        venta.setNumero_factura("FAC-0001");
        venta.setNombre_cliente_dto("Cliente Destino Prueba");
        venta.setDocumento_cliente_dto("123456");
        venta.setNombre_cliente_rmt("Cliente Remite Prueba");
        venta.setTipo_documento_rmt("CC");
        venta.setDocumento_cliente_rmt("987654");
        venta.setTelefono_cliente_rmt("3000000000");
        venta.setPeso(new BigDecimal("2.5"));
        venta.setValor_peso(new BigDecimal("5000"));
        venta.setCon_caja(true);
        venta.setPrecio_caja_aplicado(new BigDecimal("1000"));
        venta.setValor_envio(new BigDecimal("6000"));
        venta.setObservaciones("Observación de prueba");
        venta.setValor_avaluo(new BigDecimal("50000"));
        venta.setVendedor("Vendedor Prueba");
        venta.setForma_pago("Efectivo");
        venta.setEstado("1");
        venta.setFecha_creacion(LocalDateTime.now());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(List.of(venta));

        JasperPrint jasperPrint = JasperFillManager.fillReport(report, new HashMap<>(), dataSource);

        byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }
}
