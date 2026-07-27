package com.YuderTM.controlador;

import com.YuderTM.modelo.Configuracion;
import com.YuderTM.modelo.RangoPeso;
import com.YuderTM.servicio.IConfiguracionService;
import com.YuderTM.servicio.IRangoPesoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/configuracion", produces = "application/json")
@CrossOrigin(origins = {
  "http://localhost:4200",
  "http://localhost:64764",
  "https://confienvios-app-two.vercel.app",
  "https://confienvios-app.vercel.app"
})
public class ConfiguracionControlador {

  private final IConfiguracionService iConfiguracionService;
  private final IRangoPesoService iRangoPesoService;

  public ConfiguracionControlador(
    IConfiguracionService iConfiguracionService,
    IRangoPesoService iRangoPesoService
  ) {
    this.iConfiguracionService = iConfiguracionService;
    this.iRangoPesoService = iRangoPesoService;
  }

  // =============================
  // OBTENER CONFIGURACION
  // =============================
  @GetMapping
  public ResponseEntity<Configuracion> obtenerConfiguracion() {
    return ResponseEntity.ok(iConfiguracionService.obtenerConfiguracion());
  }

  // =============================
  // ACTUALIZAR CONFIGURACION
  // =============================
  @PutMapping
  public ResponseEntity<Configuracion> actualizarConfiguracion(
    @RequestBody Configuracion configuracion
  ) {
    return ResponseEntity.ok(
      iConfiguracionService.actualizarConfiguracion(configuracion)
    );
  }

  // =============================
  // RANGOS DE PESO
  // =============================
  @GetMapping("/rangos")
  public ResponseEntity<List<RangoPeso>> listarRangos() {
    return ResponseEntity.ok(iRangoPesoService.listarRangos());
  }

  @PostMapping("/rangos")
  public ResponseEntity<RangoPeso> crearRango(
    @RequestBody RangoPeso rangoPeso
  ) {
    rangoPeso.setId_rango(null);
    return ResponseEntity.ok(iRangoPesoService.guardarRango(rangoPeso));
  }

  @PutMapping("/rangos/{id}")
  public ResponseEntity<RangoPeso> actualizarRango(
    @PathVariable Integer id,
    @RequestBody RangoPeso rangoPeso
  ) {
    rangoPeso.setId_rango(id);
    return ResponseEntity.ok(iRangoPesoService.guardarRango(rangoPeso));
  }

  @DeleteMapping("/rangos/{id}")
  public ResponseEntity<Void> eliminarRango(
    @PathVariable Integer id
  ) {
    iRangoPesoService.eliminarRango(id);
    return ResponseEntity.noContent().build();
  }
}
