package com.YuderTM.controlador;

import com.YuderTM.modelo.Material;
import com.YuderTM.servicio.IMaterialService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/materiales", produces = "application/json")
@CrossOrigin(origins = {
  "http://localhost:4200",
  "http://localhost:64764",
  "https://confienvios-app-two.vercel.app",
  "https://confienvios-app.vercel.app"
})
public class MaterialControlador {

  private final IMaterialService iMaterialService;

  public MaterialControlador(IMaterialService iMaterialService) {
    this.iMaterialService = iMaterialService;
  }

  // =============================
  // LISTAR TODOS
  // =============================
  @GetMapping
  public ResponseEntity<List<Material>> listarMateriales() {
    return ResponseEntity.ok(iMaterialService.listarMateriales());
  }

  // =============================
  // BUSCAR POR ID
  // =============================
  @GetMapping("/{id}")
  public ResponseEntity<Material> buscarMaterial(@PathVariable Integer id) {

    Material material = iMaterialService.buscarMaterialId(id);

    if (material == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(material);
  }

  // =============================
  // CREAR MATERIAL
  // =============================
  @PostMapping
  public ResponseEntity<Material> crearMaterial(@RequestBody Material material) {
    return ResponseEntity.ok(iMaterialService.guardarMaterial(material));
  }

  // =============================
  // ACTUALIZAR MATERIAL
  // =============================
  @PutMapping("/{id}")
  public ResponseEntity<Material> actualizarMaterial(
    @PathVariable Integer id,
    @RequestBody Material material
  ) {
    material.setId_material(id);
    return ResponseEntity.ok(iMaterialService.guardarMaterial(material));
  }

  // =============================
  // ELIMINAR MATERIAL
  // =============================
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminarMaterial(@PathVariable Integer id) {
    iMaterialService.eliminarMaterialId(id);
    return ResponseEntity.noContent().build();
  }
}
