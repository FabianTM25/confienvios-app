package com.YuderTM.controlador;

import com.YuderTM.modelo.Factura;
<<<<<<< HEAD
import com.YuderTM.modelo.RespuestaFacturaDto;
import com.YuderTM.servicio.IFacturaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
=======
import com.YuderTM.servicio.IFacturaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
>>>>>>> c95e1604e7ea771cec1d1287270e1c842491141f
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.YuderTM.modelo.Rotulo;
import com.YuderTM.servicio.IRotuloService;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
<<<<<<< HEAD
@RequestMapping("facturas") //http://localhost:8080/Confienvios
@CrossOrigin(origins = "http://localhost:4200") // Para Angular
=======
@RequestMapping(value = "/api/facturas", produces = "application/json")//http://localhost:8080/Confienvios
/*@CrossOrigin(origins = {
    "http://localhost:4200",
    "https://confienvios-app-two.vercel.app/"  // reemplaza con tu URL real de Angular
})*/
@CrossOrigin(origins = "*")
>>>>>>> c95e1604e7ea771cec1d1287270e1c842491141f

public class FacturaControlador {

    private final IFacturaService iFacturaService;
    private final IRotuloService iRotuloService;
    private static final Logger logger = LoggerFactory.getLogger(FacturaControlador.class);

    //inyeccion de dependencia
    public FacturaControlador(IFacturaService iFacturaService, IRotuloService iRotuloService) {
        this.iFacturaService = iFacturaService;
        this.iRotuloService = iRotuloService;
    }

    // =============================
    // LISTAR TODOS
    // =============================
    @GetMapping
    public ResponseEntity<List<Factura>> listarFactura() {
        List<Factura> facturas = iFacturaService.listarFactura();
        logger.info("Facturas encontrados: " + facturas);
        facturas.forEach(factura -> logger.info(factura.toString()));//imprimir por consola
        return ResponseEntity.ok(iFacturaService.listarFactura());
    }

    // =============================
    // BUSCAR POR ID
    // =============================
    @GetMapping("/{id}")
    public ResponseEntity<Factura> buscarFactura(@PathVariable Integer id) {

        Factura factura = iFacturaService.buscarFacturaId(id);

        if (factura == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(factura);
    }

        // =============================
    // ACTUALIZAR factura
    // =============================
    @PutMapping("/{id}")
    public ResponseEntity<Factura> actualizarFactura(
            @PathVariable Integer id,
            @RequestBody Factura factura) {

        factura.setId_factura(id);

        return ResponseEntity.ok(iFacturaService.guardarFactura(factura));
    }

    // =============================
    // ELIMINAR USUARIO
    // =============================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFactura(@PathVariable Integer id) {

        iFacturaService.eliminarFacturaId(id);

        return ResponseEntity.noContent().build();
    }
//guardar factura
@PostMapping
<<<<<<< HEAD
public ResponseEntity<RespuestaFacturaDto> guardarFactura(@RequestBody Factura factura,
                                              @RequestParam(value = "autorizado", defaultValue = "false") boolean autorizado) {
   //verificacion
    RespuestaFacturaDto respuesta = new RespuestaFacturaDto();

    // 1. Verificar encomienda en el mes
    boolean tieneEncomiendaMes = iFacturaService
            .existeEncomiendaClienteMes(factura.getDocumento_cliente_dto());

    if (tieneEncomiendaMes && !autorizado) {
        // 2. Bloquear — NO se guarda nada, se pide autorización
        respuesta.setFactura(null);
        respuesta.setWarning("⚠️ El cliente ya tiene una encomienda registrada " +
                "en el mes actual. ¿Desea continuar con la facturación?");
        respuesta.setRequiereAutorizacion(true);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta); // ✅ tipo consistente
    }
=======
public ResponseEntity<Factura> guardarFactura(@RequestBody Factura factura){
>>>>>>> c95e1604e7ea771cec1d1287270e1c842491141f

    // guardar factura
    Factura facturaGuardada = iFacturaService.guardarFactura(factura);

    // crear rotulo
    Rotulo rotulo = new Rotulo();
    rotulo.setId_rotulo(facturaGuardada.getId_factura());

    rotulo.setN_factura(facturaGuardada.getNumero_factura());
    rotulo.setNombre_cliente_dto(facturaGuardada.getNombre_cliente_dto());
    rotulo.setTipo_documento_dto(facturaGuardada.getTipo_documento_dto());
    rotulo.setDocumento_cliente_dto(facturaGuardada.getDocumento_cliente_dto());
    rotulo.setTd(facturaGuardada.getTd());
    rotulo.setNiu(facturaGuardada.getNiu());
    rotulo.setPabellon(facturaGuardada.getPabellon());
    rotulo.setEstructura(facturaGuardada.getEstructura());

    // guardar rotulo
<<<<<<< HEAD
    //iRotuloService.guardarRotulo(rotulo);
    // 5. Respuesta exitosa
    respuesta.setFactura(facturaGuardada);
    respuesta.setWarning(null);
    respuesta.setRequiereAutorizacion(false);
    return ResponseEntity.ok(respuesta);
    //return ResponseEntity.ok(facturaGuardada);
=======
    iRotuloService.guardarRotulo(rotulo);

    return ResponseEntity.ok(facturaGuardada);
>>>>>>> c95e1604e7ea771cec1d1287270e1c842491141f
}
    @PatchMapping("/{id}/anular")
    public ResponseEntity<Factura> anularFactura(@PathVariable Integer id) {
        Factura factura = iFacturaService.buscarFacturaId(id);

        if (factura == null) {
            return ResponseEntity.notFound().build();
        }

        factura.setEstado("2");
        Factura actualizada = iFacturaService.guardarFactura(factura);

        return ResponseEntity.ok(actualizada);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Long>> getDashboard() {
        Map<String, Long> datos = new HashMap<>();
        datos.put("dia", iFacturaService.contarFacturasDia());
        datos.put("mes", iFacturaService.contarFacturasMes());
        datos.put("anio", iFacturaService.contarFacturasAnio());
        return ResponseEntity.ok(datos);
    }
}