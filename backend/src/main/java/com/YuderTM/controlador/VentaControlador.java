package com.YuderTM.controlador;

import com.YuderTM.modelo.Factura;
import com.YuderTM.modelo.Venta;
import com.YuderTM.modelo.VentaRequestDto;

import com.YuderTM.servicio.IFacturaService;
import com.YuderTM.servicio.IVentaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/api/ventas", produces = "application/json")
@CrossOrigin(origins = {
  "http://localhost:4200",
  "http://localhost:64764",
  "https://confienvios-app-two.vercel.app",
  "https://confienvios-app.vercel.app"
})
public class VentaControlador {

  private final IVentaService iVentaService;
  private final IFacturaService iFacturaService;

  public VentaControlador(
    IVentaService iVentaService,
    IFacturaService iFacturaService
  ) {
    this.iVentaService = iVentaService;
    this.iFacturaService = iFacturaService;
  }

  // =============================
  // LISTAR TODAS
  // =============================
  @GetMapping
  public ResponseEntity<List<Venta>> listarVenta() {
    return ResponseEntity.ok(iVentaService.listarVenta());
  }

  // =============================
  // BUSCAR POR ID
  // =============================
  @GetMapping("/{id}")
  public ResponseEntity<Venta> buscarVenta(@PathVariable Integer id) {

    Venta venta = iVentaService.buscarVentaId(id);

    if (venta == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(venta);
  }

  // =============================
  // BUSCAR FACTURA POR NUMERO (para traer los datos del cliente)
  // =============================
  @GetMapping("/buscar-factura/{numero}")
  public ResponseEntity<Factura> buscarFactura(@PathVariable String numero) {

    Factura factura = iFacturaService.buscarFacturaPorNumero(numero);

    if (factura == null) {
      throw new NoSuchElementException("No existe una factura con el numero " + numero);
    }

    return ResponseEntity.ok(factura);
  }

  // =============================
  // CREAR VENTA
  // =============================
  @PostMapping
  public ResponseEntity<Venta> crearVenta(@RequestBody VentaRequestDto request) {

    Venta venta = iVentaService.crearVenta(
      request.getNumero_factura(),
      request.getPeso(),
      request.isCon_caja(),
      request.getObservaciones(),
      request.getValor_avaluo()
    );

    return ResponseEntity.ok(venta);
  }

  // =============================
  // ELIMINAR VENTA
  // =============================
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminarVenta(@PathVariable Integer id) {
    iVentaService.eliminarVentaId(id);
    return ResponseEntity.noContent().build();
  }

  // =============================
  // ANULAR VENTA
  // =============================
  @PatchMapping("/{id}/anular")
  public ResponseEntity<Venta> anularVenta(@PathVariable Integer id) {
    return ResponseEntity.ok(iVentaService.anularVenta(id));
  }
}
