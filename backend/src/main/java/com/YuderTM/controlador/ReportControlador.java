package com.YuderTM.controlador;

import com.YuderTM.servicio.ReporteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {
  "http://localhost:4200",
  "http://localhost:64764",
  "https://confienvios-app-two.vercel.app",
  "https://confienvios-app.vercel.app"

})

public class ReportControlador {

  private final ReporteService reporteService;

  public ReportControlador(ReporteService reporteService) {
    this.reporteService = reporteService;
  }

  @GetMapping("/report/{id}")
  public ResponseEntity<byte[]> generarReporte(
    @PathVariable Integer id
  ) throws Exception {

    byte[] report =
      reporteService.generarReporte(id);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(
      MediaType.APPLICATION_PDF
    );

    headers.add(
      "Content-Disposition",
      "inline; filename=Factura.pdf"
    );

    return new ResponseEntity<>(
      report,
      headers,
      HttpStatus.OK
    );
  }

  @GetMapping("/report-correspondencia/{id}")
  public ResponseEntity<byte[]> generarCorrespondencia(
    @PathVariable Integer id
  ) throws Exception {

    byte[] report =
      reporteService.generarCorrespondencia(id);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(
      MediaType.APPLICATION_PDF
    );

    headers.add(
      "Content-Disposition",
      "inline; filename=Correspondencia.pdf"
    );

    return new ResponseEntity<>(
      report,
      headers,
      HttpStatus.OK
    );
  }

  @GetMapping("/rotulo/{id}")
  public ResponseEntity<byte[]> generarRotulo(
    @PathVariable Integer id
  ) throws Exception {

    byte[] pdf =
      reporteService.generarRotulo(id);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(
      MediaType.APPLICATION_PDF
    );

    headers.add(
      "Content-Disposition",
      "inline; filename=Rotulo.pdf"
    );

    return new ResponseEntity<>(
      pdf,
      headers,
      HttpStatus.OK
    );
  }

  @GetMapping("/report-venta/{id}")
  public ResponseEntity<byte[]> generarTicketVenta(
    @PathVariable Integer id
  ) throws Exception {

    byte[] pdf =
      reporteService.generarTicketVenta(id);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(
      MediaType.APPLICATION_PDF
    );

    headers.add(
      "Content-Disposition",
      "inline; filename=VentaTicket.pdf"
    );

    return new ResponseEntity<>(
      pdf,
      headers,
      HttpStatus.OK
    );
  }

  @GetMapping("/report-documento/{id}")
  public ResponseEntity<byte[]> generarDocumento(
    @PathVariable Integer id
  ) throws Exception {

    byte[] pdf =
      reporteService.generarDocumento(id);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(
      MediaType.APPLICATION_PDF
    );

    headers.add(
      "Content-Disposition",
      "inline; filename=Documento.pdf"
    );

    return new ResponseEntity<>(
      pdf,
      headers,
      HttpStatus.OK
    );
  }

  @GetMapping("/report-material/{id}")
  public ResponseEntity<byte[]> generarMaterial(
    @PathVariable Integer id
  ) throws Exception {

    byte[] pdf =
      reporteService.generarMaterial(id);

    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(
      MediaType.APPLICATION_PDF
    );

    headers.add(
      "Content-Disposition",
      "inline; filename=Material.pdf"
    );

    return new ResponseEntity<>(
      pdf,
      headers,
      HttpStatus.OK
    );
  }
}
