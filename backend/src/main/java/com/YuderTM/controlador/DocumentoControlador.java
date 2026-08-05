package com.YuderTM.controlador;

import com.YuderTM.modelo.Documento;
import com.YuderTM.servicio.IDocumentoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/documentos", produces = "application/json")
@CrossOrigin(origins = {
  "http://localhost:4200",
  "http://localhost:64764",
  "https://confienvios-app-two.vercel.app",
  "https://confienvios-app.vercel.app"
})
public class DocumentoControlador {

  private final IDocumentoService iDocumentoService;

  public DocumentoControlador(IDocumentoService iDocumentoService) {
    this.iDocumentoService = iDocumentoService;
  }

  // =============================
  // LISTAR TODOS
  // =============================
  @GetMapping
  public ResponseEntity<List<Documento>> listarDocumentos() {
    return ResponseEntity.ok(iDocumentoService.listarDocumentos());
  }

  // =============================
  // BUSCAR POR ID
  // =============================
  @GetMapping("/{id}")
  public ResponseEntity<Documento> buscarDocumento(@PathVariable Integer id) {

    Documento documento = iDocumentoService.buscarDocumentoId(id);

    if (documento == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(documento);
  }

  // =============================
  // CREAR DOCUMENTO
  // =============================
  @PostMapping
  public ResponseEntity<Documento> crearDocumento(@RequestBody Documento documento) {
    return ResponseEntity.ok(iDocumentoService.guardarDocumento(documento));
  }

  // =============================
  // ACTUALIZAR DOCUMENTO
  // =============================
  @PutMapping("/{id}")
  public ResponseEntity<Documento> actualizarDocumento(
    @PathVariable Integer id,
    @RequestBody Documento documento
  ) {
    documento.setId_documento(id);
    return ResponseEntity.ok(iDocumentoService.guardarDocumento(documento));
  }

  // =============================
  // ELIMINAR DOCUMENTO
  // =============================
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminarDocumento(@PathVariable Integer id) {
    iDocumentoService.eliminarDocumentoId(id);
    return ResponseEntity.noContent().build();
  }
}
