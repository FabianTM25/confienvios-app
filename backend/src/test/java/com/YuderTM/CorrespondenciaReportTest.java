package com.YuderTM;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Prueba de humo, sin contexto de Spring ni base de datos: valida que
// correspondencia.jrxml compile y se pueda llenar/exportar a PDF sin
// errores. El reporte trae su propio <query> SQL contra la tabla
// "factura", pero como aca se le pasa un JRDataSource explicito
// (JRMapCollectionDataSource), esa query no se ejecuta: los datos de
// prueba salen del mapa de abajo, no de una conexion real.
//
// IMPORTANTE: correr con JDK 17 (ver nota en VentaTicketReportTest) para
// que el .jasper generado sea compatible con la imagen Docker (Java 17).
class CorrespondenciaReportTest {

    private static final String REPORTS_DIR = "src/main/resources/reports/";

    @Test
    void compilaYLlenaCorrespondencia() throws Exception {

        JasperCompileManager.compileReportToFile(
            REPORTS_DIR + "correspondencia.jrxml",
            REPORTS_DIR + "correspondencia.jasper"
        );

        JasperReport report;
        try (InputStream jasperStream = new FileInputStream(REPORTS_DIR + "correspondencia.jasper")) {
            report = (JasperReport) JRLoader.loadObject(jasperStream);
        }

        Map<String, Object> fila = new HashMap<>();
        fila.put("id_factura", 1);
        fila.put("direccion_cliente_rmt", "Cra 45 sur N° 135-301");
        fila.put("documento_cliente_dto", "123456");
        fila.put("documento_cliente_rmt", "987654");
        fila.put("estado", "1");
        fila.put("estructura", "A");
        fila.put("id_cliente_dto", 1);
        fila.put("id_cliente_rmt", 1);
        fila.put("niu", "1208910");
        fila.put("nombre_cliente_dto", "Cliente Destino Prueba");
        fila.put("nombre_cliente_rmt", "Cliente Remite Prueba");
        fila.put("numero_factura", "FAC-0001");
        fila.put("pabellon", "7");
        fila.put("td", "502314233");
        fila.put("telefono_cliente_rmt", "3000000000");
        fila.put("fecha_creacion", new Timestamp(System.currentTimeMillis()));
        fila.put("tipo_documento_rmt", "CC");
        fila.put("tipo_documento_dto", "CC");

        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(List.of(fila));

        Map<String, Object> params = new HashMap<>();
        InputStream logo1 = getClass().getResourceAsStream("/img/confienvios.png");
        InputStream logo2 = getClass().getResourceAsStream("/img/correspondencia.png");
        if (logo1 != null) params.put("logoParam", logo1);
        if (logo2 != null) params.put("logoParam1", logo2);

        JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, dataSource);

        byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }
}
