
  package com.YuderTM.servicio;

import com.YuderTM.modelo.Documento;
import com.YuderTM.modelo.Rotulo;
import com.YuderTM.modelo.Venta;
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
  private final IVentaService iVentaService;
  private final IDocumentoService iDocumentoService;

  private JasperReport documentoReportCompilado;

  public ReporteService(
    DataSource dataSource,
    IRotuloService iRotuloService,
    IVentaService iVentaService,
    IDocumentoService iDocumentoService
  ) {
    this.dataSource = dataSource;
    this.iRotuloService = iRotuloService;
    this.iVentaService = iVentaService;
    this.iDocumentoService = iDocumentoService;
  }

  // FACTURA
  public byte[] generarReporte(Integer id) throws Exception {

    InputStream reportStream =
      this.getClass()
        .getResourceAsStream("/reports/Factura1.jasper");

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
      params.put("logoParam1", logoStream2);
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
        .getResourceAsStream("/reports/Rotulo1.jasper");


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
        .getResourceAsStream("/img/confienvios2.png");
    InputStream logoStream2 =
      this.getClass()
        .getResourceAsStream("/img/confienvios1.png");

    Map<String, Object> params = new HashMap<>();

    if (logoStream != null) {
      params.put("logoParam1", logoStream);
    }

    if (logoStream2 != null) {
      params.put("logoParam2", logoStream2);
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

  // TICKET DE VENTA
  public byte[] generarTicketVenta(Integer idVenta)
    throws Exception {

    InputStream reportStream =
      this.getClass()
        .getResourceAsStream("/reports/VentaTicket.jasper");

    if (reportStream == null) {
      throw new RuntimeException(
        "No se encontró VentaTicket.jasper"
      );
    }

    Venta venta =
      iVentaService.buscarVentaId(idVenta);

    if (venta == null) {
      throw new RuntimeException(
        "No existe la venta con ID: "
          + idVenta
      );
    }

    List<Venta> lista = List.of(venta);

    JRBeanCollectionDataSource dataSourceBean =
      new JRBeanCollectionDataSource(lista);

    InputStream logoStream =
      this.getClass()
        .getResourceAsStream("/img/confienvios2.png");
    InputStream logoStream2 =
      this.getClass()
        .getResourceAsStream("/img/confienvios1.png");

    Map<String, Object> params = new HashMap<>();

    if (logoStream != null) {
      params.put("logoParam1", logoStream);
    }

    if (logoStream2 != null) {
      params.put("logoParam2", logoStream2);
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

  // DOCUMENTO
  public byte[] generarDocumento(Integer idDocumento)
    throws Exception {

    Documento documento =
      iDocumentoService.buscarDocumentoId(idDocumento);

    if (documento == null) {
      throw new RuntimeException(
        "No existe el documento con ID: "
          + idDocumento
      );
    }

    if (documentoReportCompilado == null) {
      try (InputStream jrxmlStream =
             this.getClass()
               .getResourceAsStream("/reports/Documento.jrxml")) {

        if (jrxmlStream == null) {
          throw new RuntimeException(
            "No se encontró Documento.jrxml"
          );
        }

        documentoReportCompilado =
          JasperCompileManager.compileReport(jrxmlStream);
      }
    }

    List<Documento> lista = List.of(documento);

    JRBeanCollectionDataSource dataSourceBean =
      new JRBeanCollectionDataSource(lista);

    JasperPrint jasperPrint =
      JasperFillManager.fillReport(
        documentoReportCompilado,
        new HashMap<>(),
        dataSourceBean
      );

    return JasperExportManager
      .exportReportToPdf(jasperPrint);
  }
}

