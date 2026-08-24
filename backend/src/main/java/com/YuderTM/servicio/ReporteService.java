
  package com.YuderTM.servicio;

import com.YuderTM.modelo.Documento;
import com.YuderTM.modelo.Material;
import com.YuderTM.modelo.Rotulo;
import com.YuderTM.modelo.Venta;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReporteService {

  private final DataSource dataSource;
  private final IRotuloService iRotuloService;
  private final IVentaService iVentaService;
  private final IDocumentoService iDocumentoService;
  private final IMaterialService iMaterialService;

  public ReporteService(
    DataSource dataSource,
    IRotuloService iRotuloService,
    IVentaService iVentaService,
    IDocumentoService iDocumentoService,
    IMaterialService iMaterialService
  ) {
    this.dataSource = dataSource;
    this.iRotuloService = iRotuloService;
    this.iVentaService = iVentaService;
    this.iDocumentoService = iDocumentoService;
    this.iMaterialService = iMaterialService;
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

  // CORRESPONDENCIA
  public byte[] generarCorrespondencia(Integer id) throws Exception {

    InputStream reportStream =
      this.getClass()
        .getResourceAsStream("/reports/correspondencia.jasper");

    if (reportStream == null) {
      throw new RuntimeException(
        "No se encontró correspondencia.jasper"
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

    JasperPrint jasperPrint =
      JasperFillManager.fillReport(
        reportStream,
        new HashMap<>(),
        dataSourceBean
      );

    return JasperExportManager
      .exportReportToPdf(jasperPrint);
  }

  // DOCUMENTO (correspondencia del modulo Correspondencia y Materiales, usando el
  // diseño correspondencia.jasper del usuario). El .jasper trae su propia query SQL
  // contra "factura", pero como aca se le pasa un JRDataSource explicito esa query
  // no se ejecuta: los datos salen del Documento, mapeados a los nombres de campo
  // que espera el reporte (numero_factura = numero_radicado, estado = estado_envio, etc).
  public byte[] generarDocumento(Integer idDocumento)
    throws Exception {

    InputStream reportStream =
      this.getClass()
        .getResourceAsStream("/reports/correspondencia.jasper");

    if (reportStream == null) {
      throw new RuntimeException(
        "No se encontró correspondencia.jasper"
      );
    }

    Documento documento =
      iDocumentoService.buscarDocumentoId(idDocumento);

    if (documento == null) {
      throw new RuntimeException(
        "No existe el documento con ID: "
          + idDocumento
      );
    }

    Map<String, Object> fila = new HashMap<>();
    fila.put("id_factura", documento.getId_documento());
    fila.put("direccion_cliente_rmt", documento.getDireccion_cliente_rmt());
    fila.put("documento_cliente_dto", documento.getDocumento_cliente_dto());
    fila.put("documento_cliente_rmt", documento.getDocumento_cliente_rmt());
    fila.put("estado", documento.getEstado_envio());
    fila.put("estructura", documento.getEstructura());
    fila.put("id_cliente_dto", null);
    fila.put("id_cliente_rmt", null);
    fila.put("niu", documento.getNiu());
    fila.put("nombre_cliente_dto", documento.getNombre_cliente_dto());
    fila.put("nombre_cliente_rmt", documento.getNombre_cliente_rmt());
    fila.put("numero_factura", documento.getNumero_radicado());
    fila.put("pabellon", documento.getPabellon());
    fila.put("td", documento.getTd());
    fila.put("telefono_cliente_rmt", documento.getTelefono_cliente_rmt());
    fila.put(
      "fecha_creacion",
      documento.getFecha_creacion() != null ? Timestamp.valueOf(documento.getFecha_creacion()) : null
    );
    fila.put("tipo_documento_rmt", documento.getTipo_documento_rmt());
    fila.put("tipo_documento_dto", documento.getTipo_documento_dto());

    JRMapCollectionDataSource dataSourceMap =
      new JRMapCollectionDataSource(List.of(fila));

    InputStream logoStream =
      this.getClass()
        .getResourceAsStream("/img/confienvios.png");
    InputStream logoStream2 =
      this.getClass()
        .getResourceAsStream("/img/confienvios2.png");

    Map<String, Object> params = new HashMap<>();

    if (logoStream != null) {
      params.put("logoParam", logoStream);
    }

    if (logoStream2 != null) {
      params.put("logoParam1", logoStream2);
    }

    JasperPrint jasperPrint =
      JasperFillManager.fillReport(
        reportStream,
        params,
        dataSourceMap
      );

    return JasperExportManager
      .exportReportToPdf(jasperPrint);
  }

  // MATERIAL (mismo mecanismo que generarDocumento, pero con el diseño materiales.jasper del usuario)
  public byte[] generarMaterial(Integer idMaterial)
    throws Exception {

    InputStream reportStream =
      this.getClass()
        .getResourceAsStream("/reports/materiales.jasper");

    if (reportStream == null) {
      throw new RuntimeException(
        "No se encontró materiales.jasper"
      );
    }

    Material material =
      iMaterialService.buscarMaterialId(idMaterial);

    if (material == null) {
      throw new RuntimeException(
        "No existe el material con ID: "
          + idMaterial
      );
    }

    Map<String, Object> fila = new HashMap<>();
    fila.put("id_factura", material.getId_material());
    fila.put("direccion_cliente_rmt", material.getDireccion_cliente_rmt());
    fila.put("documento_cliente_dto", material.getDocumento_cliente_dto());
    fila.put("documento_cliente_rmt", material.getDocumento_cliente_rmt());
    fila.put("estado", material.getEstado_envio());
    fila.put("estructura", material.getEstructura());
    fila.put("id_cliente_dto", null);
    fila.put("id_cliente_rmt", null);
    fila.put("niu", material.getNiu());
    fila.put("nombre_cliente_dto", material.getNombre_cliente_dto());
    fila.put("nombre_cliente_rmt", material.getNombre_cliente_rmt());
    fila.put("numero_factura", material.getNumero_radicado());
    fila.put("pabellon", material.getPabellon());
    fila.put("td", material.getTd());
    fila.put("telefono_cliente_rmt", material.getTelefono_cliente_rmt());
    fila.put(
      "fecha_creacion",
      material.getFecha_creacion() != null ? Timestamp.valueOf(material.getFecha_creacion()) : null
    );
    fila.put("tipo_documento_rmt", material.getTipo_documento_rmt());
    fila.put("tipo_documento_dto", material.getTipo_documento_dto());
    fila.put("observacion", material.getObservacion());

    JRMapCollectionDataSource dataSourceMap =
      new JRMapCollectionDataSource(List.of(fila));

    InputStream logoStream =
      this.getClass()
        .getResourceAsStream("/img/confienvios.png");
    InputStream logoStream2 =
      this.getClass()
        .getResourceAsStream("/img/confienvios2.png");

    Map<String, Object> params = new HashMap<>();

    if (logoStream != null) {
      params.put("logoParam", logoStream);
    }

    if (logoStream2 != null) {
      params.put("logoParam1", logoStream2);
    }

    JasperPrint jasperPrint =
      JasperFillManager.fillReport(
        reportStream,
        params,
        dataSourceMap
      );

    return JasperExportManager
      .exportReportToPdf(jasperPrint);
  }
}

