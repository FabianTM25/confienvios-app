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

// Prueba de humo para materiales.jrxml (diseño propio, con caja de Observaciones),
// misma logica que CorrespondenciaReportTest: se llena con un JRDataSource explicito
// asi que la query embebida (SELECT * FROM material WHERE id_material = $P{idMaterial})
// no se ejecuta.
//
// IMPORTANTE: correr con JDK 17 (ver nota en VentaTicketReportTest).
class MaterialesReportTest {

    private static final String REPORTS_DIR = "src/main/resources/reports/";

    @Test
    void compilaYLlenaMateriales() throws Exception {

        JasperCompileManager.compileReportToFile(
            REPORTS_DIR + "materiales.jrxml",
            REPORTS_DIR + "materiales.jasper"
        );

        JasperReport report;
        try (InputStream jasperStream = new FileInputStream(REPORTS_DIR + "materiales.jasper")) {
            report = (JasperReport) JRLoader.loadObject(jasperStream);
        }

        Map<String, Object> fila = new HashMap<>();
        fila.put("id_factura", 1);
        fila.put("direccion_cliente_rmt", "Cra 45 sur N° 135-301");
        fila.put("documento_cliente_dto", "123456");
        fila.put("documento_cliente_rmt", "987654");
        fila.put("estado", "PENDIENTE");
        fila.put("estructura", "A");
        fila.put("id_cliente_dto", 1);
        fila.put("id_cliente_rmt", 1);
        fila.put("niu", "1208910");
        fila.put("nombre_cliente_dto", "Cliente Destino Prueba");
        fila.put("nombre_cliente_rmt", "Cliente Remite Prueba");
        fila.put("numero_factura", "MAT-0001");
        fila.put("pabellon", "7");
        fila.put("td", "502314233");
        fila.put("telefono_cliente_rmt", "3000000000");
        fila.put("fecha_creacion", new Timestamp(System.currentTimeMillis()));
        fila.put("tipo_documento_rmt", "CC");
        fila.put("tipo_documento_dto", "CC");
        fila.put("observacion", "Materiales de carpintería");

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
