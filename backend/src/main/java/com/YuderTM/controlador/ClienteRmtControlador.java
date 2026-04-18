package com.YuderTM.controlador;

import com.YuderTM.modelo.Cliente_rmt;

import com.YuderTM.servicio.IClienteRmtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/clientes", produces = "application/json") //http://localhost:8080/Confienvios
/*@CrossOrigin(origins = {
    "http://localhost:4200",
    "https://confienvios-app-two.vercel.app/"  // reemplaza con tu URL real de Angular
})*/
@CrossOrigin(origins = "*")

public class ClienteRmtControlador {

    private final IClienteRmtService iClienteRmtService;

    private static final Logger logger = LoggerFactory.getLogger(ClienteRmtControlador.class);

    //inyeccion de dependencia
    public ClienteRmtControlador(IClienteRmtService iClienteRmtService) {
        this.iClienteRmtService = iClienteRmtService;
    }

    // =============================
    // LISTAR TODOS
    // =============================
    @GetMapping
    public ResponseEntity<List<Cliente_rmt>> listarClienteRtm() {
        List<Cliente_rmt> cliente_rmt = iClienteRmtService.listarClienteRtm();
        logger.info("Usuarios encontrados: " + cliente_rmt);
        cliente_rmt.forEach(usuario -> logger.info(usuario.toString()));//imprimir por consola
        return ResponseEntity.ok(iClienteRmtService.listarClienteRtm());
    }

    // =============================
    // BUSCAR POR ID
    // =============================
    @GetMapping("/{id}")
    public ResponseEntity<Cliente_rmt> buscarUClienteRmt(@PathVariable Integer id) {

        Cliente_rmt cliente_rmt = iClienteRmtService.buscarPorIdRtm(id);

        if (cliente_rmt == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cliente_rmt);
    }

    // =============================
    // CREAR USUARIO
    // =============================
    @PostMapping
    public ResponseEntity<Cliente_rmt> crearClienteRmt(@RequestBody Cliente_rmt cliente_rmt) {
        return ResponseEntity.ok(iClienteRmtService.guardarClienteRtm(cliente_rmt));
    }

    // =============================
    // ACTUALIZAR USUARIO
    // =============================
    @PutMapping("/{id}")
    public ResponseEntity<Cliente_rmt> actualizarCliente_rmt(
            @PathVariable Integer id,
            @RequestBody Cliente_rmt cliente_rmt) {

        cliente_rmt.setIdClienteRmt(id);

        return ResponseEntity.ok(iClienteRmtService.guardarClienteRtm(cliente_rmt));
    }

    // =============================
    // ELIMINAR USUARIO
    // =============================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarClienteRtm(@PathVariable Integer id) {

        iClienteRmtService.eliminarClienteRtm(id);

        return ResponseEntity.noContent().build();
    }
// =============================
    // buscar  USUARIO- endpoint
    // =============================

    @GetMapping("/buscar")
    public ResponseEntity<Cliente_rmt> buscarCliente(@RequestParam String texto) {
        System.out.println("Documento recibido: " + texto);

        Cliente_rmt cliente = iClienteRmtService.buscarDocumento(texto);

        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cliente);
    }



}
