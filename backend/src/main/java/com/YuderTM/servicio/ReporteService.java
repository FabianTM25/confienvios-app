
  package com.YuderTM.servicio;

import com.YuderTM.modelo.Rotulo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReporteService {

  private final DataSource dataSource;
  private final IRotuloService iRotuloService;

  public ReporteService(
    DataSource dataSource,
    IRotuloService iRotuloService
  ) {
    this.dataSource = dataSource;
    this.iRotuloService = iRotuloService;
  }

  // FACTURA
  public byte[] generarReporte(Integer id) throws Exception {

    InputStream reportStream =
      this.getClass()
        .getResourceAsStream("/reports/Factura.jasper");

    if (reportStream == null) {
      throw new RuntimeException(
        "No se encontró Factura.jasper"
      );
    }

    InputStream logoStream =
      this.getClass()
        .getResourceAsStream("/img/confienvios.png");

    InputStream logoStream2 =
      this.getClass()
        .getResourceAsStream("/img/confienvios2.png");

    Map<String, Object> params = new HashMap<>();

    params.put("idFactura", id);

    if (logoStream != null) {
      params.put("logoParam", logoStream);
    }

    if (logoStream2 != null) {
      params.put("logoParam2", logoStream2);
    }

    try (Connection connection =
           dataSource.getConnection()) {

      JasperPrint jasperPrint =
        JasperFillManager.fillReport(
          reportStream,
          params,
          connection
        );

      return JasperExportManager
        .exportReportToPdf(jasperPrint);
    }
  }

  // ROTULO
  public byte[] generarRotulo(Integer idRotulo)
    throws Exception {

    InputStream reportStream =
      this.getClass()
        .getResourceAsStream("/reports/Rotulo.jasper");

    if (reportStream == null) {
      throw new RuntimeException(
        "No se encontró Rotulo.jasper"
      );
    }

    Rotulo rotulo =
      iRotuloService.buscarPorId(idRotulo);

    if (rotulo == null) {
      throw new RuntimeException(
        "No existe el rótulo con ID: "
          + idRotulo
      );
    }

    List<Rotulo> lista = List.of(rotulo);

    JRBeanCollectionDataSource dataSourceBean =
      new JRBeanCollectionDataSource(lista);

    InputStream logoStream =
      this.getClass()
        .getResourceAsStream("/img/confienvios1.png");

    Map<String, Object> params = new HashMap<>();

    if (logoStream != null) {
      params.put("logoParam1", logoStream);
    }

    JasperPrint jasperPrint =
      JasperFillManager.fillReport(
        reportStream,
        params,
        dataSourceBean
      );

    return JasperExportManager
      .exportReportToPdf(jasperPrint);
  }
}

