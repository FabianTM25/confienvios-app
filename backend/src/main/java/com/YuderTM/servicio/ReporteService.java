package com.YuderTM.servicio;

import com.YuderTM.modelo.Rotulo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import java.util.List;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReporteService {

    private final DataSource dataSource;
    private final IRotuloService iRotuloService;

    // 🔥 Constructor con ambas dependencias
    public ReporteService(DataSource dataSource, IRotuloService iRotuloService) {
        this.dataSource = dataSource;
        this.iRotuloService = iRotuloService;
    }

    public byte[] generarReporte(Integer id) throws Exception {

        try{
        // 1. Cargar el archivo del reporte (.jasper)
        InputStream reportStream = this.getClass().getResourceAsStream("/reports/factura.jasper");

        if (reportStream == null) {
            throw new RuntimeException("No se encontró el reporte: " + id);
        }

        // 2. CARGAR EL LOGO
        // Asegúrate de que la ruta sea /img/confienvios.png dentro de src/main/resources
        InputStream logoStream = this.getClass().getResourceAsStream("/img/confienvios.png");
        InputStream logoStream1 = this.getClass().getResourceAsStream("/img/confienvios1.png"); // Tu nueva imagen

        Map<String, Object> params = new HashMap<>();

        // El ID va como parámetro para filtrar la factura correcta
        params.put("idFactura", id);

        // Solo agregamos el parámetro si la imagen existe para evitar errores
        if (logoStream != null) {
            // "logoParam" debe ser el nombre exacto del parámetro en JasperSoft Studio
            params.put("logoParam", logoStream);

        }
        // Pasar el segundo logo (logoParam1)
        if (logoStream1 != null) {
            params.put("logoParam1", logoStream1); // Esta llave debe existir en Jasper
        }else {
            System.out.println("ADVERTENCIA: No se encontró la imagen en /img/confienvios.png");
        }

        // 3. Llenar el reporte usando la conexión del DataSource
        // Usamos un bloque try-with-resources para asegurar que la conexión se cierre
        try (Connection connection = dataSource.getConnection()) {
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, params, connection);

            // 4. Exportar a PDF
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }
    }catch (Exception e) {
    e.printStackTrace(); // 👈 IMPORTANTE
    throw e;
}
}

    //rotulo

    public byte[] generarRotulo(Integer idRotulo) throws Exception {
        try{

        // 1. Cargar el .jasper (NO SE COMPILA)
        InputStream reportStream = this.getClass().getResourceAsStream("/reports/rotulo.jasper");

        if (reportStream == null) {
            throw new RuntimeException("No se encontró rotulo.jasper");
        }

        // 2. Obtener datos
        Rotulo rotulo = iRotuloService.buscarPorId(idRotulo);

        if (rotulo == null) {
            throw new RuntimeException("No existe el rótulo con ID: " + idRotulo);
        }

        List<Rotulo> lista = List.of(rotulo);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);

        // 3. LOGOS
        InputStream logoStream1 = this.getClass().getResourceAsStream("/img/confienvios1.png");

        Map<String, Object> params = new HashMap<>();

        if (logoStream1 != null) {
            params.put("logoParam1", logoStream1);
        }

        // 4. Llenar reporte
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, params, new JREmptyDataSource());
        //JasperFillManager.fillReport(reportStream, params, dataSource);

        // 5. Exportar PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    catch (Exception e) {
        e.printStackTrace(); // 🔥 CLAVE
        throw new RuntimeException("Error generando rótulo: " + e.getMessage());
    }
}

}