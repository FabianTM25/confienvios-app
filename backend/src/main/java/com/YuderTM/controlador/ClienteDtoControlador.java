package com.YuderTM.controlador;

import com.YuderTM.modelo.Cliente_dto;
import com.YuderTM.servicio.IClienteDtoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/clienteDto", produces = "application/json")//http://localhost:8080/Confienvios
@CrossOrigin(origins = {
    "http://localhost:4200",
    "https://confienvios-app-two.vercel.app/"  // reemplaza con tu URL real de Angular
})

public class ClienteDtoControlador {

    private final IClienteDtoService iClienteDtoService;

    private static final Logger logger = LoggerFactory.getLogger(ClienteDtoControlador.class);

    //inyeccion de dependencia
    public ClienteDtoControlador(IClienteDtoService iClienteDtoService) {
        this.iClienteDtoService = iClienteDtoService;
    }

    // =============================
    // LISTAR TODOS
    // =============================
    @GetMapping
    public ResponseEntity<List<Cliente_dto>> listarClienteDto() {
        List<Cliente_dto> cliente_dto= iClienteDtoService.listarClienteDto();
        logger.info("Usuarios encontrados: " + cliente_dto);
        cliente_dto.forEach(usuario -> logger.info(usuario.toString()));//imprimir por consola
        return ResponseEntity.ok(iClienteDtoService.listarClienteDto());
    }

    // =============================
    // BUSCAR POR ID
    // =============================
    @GetMapping("/{id}")
    public ResponseEntity<Cliente_dto> buscarUClienteDto(@PathVariable Integer id) {

        Cliente_dto cliente_dto= iClienteDtoService.buscarPorIdDto(id);

        if (cliente_dto== null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cliente_dto);
    }

    // =============================
    // CREAR USUARIO
    // =============================
    @PostMapping
    public ResponseEntity<Cliente_dto> crearClienteDto(@RequestBody Cliente_dto cliente_dto) {
        return ResponseEntity.ok(iClienteDtoService.guardarClienteDto(cliente_dto));
    }

    // =============================
    // ACTUALIZAR USUARIO
    // =============================
    @PutMapping("/{id}")
    public ResponseEntity<Cliente_dto> actualizarCliente_dto(
            @PathVariable Integer id,
            @RequestBody Cliente_dto cliente_dto) {

        cliente_dto.setIdClienteDto(id);

        return ResponseEntity.ok(iClienteDtoService.guardarClienteDto(cliente_dto));
    }

    // =============================
    // ELIMINAR USUARIO
    // =============================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarClienteDto(@PathVariable Integer id) {

        iClienteDtoService.eliminarClienteDto(id);

        return ResponseEntity.noContent().build();
    }
// =============================
    // buscar  USUARIO- endpoint
    // =============================

    @GetMapping("/buscar")
    public ResponseEntity<Cliente_dto> buscarCliente(@RequestParam String texto) {
        System.out.println("Documento recibido: " + texto);

        Cliente_dto cliente = iClienteDtoService.buscarDocumento(texto);

        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cliente);
    }

}
