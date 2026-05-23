
  package com.YuderTM.controlador;

import com.YuderTM.modelo.Factura;
import com.YuderTM.modelo.RespuestaFacturaDto;
import com.YuderTM.modelo.Rotulo;

import com.YuderTM.servicio.IFacturaService;
import com.YuderTM.servicio.IRotuloService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/facturas", produces = "application/json")
@CrossOrigin(origins = {
  "http://localhost:4200",
  "http://localhost:64764",
  "https://confienvios-app-two.vercel.app"
})
public class FacturaControlador {

  private final IFacturaService iFacturaService;
  private final IRotuloService iRotuloService;

  private static final Logger logger =
    LoggerFactory.getLogger(FacturaControlador.class);

  // inyección de dependencia
  public FacturaControlador(
    IFacturaService iFacturaService,
    IRotuloService iRotuloService
  ) {
    this.iFacturaService = iFacturaService;
    this.iRotuloService = iRotuloService;
  }

  // =============================
  // LISTAR TODOS
  // =============================
  @GetMapping
  public ResponseEntity<List<Factura>> listarFactura() {

    List<Factura> facturas =
      iFacturaService.listarFactura();

    logger.info("Facturas encontradas: " + facturas);

    return ResponseEntity.ok(facturas);
  }

  // =============================
  // BUSCAR POR ID
  // =============================
  @GetMapping("/{id}")
  public ResponseEntity<Factura> buscarFactura(
    @PathVariable Integer id
  ) {

    Factura factura =
      iFacturaService.buscarFacturaId(id);

    if (factura == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(factura);
  }

  // =============================
  // GUARDAR FACTURA
  // =============================
  @PostMapping
  public ResponseEntity<RespuestaFacturaDto> guardarFactura(
    @RequestBody Factura factura,
    @RequestParam(
      value = "autorizado",
      defaultValue = "false"
    ) boolean autorizado
  ) {

    RespuestaFacturaDto respuesta =
      new RespuestaFacturaDto();

    // verificar encomienda
    boolean tieneEncomiendaMes =
      iFacturaService.existeEncomiendaClienteMes(
        factura.getDocumento_cliente_dto()
      );

    if (tieneEncomiendaMes && !autorizado) {

      respuesta.setFactura(null);

      respuesta.setWarning(
        "⚠️ El cliente ya tiene una encomienda registrada " +
          "en el mes actual. ¿Desea continuar con la facturación?"
      );

      respuesta.setRequiereAutorizacion(true);

      return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(respuesta);
    }

    // guardar factura
    Factura facturaGuardada =
      iFacturaService.guardarFactura(factura);

    // crear rótulo
    Rotulo rotulo = new Rotulo();

    rotulo.setId_rotulo(
      facturaGuardada.getId_factura()
    );

    rotulo.setN_factura(
      facturaGuardada.getNumero_factura()
    );

    rotulo.setNombre_cliente_dto(
      facturaGuardada.getNombre_cliente_dto()
    );

    rotulo.setTipo_documento_dto(
      facturaGuardada.getTipo_documento_dto()
    );

    rotulo.setDocumento_cliente_dto(
      facturaGuardada.getDocumento_cliente_dto()
    );

    rotulo.setTd(facturaGuardada.getTd());
    rotulo.setNiu(facturaGuardada.getNiu());
    rotulo.setPabellon(facturaGuardada.getPabellon());
    rotulo.setEstructura(facturaGuardada.getEstructura());

    // guardar rótulo
    iRotuloService.guardarRotulo(rotulo);

    // respuesta
    respuesta.setFactura(facturaGuardada);
    respuesta.setWarning(null);
    respuesta.setRequiereAutorizacion(false);

    return ResponseEntity.ok(respuesta);
  }

  // =============================
  // ACTUALIZAR FACTURA
  // =============================
  @PutMapping("/{id}")
  public ResponseEntity<Factura> actualizarFactura(
    @PathVariable Integer id,
    @RequestBody Factura factura
  ) {

    factura.setId_factura(id);

    return ResponseEntity.ok(
      iFacturaService.guardarFactura(factura)
    );
  }

  // =============================
  // ELIMINAR FACTURA
  // =============================
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminarFactura(
    @PathVariable Integer id
  ) {

    iFacturaService.eliminarFacturaId(id);

    return ResponseEntity.noContent().build();
  }

  // =============================
  // ANULAR FACTURA
  // =============================
  @PatchMapping("/{id}/anular")
  public ResponseEntity<Factura> anularFactura(
    @PathVariable Integer id
  ) {

    Factura factura =
      iFacturaService.buscarFacturaId(id);

    if (factura == null) {
      return ResponseEntity.notFound().build();
    }

    factura.setEstado("2");

    Factura actualizada =
      iFacturaService.guardarFactura(factura);

    return ResponseEntity.ok(actualizada);
  }

  // =============================
  // DASHBOARD
  // =============================
  @GetMapping("/dashboard")
  public ResponseEntity<Map<String, Long>> getDashboard() {

    Map<String, Long> datos =
      new HashMap<>();

    datos.put(
      "dia",
      iFacturaService.contarFacturasDia()
    );

    datos.put(
      "mes",
      iFacturaService.contarFacturasMes()
    );

    datos.put(
      "anio",
      iFacturaService.contarFacturasAnio()
    );

    return ResponseEntity.ok(datos);
  }
}

